package hiperium.city.trails.create.function.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Represents a request for an event.
 */
public record EventBridgeRequest(

    String id,
    String version,
    String source,
    String account,
    String time,
    String region,
    List<String> resources,

    @JsonProperty("detail-type")
    String detailType,

    @Valid
    @NotNull(message = "Detail cannot be null.")
    EventBridgeDetail detail){
}
