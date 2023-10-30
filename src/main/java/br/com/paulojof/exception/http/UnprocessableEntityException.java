package br.com.paulojof.exception.http;

import org.springframework.http.HttpStatus;

import br.com.paulojof.exception.SqsClientException;
import br.com.paulojof.model.response.ErrorResponse;

public class UnprocessableEntityException extends RestException {

    private String responseBodyCode;

    private ErrorResponse responseBody;

    public UnprocessableEntityException(String responseBodyCode) {
        this.responseBodyCode = responseBodyCode;
    }

    public UnprocessableEntityException(ErrorResponse responseBody) {
        this.responseBody = responseBody;
    }

    public UnprocessableEntityException(SqsClientException sqsClientException) {
        this.responseBodyCode = sqsClientException.getErrorCode();
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public String getResponseBodyCode() {
        return responseBodyCode;
    }

    @Override
    public ErrorResponse getResponseBody() {
        return responseBody;
    }

}
