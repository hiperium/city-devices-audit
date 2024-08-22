package hiperium.city.trails.create.function.functions;

import hiperium.city.trails.create.function.dto.CreateTrailResponse;
import hiperium.city.trails.create.function.services.TrailsService;
import hiperium.city.trails.create.function.utils.FunctionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * The CreateFunction class implements the Function interface to create a trail in response to a request.
 * It takes a request message containing a payload as a byte array and returns a Mono that emits a CreateTrailResponse object.
 */
public class CreateFunction implements Function<Message<byte[]>, Mono<CreateTrailResponse>> {

    private final TrailsService trailsService;

    /**
     * Constructs a CreateFunction object with the provided TrailsService instance.
     *
     * @param trailsService The TrailsService instance to be used for trail-related operations.
     */
    public CreateFunction(TrailsService trailsService) {
        this.trailsService = trailsService;
    }

    /**
     * Applies the create trail function to the given request message.
     *
     * @param requestMessage the request message containing the payload as a byte array
     * @return a Mono that emits a CreateTrailResponse object
     */
    @Override
    public Mono<CreateTrailResponse> apply(Message<byte[]> requestMessage) {
        return Mono.fromCallable(() -> FunctionUtils.deserializeRequest(requestMessage))
            .doOnNext(FunctionUtils::validateRequest)
            .flatMap(this.trailsService::create)
            .then(this.createResponse())
            .onErrorResume(FunctionUtils::handleRuntimeException);
    }

    private Mono<CreateTrailResponse> createResponse() {
        return Mono.fromSupplier(() -> new CreateTrailResponse.Builder()
            .statusCode(HttpStatus.OK.value())
            .body("Trail created successfully.")
            .build());
    }
}
