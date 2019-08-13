package org.galatea.jen.entrypoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles all exceptions that are thrown by the RequestController
 */

@ControllerAdvice //annotation used for unified exception
                  // handling throughout a while application
@Slf4j
public class RestExceptionHandler {

    /**
     * This handler handles cases in which the user fails to provide both
     * required parameters (number of days of stock data to return, as well as the
     * company ticker symbol)
     * @param exception
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestException(
            final MissingServletRequestParameterException exception){
        log.debug("Missing Parameter sent ", exception);
        String errorMessage = new StringBuilder().append("Required parameters 'symbol' and 'days' missing. Please try your request again " +
                "with all the required parameters.").toString();

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST,errorMessage);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(
            final NumberFormatException exception){
        String errorMessage = new StringBuilder().append("An integer is required for the parameter 'days' but input string ")
                .append(exception.getMessage().substring(exception.getMessage().indexOf(":") + 2))
                .append(" was found")
                .toString();
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,errorMessage);
        return buildResponseEntity(error);
    }



    /**
     * This method creates a new error response entity
     * @param apiError
     * @return
     */
    private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
