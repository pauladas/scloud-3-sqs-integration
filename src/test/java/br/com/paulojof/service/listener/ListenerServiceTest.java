package br.com.paulojof.service.listener;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.paulojof.exception.FirstQueueException;
import br.com.paulojof.exception.SecondQueueException;
import br.com.paulojof.exception.ThirdQueueException;
import br.com.paulojof.model.FirstQueueDTO;
import br.com.paulojof.model.FirstQueueFamilyDTO;
import br.com.paulojof.model.JsonParser;
import br.com.paulojof.model.SecondQueueDTO;
import br.com.paulojof.model.SecondQueueFamilyDTO;
import br.com.paulojof.model.ThirdQueueDTO;
import br.com.paulojof.model.ThirdQueueFamilyDTO;
import br.com.paulojof.model.ThirdQueuePersonDTO;
import br.com.paulojof.service.FirstQueueService;
import br.com.paulojof.service.SecondQueueService;
import br.com.paulojof.service.ThirdQueueService;
import software.amazon.awssdk.services.sqs.model.Message;

@ExtendWith(MockitoExtension.class)
public class ListenerServiceTest {

    @InjectMocks
    private ListenerService listenerService;

    @Mock
    private FirstQueueService firstQueueService;

    @Mock
    private SecondQueueService secondQueueService;

    @Mock
    private ThirdQueueService thirdQueueService;

    private String firstSqsMessage;

    private Message secondSqsMessage;

    private ThirdQueueDTO thirdSqsMessage;

    @Test
    public void shouldFirstQueueListener() throws FirstQueueException {
        givenValidFirstQueueDTOSqsMessage();
        givenFirstQueueServiceProcessFirstQueueMessageRuns();
        whenCallFirstQueueListener();
        thenExpectFirstQueueServiceProcessFirstQueueMessageCalledOnce();
    }

    @Test
    public void shouldNotFirstQueueListener() throws FirstQueueException {
        givenInvalidFirstQueueDTOSqsMessage();
        whenCallFirstQueueListenerThrowsRuntimeException();
        thenExpectFirstQueueServiceProcessFirstQueueMessageNotCalled();
    }

    @Test
    public void shouldSecondQueueListener() throws SecondQueueException {
        givenValidSecondQueueDTOSqsMessage();
        givenSecondQueueServiceProcessSecondQueueMessageRuns();
        whenCallSecondQueueListener();
        thenExpectFirstQueueServiceProcessSecondQueueMessageCalledOnce();
    }

    @Test
    public void shouldNotSecondQueueListener() throws SecondQueueException {
        givenInvalidSecondQueueDTOSqsMessage();
        whenCallSecondQueueListenerThrowsRuntimeException();
        thenExpectFirstQueueServiceProcessSecondQueueMessageNotCalled();
    }

    @Test
    public void shouldThirdQueueListener() throws ThirdQueueException {
        givenValidThirdQueueDTOSqsMessage();
        givenThirdQueueServiceProcessThirdQueueMessageRuns();
        whenCallThirdQueueListener();
        thenExpectThirdQueueServiceProcessThirdQueueMessageCalledOnce();
    }

    @Test
    public void shouldNotThirdQueueListener() throws ThirdQueueException {
        givenValidThirdQueueDTOSqsMessage();
        givenThirdQueueServiceProcessThirdQueueMessageThrowsThirdQueueException();
        whenCallThirdQueueListenerThrowsRuntimeException();
        thenExpectThirdQueueServiceProcessThirdQueueMessageCalledOnce();
    }

    // GIVEN
    private void givenValidFirstQueueDTOSqsMessage() {
        FirstQueueDTO firstQueueDTO = buildFirstQueueDTO();
        firstSqsMessage = JsonParser.objectToStringJson(firstQueueDTO);
    }

    private void givenInvalidFirstQueueDTOSqsMessage() {
        firstSqsMessage = "example string";
    }

    private void givenFirstQueueServiceProcessFirstQueueMessageRuns() throws FirstQueueException {
        doNothing().when(firstQueueService).processFirstQueueMessage(any(FirstQueueDTO.class));
    }

    private void givenValidSecondQueueDTOSqsMessage() {
        SecondQueueDTO secondQueueDTO = buildSecondQueueDTO();
        String secondQueueDTOAsString = JsonParser.objectToStringJson(secondQueueDTO);
        secondSqsMessage = Message.builder()
                .body(secondQueueDTOAsString)
                .build();
    }

    private void givenInvalidSecondQueueDTOSqsMessage() {
        String secondQueueDTOAsString = "example string";
        secondSqsMessage = Message.builder()
                .body(secondQueueDTOAsString)
                .build();
    }

    private void givenSecondQueueServiceProcessSecondQueueMessageRuns() throws SecondQueueException {
        doNothing().when(secondQueueService).processSecondQueueMessage(any(SecondQueueDTO.class));
    }

    private void givenValidThirdQueueDTOSqsMessage() {
        thirdSqsMessage = buildThirdQueueDTO();
    }

    private void givenThirdQueueServiceProcessThirdQueueMessageRuns() throws ThirdQueueException {
        doNothing().when(thirdQueueService).processThirdQueueMessage(any(ThirdQueueDTO.class));
    }

    private void givenThirdQueueServiceProcessThirdQueueMessageThrowsThirdQueueException() throws ThirdQueueException {
        doThrow(ThirdQueueException.class).when(thirdQueueService).processThirdQueueMessage(any(ThirdQueueDTO.class));
    }

    // WHEN
    private void whenCallFirstQueueListener() {
        listenerService.firstQueueListener(firstSqsMessage);
    }

    private void whenCallSecondQueueListener() {
        listenerService.secondQueueListener(secondSqsMessage);
    }

    private void whenCallThirdQueueListener() {
        listenerService.thirdQueueListener(thirdSqsMessage);
    }

    private void whenCallFirstQueueListenerThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> listenerService.firstQueueListener(firstSqsMessage));
    }

    private void whenCallSecondQueueListenerThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> listenerService.secondQueueListener(secondSqsMessage));
    }

    private void whenCallThirdQueueListenerThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> listenerService.thirdQueueListener(thirdSqsMessage));
    }

    // THEN
    private void thenExpectFirstQueueServiceProcessFirstQueueMessageCalledOnce() throws FirstQueueException {
        verify(firstQueueService).processFirstQueueMessage(any(FirstQueueDTO.class));
    }

    private void thenExpectFirstQueueServiceProcessFirstQueueMessageNotCalled() throws FirstQueueException {
        verify(firstQueueService, never()).processFirstQueueMessage(any(FirstQueueDTO.class));
    }

    private void thenExpectFirstQueueServiceProcessSecondQueueMessageCalledOnce() throws SecondQueueException {
        verify(secondQueueService).processSecondQueueMessage(any(SecondQueueDTO.class));
    }

    private void thenExpectFirstQueueServiceProcessSecondQueueMessageNotCalled() throws SecondQueueException {
        verify(secondQueueService, never()).processSecondQueueMessage(any(SecondQueueDTO.class));
    }

    private void thenExpectThirdQueueServiceProcessThirdQueueMessageCalledOnce() throws ThirdQueueException {
        verify(thirdQueueService).processThirdQueueMessage(any(ThirdQueueDTO.class));
    }

    // BUILD
    private FirstQueueDTO buildFirstQueueDTO() {
        FirstQueueFamilyDTO firstQueueFamilyDTO = buildFirstQueueFamilyDTO();
        return FirstQueueDTO.builder()
                .name(RandomStringUtils.randomAlphanumeric(5, 15))
                .age(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .car(RandomStringUtils.randomAlphanumeric(5, 15))
                .family(List.of(firstQueueFamilyDTO))
                .build();
    }

    private FirstQueueFamilyDTO buildFirstQueueFamilyDTO() {
        return FirstQueueFamilyDTO.builder()
                .name(RandomStringUtils.randomAlphanumeric(5, 15))
                .age(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .car(RandomStringUtils.randomAlphanumeric(5, 15))
                .nicknames(List.of(RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphanumeric(4, 10)))
                .build();
    }

    private SecondQueueDTO buildSecondQueueDTO() {
        SecondQueueFamilyDTO secondQueueFamilyDTO = buildSecondQueueFamilyDTO();
        return SecondQueueDTO.builder()
                .personName(RandomStringUtils.randomAlphanumeric(5, 15))
                .personAge(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .personCar(RandomStringUtils.randomAlphanumeric(5, 15))
                .personsFamily(List.of(secondQueueFamilyDTO))
                .build();
    }

    private SecondQueueFamilyDTO buildSecondQueueFamilyDTO() {
        return SecondQueueFamilyDTO.builder()
                .familyPersonName(RandomStringUtils.randomAlphanumeric(5, 15))
                .familyPersonAge(Integer.parseInt(RandomStringUtils.randomNumeric(2, 3)))
                .familyPersonCar(RandomStringUtils.randomAlphanumeric(5, 15))
                .familyPersonNicknames(List.of(RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphanumeric(4, 10)))
                .build();
    }

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
