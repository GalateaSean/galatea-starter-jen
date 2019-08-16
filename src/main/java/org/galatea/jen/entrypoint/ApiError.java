package org.galatea.jen.entrypoint;

import lombok.*;
import org.springframework.http.HttpStatus;

@Value
public class ApiError {

    @NonNull
    private HttpStatus status;

    @NonNull
    private String message;

}
