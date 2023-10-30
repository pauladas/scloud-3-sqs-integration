package br.com.paulojof.exception;

import lombok.Getter;

@Getter
public class SqsClientException extends Exception {

    private final String errorCode;

    public SqsClientException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SqsClientException(String message) {
        super(message);
        this.errorCode = null;
    }

}
