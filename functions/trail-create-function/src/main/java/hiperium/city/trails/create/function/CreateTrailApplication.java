package hiperium.city.trails.create.function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The CreateTrailApplication class is the main class of the application that starts the application by running the main method.
 * It is annotated with the @SpringBootApplication annotation, which is used to enable various Spring Boot features in the application.
 * <p>
 * The main method is responsible for starting the application by running the SpringApplication.run() method.
 * It takes the command line arguments as input.
 */
@SpringBootApplication
public class CreateTrailApplication {

    /**
     * The main method is the entry point of the application.
     * It is responsible for starting the application by running the {@link SpringApplication#run(Class, String...)} method.
     *
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(CreateTrailApplication.class, args);
    }
}
