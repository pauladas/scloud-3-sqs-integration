package br.com.paulojof.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.paulojof.model.ThirdQueueDTO;
import br.com.paulojof.model.ThirdQueueFamilyDTO;
import br.com.paulojof.model.ThirdQueuePersonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdQueueService {

    public void processFirstQueueMessage(ThirdQueueDTO dto) {
        try {
            ThirdQueuePersonDTO person = dto.getPerson();
            List<ThirdQueueFamilyDTO> familyList = dto.getFamily();
            log.info("Message received Person with name {}, age {},car {} and {} family members",
                    person.getName(), person.getAge(), person.getCar(), familyList.size());
        } catch (Exception e) {
            log.error("Error while trying to process first-queue message. {}", e.getMessage(), e);
            throw new RuntimeException("Error processing third queue message...");
        }
    }

}
