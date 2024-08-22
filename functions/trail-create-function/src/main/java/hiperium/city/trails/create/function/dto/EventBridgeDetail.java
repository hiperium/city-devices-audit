package hiperium.city.trails.create.function.dto;

import hiperium.cities.commons.annotations.ValidUUID;
import hiperium.city.trails.create.function.commons.DeviceOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The {@code EventBridgeDetail} class represents the details of an event to be sent to an event bridge.
 * It contains information such as the task ID, device ID, city ID, and device operation.
 *
 * @param taskId The task ID associated with the event.
 * @param deviceId The device ID associated with the event.
 * @param cityId The city ID associated with the event.
 * @param deviceOperation The device operation associated with the event.
 */
public record EventBridgeDetail (

    @NotBlank(message = "Task ID cannot be blank.")
    @NotEmpty(message = "Task ID cannot be empty.")
    @ValidUUID(message = "Task ID must have a valid format.")
    String taskId,

    @NotBlank(message = "Device ID cannot be blank.")
    @NotEmpty(message = "Device ID cannot be empty.")
    @ValidUUID(message = "Device ID must have a valid format.")
    String deviceId,

    @NotBlank(message = "City ID cannot be blank.")
    @NotEmpty(message = "City ID cannot be empty.")
    @ValidUUID(message = "City ID must have a valid format.")
    String cityId,

    @NotNull(message = "Device operation cannot be null.")
    DeviceOperation deviceOperation
) {
}
