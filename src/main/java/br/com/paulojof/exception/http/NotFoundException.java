package br.com.paulojof.exception.http;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class NotFoundException extends RestException {

	private String responseBodyCode;

	public NotFoundException(String responseBodyCode) {
		this.responseBodyCode = responseBodyCode;
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_FOUND;
	}

	@Override
	public String getResponseBodyCode() {
		return responseBodyCode;
	}

}
