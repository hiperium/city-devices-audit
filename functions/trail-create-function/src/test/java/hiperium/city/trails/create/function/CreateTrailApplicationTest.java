package hiperium.city.trails.create.function;

import hiperium.city.trails.create.function.common.TestContainersBase;
import hiperium.city.trails.create.function.configurations.FunctionConfig;
import hiperium.city.trails.create.function.dto.CreateTrailResponse;
import hiperium.city.trails.create.function.utils.TestsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@FunctionalSpringBootTest(classes = CreateTrailApplication.class)
class CreateTrailApplicationTest extends TestContainersBase {

    @Autowired
    private FunctionCatalog functionCatalog;

    @Autowired
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @BeforeEach
    void init() {
        TestsUtils.waitForDynamoDbToBeReady(this.dynamoDbAsyncClient);
    }

    @ParameterizedTest
    @DisplayName("Valid requests")
    @ValueSource(strings = {
        "requests/valid/lambda-valid-id-request.json"
    })
    void givenValidEvent_whenInvokeLambdaFunction_thenExecuteSuccessfully(String jsonFilePath) throws IOException {
        Function<Message<byte[]>, Mono<CreateTrailResponse>> function = this.getFunctionUnderTest();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath)) {
            assert inputStream != null;
            Message<byte[]> requestMessage = TestsUtils.createMessage(inputStream.readAllBytes());

            StepVerifier.create(function.apply(requestMessage))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.error()).isNull();

                    // The status code should be a success code.
                    int statusCode = response.statusCode();
                    assertThat(statusCode >= HttpStatus.OK.value() && statusCode <= HttpStatus.IM_USED.value()).isTrue();
                })
                .verifyComplete();
        }
    }

    @ParameterizedTest
    @DisplayName("Non-valid requests")
    @ValueSource(strings = {
        "requests/invalid/empty-city-id.json",
        "requests/invalid/empty-device-id.json",
        "requests/invalid/empty-task-id.json",
        "requests/invalid/wrong-city-id.json",
        "requests/invalid/wrong-device-id.json",
        "requests/invalid/wrong-task-id.json",
        "requests/invalid/wrong-payload-lambda.json",
        "requests/invalid/wrong-payload-event.json"
    })
    void givenNonValidEvents_whenInvokeLambdaFunction_thenReturnErrors(String jsonFilePath) throws IOException {
        Function<Message<byte[]>, Mono<CreateTrailResponse>> function = this.getFunctionUnderTest();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath)) {
            assert inputStream != null;
            Message<byte[]> requestMessage = TestsUtils.createMessage(inputStream.readAllBytes());

            StepVerifier.create(function.apply(requestMessage))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.error()).isNotNull();

                    // The status code should be an error code.
                    int statusCode = response.error().errorCode();
                    assertThat(statusCode >= HttpStatus.OK.value() && statusCode <= HttpStatus.IM_USED.value()).isFalse();
                })
                .verifyComplete();
        }
    }

    private Function<Message<byte[]>, Mono<CreateTrailResponse>> getFunctionUnderTest() {
        Function<Message<byte[]>, Mono<CreateTrailResponse>> function = this.functionCatalog.lookup(Function.class,
            FunctionConfig.FUNCTION_BEAN_NAME);
        assertThat(function).isNotNull();
        return function;
    }
}
