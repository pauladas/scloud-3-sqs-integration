package br.com.paulojof.exception;

import lombok.Getter;

@Getter
public class ThirdQueueException extends Exception {

    public ThirdQueueException(String message) {
        super(message);
    }

}
