package hiperium.city.trails.create.function.mappers;

import hiperium.city.trails.create.function.dto.EventBridgeDetail;
import hiperium.city.trails.create.function.entities.Trail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;

/**
 * The TrailMapper interface is responsible for mapping an {@code EventBridgeDetail} object to a {@code Trail} object.
 * It uses the MapStruct library and is annotated with {@code @Mapper(componentModel = "spring")} to enable Spring component scanning and injection.
 * The mapping functionality is defined by the single method {@code mapToTrail} which takes an {@code EventBridgeDetail} object as input and returns a {@code Trail} object.
 * This method applies the MapStruct annotations {@code @Mapping} to specify the mappings between the source and target object properties.
 *
 * @see Trail
 * @see EventBridgeDetail
 */
@Mapper(componentModel = "spring")
public interface TrailMapper {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Maps an {@code EventBridgeDetail} object to a {@code Trail} object.
     *
     * @param eventDetail The {@code EventBridgeDetail} object to be mapped.
     * @return The mapped {@code Trail} object.
     */
    @Mapping(target = "id",        expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now().format(TrailMapper.DATE_TIME_FORMATTER))")
    Trail mapToTrail(EventBridgeDetail eventDetail);
}
