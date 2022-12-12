package com.example.app.exception;

import jakarta.validation.ConstraintViolationException;

public class InvalidRequestException extends Exception {
    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

    public InvalidRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static InvalidRequestException wrap(ConstraintViolationException e) {
        StringBuffer sb = new StringBuffer("Invalid input: \n");
        for (var constraint : e.getConstraintViolations()) {
            sb.append("\t").append(constraint.getPropertyPath().toString()).append(": ").append(constraint.getMessage()).append("\n");
        }

        return new InvalidRequestException(sb.toString(), e);
    }
}
