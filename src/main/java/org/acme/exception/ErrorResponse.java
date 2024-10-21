package org.acme.exception;

import java.time.LocalDateTime;

/**
 * This class is used to display the exception as below fields format.
 */
public class ErrorResponse {
    private final String message;
    private final int statusCode;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
