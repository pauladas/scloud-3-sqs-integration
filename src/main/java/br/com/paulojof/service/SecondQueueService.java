package br.com.paulojof.service;

import org.springframework.stereotype.Service;

import br.com.paulojof.configuration.ApplicationConfig;
import br.com.paulojof.model.SecondQueueDTO;
import br.com.paulojof.model.ThirdQueueDTO;
import br.com.paulojof.service.client.CustomSqsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecondQueueService {

    private final CustomSqsClient sqsClient;

    private final ApplicationConfig applicationConfig;

    public void processFirstQueueMessage(SecondQueueDTO dto) {
        try {
            log.info("Message received Person with name {}, age {},car {} and {} family members",
                    dto.getPersonName(), dto.getPersonAge(), dto.getPersonCar(), dto.getPersonsFamily().size());
            ThirdQueueDTO thirdQueueDTO = ThirdQueueDTO.valueOf(dto);
            sqsClient.sendToSqs(thirdQueueDTO, applicationConfig.getSqsThirdQueueName());
            log.info("Sending, now, dto as String...");
            sqsClient.sendToSqsAsString(thirdQueueDTO, applicationConfig.getSqsThirdQueueName());
        } catch (Exception e) {
            log.error("Error while trying to process first-queue message. {}", e.getMessage(), e);
            throw new RuntimeException("Error processing second queue message...");
        }
    }

}
