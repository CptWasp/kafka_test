package containersSteps;


import kafkaReuse.KafkaReuseContainer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import services.SendService;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class SendServiceTest {

    /** Принудительный запуск контейнеров
    @Container
    KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    **/


//    KafkaContainer kafkaContainer = KafkaReuseContainer.reuseContainer("getAndSend");
    KafkaContainer kafkaContainer = KafkaReuseContainer.reuseContainer("send");

    @Test
    public void sendRecordInContainer(){
        kafkaContainer.start();
        String bootstrapServer = kafkaContainer.getBootstrapServers();
//        String topicName = "send-container-topic";
        String topicName = "topic";


        SendService service = new SendService(bootstrapServer, topicName);
        service.sendRecords(List.of("test1", "test2", "test3"));

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-java-test");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer consumer = new KafkaConsumer(properties);
        consumer.subscribe(Arrays.asList(topicName));
        ConsumerRecords records = consumer.poll(Duration.ofMillis(10000));
        consumer.close();



        assertEquals(3, records.count());



    }



}
