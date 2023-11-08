package containersSteps;

import kafkaReuse.KafkaReuseContainer;
import kafkaReuse.ReuseKafkaContainerExtension;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import services.GetService;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
//@ExtendWith(ReuseKafkaContainerExtension.class)
public class GetServiceTest {

    /** Принудительный запуск контейнеров
    @Container
    KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
     **/

//    KafkaContainer kafkaContainer = KafkaReuseContainer.reuseContainer("getAndSend");
    KafkaContainer kafkaContainer = KafkaReuseContainer.reuseContainer("get");


    @Test
    public void getRecordsFromContainer(){
//        kafkaContainer.start();
        String bootstrapServer = kafkaContainer.getBootstrapServers();
//        String topicName = "read-topic";
        String topicName = "topic";

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        KafkaProducer producer = new KafkaProducer<>(properties);
        producer.send(new ProducerRecord<>(topicName, "hello1"));
        producer.close();


        GetService getService = new GetService(bootstrapServer, topicName);
        int recordsSize = getService.getRecordsSize();

        assertEquals(1, recordsSize);
    }

}
