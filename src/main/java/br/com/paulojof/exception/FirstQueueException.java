package br.com.paulojof.exception;

import lombok.Getter;

@Getter
public class FirstQueueException extends Exception {

    public FirstQueueException(String message) {
        super(message);
    }

}
