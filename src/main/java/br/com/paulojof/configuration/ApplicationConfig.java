package br.com.paulojof.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ApplicationConfig {

	@Value("${example-client.url}")
	private String exampleUrl;

}
