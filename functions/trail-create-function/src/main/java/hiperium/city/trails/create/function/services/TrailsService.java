package hiperium.city.trails.create.function.services;

import hiperium.city.trails.create.function.dto.EventBridgeRequest;
import hiperium.city.trails.create.function.mappers.TrailMapper;
import hiperium.city.trails.create.function.repositories.TrailsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The TrailsService class is responsible for handling trail-related operations.
 * It provides methods to update the device status using EventBridgeRequest objects.
 */
@Service
public class TrailsService {

    private final TrailMapper trailMapper;
    private final TrailsRepository trailsRepository;

    /**
     * The TrailsService class is responsible for handling trail-related operations.
     * It provides methods to update the device status using EventBridgeRequest objects.
     *
     * @see EventBridgeRequest
     */
    public TrailsService(TrailMapper trailMapper, TrailsRepository trailsRepository) {
        this.trailMapper = trailMapper;
        this.trailsRepository = trailsRepository;
    }

    /**
     * Updates the status of a device in the Trails table of DynamoDB.
     *
     * @param eventBridgeRequest The {@code EventBridgeRequest} object containing the details of the device and the status update.
     * @return A {@code Mono<Void>} that completes successfully if the device status is updated successfully, or completes with an error
     * if there is a failure during the update process.
     */
    public Mono<Void> create(final EventBridgeRequest eventBridgeRequest) {
        return Mono.just(eventBridgeRequest)
            .map(EventBridgeRequest::detail)
            .map(this.trailMapper::mapToTrail)
            .flatMap(this.trailsRepository::create);
    }
}
