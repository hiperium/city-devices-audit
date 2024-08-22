package hiperium.city.trails.create.function.configurations;

import hiperium.cities.commons.loggers.HiperiumLogger;
import hiperium.city.trails.create.function.dto.CreateTrailResponse;
import hiperium.city.trails.create.function.functions.CreateFunction;
import hiperium.city.trails.create.function.services.TrailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * This class represents the configuration for functions in the application.
 */
@Configuration(proxyBeanMethods = false)
public class FunctionConfig {

    public static final String FUNCTION_BEAN_NAME = "create";
    private static final HiperiumLogger LOGGER = new HiperiumLogger(FunctionConfig.class);

    private final TrailsService trailsService;

    /**
     * This class represents the configuration for functions in the application.
     *
     * @param trailsService The TrailsService instance used for trail-related operations.
     */
    public FunctionConfig(TrailsService trailsService) {
        this.trailsService = trailsService;
    }

    /**
     * Returns a function bean that is responsible for updating the status of a trail in response to a request.
     * The function takes a request message containing a payload as a byte array and returns a Mono that emits a CreateTrailResponse object.
     *
     * @return a function bean of type Function<Message<byte[]>, Mono<CreateTrailResponse>> that updates the status of a trail in response to a request
     */
    @Bean(FUNCTION_BEAN_NAME)
    public Function<Message<byte[]>, Mono<CreateTrailResponse>> updateStatusFunction() {
        LOGGER.debug("Creating Trail Function bean...");
        return new CreateFunction(this.trailsService);
    }
}
