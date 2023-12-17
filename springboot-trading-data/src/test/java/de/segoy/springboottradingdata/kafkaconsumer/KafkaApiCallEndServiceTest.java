package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.entity.message.BaseMessage;
import de.segoy.springboottradingdata.model.entity.message.ErrorMessage;
import de.segoy.springboottradingdata.model.entity.message.TwsMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KafkaApiCallEndServiceTest {

    @Mock
    Consumer<String, IBKRDataTypeEntity> consumer;
    @Mock
    ConsumerRecord<String, IBKRDataTypeEntity> record1;
    @Mock
    ConsumerRecord<String, IBKRDataTypeEntity> record2;
    @Mock
    ConsumerRecord<String, IBKRDataTypeEntity> record3;
    @Mock
    private KafkaConsumerProvider kafkaConsumerProvider;
    @Mock
    private KafkaConstantsConfig kafkaConstantsConfig;
    @InjectMocks
    KafkaApiCallEndService kafkaApiCallEndService;

    @BeforeEach
    void setup() {
        ConsumerRecords<String, IBKRDataTypeEntity> records = new ConsumerRecords<>(Map.of(new TopicPartition("error",
                        2), List.of(record1, record3), new TopicPartition("message", 1), List.of(record2)));

        when(kafkaConstantsConfig.getERROR_MESSAGE_TOPIC()).thenReturn("error");
        when(kafkaConsumerProvider.createConsumerWithSubscription(List.of("message", "error"))).thenReturn(consumer);
        when(consumer.poll(Duration.ofMillis(50L))).thenReturn(records);
    }
    @Test
    void testWaitWithValidMessages() {

        BaseMessage msg1 = ErrorMessage.builder().messageId(1).message("normal Error Message").build();
        BaseMessage msg2 = TwsMessage.builder().messageId(2).message("normal End Message").build();
        BaseMessage msg3 = ErrorMessage.builder().messageId(3).message("normalWarn Message").build();

        setup();

        when(record1.value()).thenReturn(msg1);
        when(record2.value()).thenReturn(msg2);
        when(record3.value()).thenReturn(msg3);

        kafkaApiCallEndService.waitForApiCallToFinish(2, "message");

        verify(record1,times(1)).value();
        verify(record2,times(1)).value();
        verify(record3,times(1)).value();
    }

    @Test
    void testWaitWithErrorMessage() {

        BaseMessage msg1 = ErrorMessage.builder().messageId(1).message("normal Error Message").build();
        BaseMessage msg2 = TwsMessage.builder().messageId(2).message("normal End Message").build();
        BaseMessage msg3 = ErrorMessage.builder().messageId(3).message("normalWarn Message").build();

        setup();

        when(record1.value()).thenReturn(msg1);
        when(record2.value()).thenReturn(msg2);
        when(record3.value()).thenReturn(msg3);

        boolean threw = true;
        try{
        kafkaApiCallEndService.waitForApiCallToFinish(1,"message");
        threw = false;
        }catch (RuntimeException e){
            assertEquals("Error occured: normal Error Message",e.getMessage());
        }
        assertTrue(threw);
    }
    @Test
    void testWaitWithErrorMessageBeforeNormalFinish() {

        BaseMessage msg1 = ErrorMessage.builder().messageId(1).message("normal Error Message").build();
        BaseMessage msg2 = TwsMessage.builder().messageId(3).message("normal End Message").build();
        BaseMessage msg3 = ErrorMessage.builder().messageId(3).message("normalWarn Message").build();

        setup();

        when(record1.value()).thenReturn(msg1);
        when(record2.value()).thenReturn(msg2);
        when(record3.value()).thenReturn(msg3);

        boolean threw = true;
        try {
            kafkaApiCallEndService.waitForApiCallToFinish(1,"message");
            threw = false;
        } catch (RuntimeException e) {
            assertEquals("Error occured: normal Error Message",e.getMessage());
        }
        assertTrue(threw);
    }



}