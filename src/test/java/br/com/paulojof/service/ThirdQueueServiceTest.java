package br.com.paulojof.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.paulojof.exception.ThirdQueueException;
import br.com.paulojof.model.ThirdQueueDTO;
import br.com.paulojof.model.ThirdQueueFamilyDTO;
import br.com.paulojof.model.ThirdQueuePersonDTO;

@ExtendWith(MockitoExtension.class)
public class ThirdQueueServiceTest {

    @InjectMocks
    private ThirdQueueService thirdQueueService;

    private ThirdQueueDTO thirdQueueDTO;

    @Test
    public void shouldProcessThirdQueueMessage() throws ThirdQueueException {
        givenThirdQueueDTO();
        whenProcessThirdQueueMessage();
    }

    @Test
    public void shouldNotProcessThirdQueueMessage() {
        givenNullThirdQueueDTO();
        whenProcessThirdQueueMessageThrowsThirdQueueException();
    }

    // GIVEN
    private void givenThirdQueueDTO() {
        thirdQueueDTO = buildThirdQueueDTO();
    }

    private void givenNullThirdQueueDTO() {
        thirdQueueDTO = null;
    }

    // WHEN
    private void whenProcessThirdQueueMessage() throws ThirdQueueException {
        thirdQueueService.processThirdQueueMessage(thirdQueueDTO);
    }

    private void whenProcessThirdQueueMessageThrowsThirdQueueException() {
        assertThrows(ThirdQueueException.class, () -> thirdQueueService.processThirdQueueMessage(thirdQueueDTO));
    }

    // THEN

    // BUILD
    private ThirdQueueDTO buildThirdQueueDTO() {
        ThirdQueuePersonDTO thirdQueuePersonDTO = buildThirdQueuePersonDTO();
        ThirdQueueFamilyDTO thirdQueueFamilyDTO = buildThirdQueueFamilyDTO();
        return new ThirdQueueDTO(thirdQueuePersonDTO, List.of(thirdQueueFamilyDTO));
    }

    private ThirdQueuePersonDTO buildThirdQueuePersonDTO() {
        return ThirdQueuePersonDTO.builder()
                .name(RandomStringUtils.randomAlphanumeric(5, 15))
                .age(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .car(RandomStringUtils.randomAlphanumeric(5, 15))
                .build();
    }

    private ThirdQueueFamilyDTO buildThirdQueueFamilyDTO() {
        return ThirdQueueFamilyDTO.builder()
                .name(RandomStringUtils.randomAlphanumeric(5, 15))
                .age(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .car(RandomStringUtils.randomAlphanumeric(5, 15))
                .nicknames(List.of(RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphanumeric(4, 10)))
                .build();
    }

}
