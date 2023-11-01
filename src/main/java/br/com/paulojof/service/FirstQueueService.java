package br.com.paulojof.service;

import org.springframework.stereotype.Service;

import br.com.paulojof.configuration.ApplicationConfig;
import br.com.paulojof.model.FirstQueueDTO;
import br.com.paulojof.model.SecondQueueDTO;
import br.com.paulojof.service.client.CustomSqsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirstQueueService {

    private final CustomSqsClient sqsClient;

    private final ApplicationConfig applicationConfig;

    public void processFirstQueueMessage(FirstQueueDTO dto) {
        try {
            log.info("Message received with name {}, age {},car {} and {} family members",
                    dto.getName(), dto.getAge(), dto.getCar(), dto.getFamily().size());
            SecondQueueDTO secondQueueDTO = SecondQueueDTO.valueOf(dto);
            sqsClient.sendToSqsAsString(secondQueueDTO, applicationConfig.getSqsSecondQueueName());
        } catch (Exception e) {
            log.error("Error while trying to process first-queue message. {}", e.getMessage(), e);
            throw new RuntimeException("Error processing first queue message...");
        }
    }

}
