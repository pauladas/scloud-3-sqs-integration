package br.com.paulojof.exception.http;

import br.com.paulojof.exception.ExampleClientException;
import br.com.paulojof.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends RestException {

	private String responseBodyCode;

	private ErrorResponse responseBody;

	public UnprocessableEntityException(String responseBodyCode) {
		this.responseBodyCode = responseBodyCode;
	}

	public UnprocessableEntityException(ErrorResponse responseBody) {
		this.responseBody = responseBody;
	}

	public UnprocessableEntityException(ExampleClientException exampleClientException) {
		this.responseBodyCode = exampleClientException.getErrorCode();
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
