package br.com.paulojof.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@Profile("test")
@Configuration
public class AwsTestConfig {

    @Bean
    public SqsTemplate sqsTemplate() {
        return Mockito.mock(SqsTemplate.class);
    }

}
