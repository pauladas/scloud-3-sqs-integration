package br.com.paulojof.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import br.com.paulojof.model.SecondQueueDTO;
import br.com.paulojof.model.SecondQueueFamilyDTO;
import br.com.paulojof.service.client.CustomSqsClient;

@ExtendWith(MockitoExtension.class)
public class SecondQueueServiceTest {

    @InjectMocks
    private SecondQueueService secondQueueService;

    @Mock
    private CustomSqsClient sqsClient;

    @Mock
    private ApplicationConfig applicationConfig;

    private SecondQueueDTO secondQueueDTO;

    @Test
    public void shouldProcessSecondQueueMessage() throws Exception {
        givenSecondQueueDTO();
        givenApplicationConfigGetSqsThirdQueueName();
        givenSqsClientSendToSqsRuns();
        givenSqsClientSendToSqsAsStringRuns();
        whenProcessSecondQueueMessage();
        thenExpectApplicationConfigGetSqsThirdQueueNameCalledTrice();
        thenExpectSqsClientSendToSqsAsStringCalledOnce();
        thenExpectSqsClientSendToSqsCalledTwice();
    }

    @Test
    public void shouldNotProcessSecondQueueMessageWithSqsClientThrowingSqsClientException() throws Exception {
        givenSecondQueueDTO();
        givenApplicationConfigGetSqsThirdQueueName();
        givenSqsClientSendToSqsRunsThrowsSqsClientException();
        whenProcessSecondQueueMessageThrowsRunException();
        thenExpectSqsClientSendToSqsCalledCalledOnce();
        thenExpectSqsClientSendToSqsAsStringNotCalled();
        thenExpectApplicationConfigGetSqsThirdQueueNameCalledOnce();
    }

    // GIVEN
    private void givenSecondQueueDTO() {
        secondQueueDTO = buildSecondQueueDTO();
    }

    private void givenApplicationConfigGetSqsThirdQueueName() {
        doReturn("second-sqs").when(applicationConfig).getSqsThirdQueueName();
    }

    private void givenSqsClientSendToSqsRuns() throws SqsClientException {
        doNothing().when(sqsClient).sendToSqs(any(), anyString());
    }

    private void givenSqsClientSendToSqsAsStringRuns() throws SqsClientException {
        doNothing().when(sqsClient).sendToSqsAsString(any(), anyString());
    }

    private void givenSqsClientSendToSqsRunsThrowsSqsClientException() throws SqsClientException {
        doThrow(SqsClientException.class).when(sqsClient).sendToSqs(any(), anyString());
    }

    // WHEN
    private void whenProcessSecondQueueMessage() {
        secondQueueService.processSecondQueueMessage(secondQueueDTO);
    }

    private void whenProcessSecondQueueMessageThrowsRunException() {
        assertThrows(RuntimeException.class, () -> secondQueueService.processSecondQueueMessage(secondQueueDTO));
    }

    // THEN
    private void thenExpectApplicationConfigGetSqsThirdQueueNameCalledTrice() {
        verify(applicationConfig, times(3)).getSqsThirdQueueName();
    }

    private void thenExpectSqsClientSendToSqsCalledTwice() throws SqsClientException {
        verify(sqsClient, times(2)).sendToSqs(any(), anyString());
    }

    private void thenExpectSqsClientSendToSqsAsStringCalledOnce() throws SqsClientException {
        verify(sqsClient).sendToSqsAsString(any(), anyString());
    }

    private void thenExpectSqsClientSendToSqsCalledCalledOnce() throws SqsClientException {
        verify(sqsClient).sendToSqs(any(), anyString());
    }

    private void thenExpectSqsClientSendToSqsAsStringNotCalled() throws SqsClientException {
        verify(sqsClient, never()).sendToSqsAsString(any(), anyString());
    }

    private void thenExpectApplicationConfigGetSqsThirdQueueNameCalledOnce() {
        verify(applicationConfig).getSqsThirdQueueName();
    }

    // BUILD
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

}
