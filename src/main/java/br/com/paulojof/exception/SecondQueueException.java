package br.com.paulojof.exception;

import lombok.Getter;

@Getter
public class SecondQueueException extends Exception {

    public SecondQueueException(String message) {
        super(message);
    }

}
