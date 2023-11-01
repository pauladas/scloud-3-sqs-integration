package br.com.paulojof.service.client;

import org.springframework.stereotype.Service;

import br.com.paulojof.exception.SqsClientException;
import br.com.paulojof.model.JsonParser;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomSqsClient {

    private final SqsTemplate sqsTemplate;

    public void sendToSqsAsString(Object sqsMessage, String sqsUrl) throws SqsClientException {
        log.info("Sending message to SQS queue: {}", sqsUrl);
        try {
            sqsTemplate.send(sqsUrl, JsonParser.objectToStringJson(sqsMessage));
            log.info("Message sent to {} queue.", sqsUrl);
        } catch (Exception e) {
            log.error("Error when sending message to SQS: {} - Message: {}", sqsUrl, e.getMessage(), e);
            throw new SqsClientException(e.getMessage());
        }
    }

    public void sendToSqs(Object sqsMessage, String sqsUrl) throws SqsClientException {
        log.info("Sending message to SQS queue: {}", sqsUrl);
        try {
            sqsTemplate.send(sqsUrl, sqsMessage);
            log.info("Message sent to {} queue.", sqsUrl);
        } catch (Exception e) {
            log.error("Error when sending message to SQS: {} - Message: {}", sqsUrl, e.getMessage(), e);
            throw new SqsClientException(e.getMessage());
        }
    }

}
