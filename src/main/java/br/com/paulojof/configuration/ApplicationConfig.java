package br.com.paulojof.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class ApplicationConfig {

    @Value("${cloud.aws.sqs.first-queue}")
    private String sqsFirstQueueName;

    @Value("${cloud.aws.sqs.second-queue}")
    private String sqsSecondQueueName;

    @Value("${cloud.aws.sqs.third-queue}")
    private String sqsThirdQueueName;

}
