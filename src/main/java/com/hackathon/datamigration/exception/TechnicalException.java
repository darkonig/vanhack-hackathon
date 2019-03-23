package com.hackathon.datamigration.exception;

public class TechnicalException extends RuntimeException {

    public TechnicalException(String msg) {
        super(msg);
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
