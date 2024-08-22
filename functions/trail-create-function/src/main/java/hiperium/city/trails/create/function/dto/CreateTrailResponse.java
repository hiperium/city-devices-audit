package hiperium.city.trails.create.function.dto;

import hiperium.cities.commons.dto.ErrorResponse;

/**
 * This class represents the response of an event operation.
 */
public record CreateTrailResponse(

    Integer statusCode,
    String body,
    ErrorResponse error) {

    /**
     * The Builder class is a utility class that provides methods for constructing a CreateTrailResponse object
     * with various properties.
     */
    public static class Builder {
        private Integer statusCode;
        private String body;
        private ErrorResponse error;

        /**
         * Sets the status code of the CreateTrailResponse.
         *
         * @param statusCode the status code to be set
         * @return the Builder object with the updated status code
         */
        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        /**
         * Sets the body of the CreateTrailResponse.
         *
         * @param body the body to be set
         * @return the Builder object with the updated body
         */
        public Builder body(String body) {
            this.body = body;
            return this;
        }

        /**
         * Sets the error response for the device update response builder.
         *
         * @param error the error response to be set
         * @return the updated builder instance
         */
        public Builder error(ErrorResponse error) {
            this.error = error;
            return this;
        }

        /**
         * Builds a CreateTrailResponse object with the provided response code, body, and error.
         *
         * @return A new CreateTrailResponse object.
         */
        public CreateTrailResponse build() {
            return new CreateTrailResponse(statusCode, body, error);
        }
    }
}
