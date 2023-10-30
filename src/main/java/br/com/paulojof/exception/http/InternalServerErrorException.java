package br.com.paulojof.exception.http;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class InternalServerErrorException extends RestException {

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
