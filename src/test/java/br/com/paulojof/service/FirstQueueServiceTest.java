package br.com.paulojof.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.paulojof.configuration.ApplicationConfig;
import br.com.paulojof.exception.SqsClientException;
import br.com.paulojof.model.FirstQueueDTO;
import br.com.paulojof.model.FirstQueueFamilyDTO;
import br.com.paulojof.service.client.CustomSqsClient;

@ExtendWith(MockitoExtension.class)
public class FirstQueueServiceTest {

    @InjectMocks
    private FirstQueueService firstQueueService;

    @Mock
    private CustomSqsClient sqsClient;

    @Mock
    private ApplicationConfig applicationConfig;

    private FirstQueueDTO firstQueueDTO;

    @Test
    public void shouldProcessFirstQueueMessage() throws Exception {
        givenFirstQueueDTO();
        givenApplicationConfigGetSqsSecondQueueName();
        givenSqsClientSendToSqsAsStringRuns();
        whenProcessFirstQueueMessage();
        thenExpectApplicationConfigGetSqsSecondQueueNameCalledOnce();
        thenExpectSqsClientSendToSqsAsStringCalledOnce();
    }

    @Test
    public void shouldNotProcessFirstQueueMessageWithSqsClientThrowingSqsClientException() throws Exception {
        givenFirstQueueDTO();
        givenApplicationConfigGetSqsSecondQueueName();
        givenSqsClientSendToSqsAsStringThrowsSqsClientException();
        whenProcessFirstQueueMessageThrowsRunException();
        thenExpectApplicationConfigGetSqsSecondQueueNameCalledOnce();
        thenExpectSqsClientSendToSqsAsStringCalledOnce();
    }

    // GIVEN
    private void givenFirstQueueDTO() {
        firstQueueDTO = buildFirstQueueDTO();
    }

    private void givenApplicationConfigGetSqsSecondQueueName() {
        doReturn("second-sqs").when(applicationConfig).getSqsSecondQueueName();
    }

    private void givenSqsClientSendToSqsAsStringRuns() throws SqsClientException {
        doNothing().when(sqsClient).sendToSqsAsString(any(), anyString());
    }

    private void givenSqsClientSendToSqsAsStringThrowsSqsClientException() throws SqsClientException {
        doThrow(SqsClientException.class).when(sqsClient).sendToSqsAsString(any(), anyString());
    }

    // WHEN
    private void whenProcessFirstQueueMessage() {
        firstQueueService.processFirstQueueMessage(firstQueueDTO);
    }

    private void whenProcessFirstQueueMessageThrowsRunException() {
        assertThrows(RuntimeException.class, () -> firstQueueService.processFirstQueueMessage(firstQueueDTO));
    }

    // THEN
    private void thenExpectApplicationConfigGetSqsSecondQueueNameCalledOnce() {
        verify(applicationConfig).getSqsSecondQueueName();
    }

    private void thenExpectSqsClientSendToSqsAsStringCalledOnce() throws SqsClientException {
        verify(sqsClient).sendToSqsAsString(any(), anyString());
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

}
