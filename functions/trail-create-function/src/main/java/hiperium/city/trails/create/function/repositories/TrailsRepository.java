package hiperium.city.trails.create.function.repositories;

import hiperium.cities.commons.exceptions.CityException;
import hiperium.cities.commons.loggers.HiperiumLogger;
import hiperium.city.trails.create.function.entities.Trail;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;
import java.util.UUID;

/**
 * The TrailsRepository class is responsible for interacting with the Trails table in DynamoDB.
 * It provides methods to create and retrieve trail records.
 */
@Repository
public class TrailsRepository {

    private static final HiperiumLogger LOGGER = new HiperiumLogger(TrailsRepository.class);

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    /**
     * The TrailsRepository class is responsible for interacting with the Trails table in DynamoDB.
     * It provides methods to create and retrieve trail records.
     *
     * @param dynamoDbAsyncClient An instance of the DynamoDbAsyncClient used to interact with DynamoDB.
     */
    public TrailsRepository(DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    /**
     * Creates a new Trail in the Trails table of DynamoDB.
     *
     * @param trail The Trail object representing the data to be inserted into the table.
     * @return A Mono that completes successfully if the Trail is inserted successfully, or completes with an error
     * if there is a failure during the insertion process.
     */
    public Mono<Void> create(@NonNull Trail trail) {
        PutItemRequest putItemRequest = this.buildPutItemRequest(trail);
        return Mono.fromCompletionStage(this.dynamoDbAsyncClient.putItem(putItemRequest))
            .doOnError(exception -> LOGGER.error("Couldn't insert a Trail.", exception.getMessage(), trail))
            .onErrorMap(DynamoDbException.class, exception -> new CityException("Error when inserting a Trail."))
            .then();
    }

    private PutItemRequest buildPutItemRequest(Trail trail) {
        return PutItemRequest.builder()
            .item(Map.of(
                Trail.ID_FIELD, AttributeValue.fromS(UUID.randomUUID().toString()),
                Trail.CITY_ID_FIELD, AttributeValue.fromS(trail.cityId()),
                Trail.TASK_ID_FIELD, AttributeValue.fromS(trail.taskId()),
                Trail.DEVICE_ID_FIELD, AttributeValue.fromS(trail.deviceId()),
                Trail.OPERATION_FIELD, AttributeValue.fromS(trail.deviceOperation().name()),
                Trail.CREATED_AT_FIELD, AttributeValue.fromS(trail.createdAt())))
            .tableName(Trail.TABLE_NAME)
            .build();
    }
}
