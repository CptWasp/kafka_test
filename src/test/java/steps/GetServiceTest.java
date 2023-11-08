package steps;

import kafkaReuse.ReuseKafkaContainerExtension;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import services.GetService;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetServiceTest {

    @Test
    public void getRecordSize(){
        String bootstrapServer = "localhost:9092";
        String topicName = "read-topic";

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
