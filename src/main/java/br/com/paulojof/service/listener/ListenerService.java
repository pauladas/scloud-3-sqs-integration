package br.com.paulojof.service.listener;

import org.springframework.stereotype.Service;

import br.com.paulojof.model.FirstQueueDTO;
import br.com.paulojof.model.JsonParser;
import br.com.paulojof.model.SecondQueueDTO;
import br.com.paulojof.model.ThirdQueueDTO;
import br.com.paulojof.service.FirstQueueService;
import br.com.paulojof.service.SecondQueueService;
import br.com.paulojof.service.ThirdQueueService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.model.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListenerService {

    private final FirstQueueService firstQueueService;

    private final SecondQueueService secondQueueService;

    private final ThirdQueueService thirdQueueService;

    /**
     * Listener para processamento de mensagem mapeada como String e envio à próxima fila
     *
     * @param sqsMessage
     *            Mensagem vinda da fila SQS, mapeada como String
     */
    @SqsListener("${cloud.aws.sqs.first-queue}")
    public void firstQueueListener(String sqsMessage) {
        log.info("First SQS message received as String! {}", sqsMessage);
        try {
            FirstQueueDTO dto = JsonParser.stringJsonToObject(sqsMessage, FirstQueueDTO.class);
            firstQueueService.processFirstQueueMessage(dto);
        } catch (Exception e) {
            log.error("Error while trying to process first-queue message. {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Listener para processamento da mensagem mapeada como Message (possibilitando recuperar mais informações da mensagem) e envio à próxima fila, de
     * 3 maneiras diferentes
     * 
     * @param message
     *            Mensagem vinda da fila SQS, mapeada como Message (da Amazon SDK)
     */
    @SqsListener("${cloud.aws.sqs.second-queue}")
    public void secondQueueListener(Message message) {
        log.info("Second SQS message received as Message Object! {}", message);
        try {
            log.info("MessageId: {} | Attribute as strings {}", message.messageId(), message.attributesAsStrings());
            SecondQueueDTO dto = JsonParser.stringJsonToObject(message.body(), SecondQueueDTO.class);
            secondQueueService.processSecondQueueMessage(dto);
        } catch (Exception e) {
            log.error("Error while trying to process first-queue message. {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Listener para processamento da mensagem mapeada como o DTO, mostrando como o mesmo pode ser tratado (vide erros)
     * 
     * @param thirdQueueDTO
     *            Mensagem vinda da 3 fila SQS, mapeado diretamente como DTO (e seus impactos)
     */
    @SqsListener("${cloud.aws.sqs.third-queue}")
    public void thirdQueueListener(ThirdQueueDTO thirdQueueDTO) {
        log.info("Third SQS message received as ThirdQueueDTO! {}", thirdQueueDTO);
        try {
            thirdQueueService.processThirdQueueMessage(thirdQueueDTO);
        } catch (Exception e) {
            log.error("Error while trying to process first-queue message. {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
