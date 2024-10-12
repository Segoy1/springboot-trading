package de.segoy.springboottradingibkr.client.errorhandling;

import static org.mockito.Mockito.when;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaConsumerProvider;
import de.segoy.springboottradingdata.model.data.kafka.KafkaDataType;
import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//Hard To Test always Throws Errors because of Constructor
@ExtendWith(MockitoExtension.class)
class ApiResponseErrorHandlerTest {

    @Mock
    private ErrorCodeMapper errorCodeMapper;
    @Mock
    private Consumer<String, KafkaDataType> consumer;
    @Mock
    private KafkaConsumerProvider kafkaConsumerProvider;
    @Mock
    private KafkaConstantsConfig kafkaConstantsConfig;
    @InjectMocks
    private ApiResponseErrorHandler apiResponseErrorHandler;


    @BeforeEach
    void setup(){
        ConsumerRecord<String, KafkaDataType> r1 =
                new ConsumerRecord<>("topic",1,1,"1", ErrorMessage.builder().errorCode(1).build());
        ConsumerRecord<String, KafkaDataType> r2 =
                new ConsumerRecord<>("topic",1,2,"2", ErrorMessage.builder().errorCode(2).build());

        TopicPartition part = new TopicPartition("topic",1);
        ConsumerRecords<String, KafkaDataType> records = new ConsumerRecords<>(Map.of(part, List.of(r1,r2)));


        when(kafkaConsumerProvider.createConsumerWithSubscription(List.of("topic"))).thenReturn(consumer);
        when(consumer.poll(Duration.ofMillis(100L))).thenReturn(records);
    }

//    @Test
//    void testIsErrorForIdFalse(){
//        boolean result = apiResponseErrorHandler.isErrorForId(3);
//        assertFalse(result);
//    }

}
