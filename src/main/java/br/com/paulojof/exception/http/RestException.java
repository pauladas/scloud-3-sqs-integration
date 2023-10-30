package br.com.paulojof.exception.http;

import br.com.paulojof.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public abstract class RestException extends RuntimeException {

	public abstract HttpStatus getStatus();

	public RestException() {
		super();
	}

	public RestException(String message) {
		super(message);
	}

	public String getResponseBodyCode() {
		return null;
	}

	public ErrorResponse getResponseBody() {
		return null;
	}

}
