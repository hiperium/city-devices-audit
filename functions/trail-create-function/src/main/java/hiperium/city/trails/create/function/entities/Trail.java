package hiperium.city.trails.create.function.entities;

import hiperium.city.trails.create.function.commons.DeviceOperation;

/**
 * The Trail class represents a trail record in the Trails table of DynamoDB.
 * It contains information such as the trail ID, city ID, task ID, device ID, device operation, and execution date.
 */
public record Trail(
    String id,
    String cityId,
    String taskId,
    String deviceId,
    DeviceOperation deviceOperation,
    String createdAt
) {
    public static final String TABLE_NAME = "Trails";
    public static final String ID_FIELD = "id";
    public static final String CITY_ID_FIELD = "cityId";
    public static final String TASK_ID_FIELD = "taskId";
    public static final String DEVICE_ID_FIELD = "deviceId";
    public static final String OPERATION_FIELD = "deviceOperation";
    public static final String CREATED_AT_FIELD = "createdAt";
}
