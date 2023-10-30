package br.com.paulojof.exception.http;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RestException {

	private String responseBodyCode;

	public BadRequestException(String responseBodyCode) {
		this.responseBodyCode = responseBodyCode;
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getResponseBodyCode() {
		return responseBodyCode;
	}

}
