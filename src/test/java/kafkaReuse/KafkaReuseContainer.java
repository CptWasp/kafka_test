package kafkaReuse;


import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * для переиспользования контейнеров
 */
public class KafkaReuseContainer {
    private static Map<String, KafkaContainer> containers = new HashMap<>();


    public static KafkaContainer reuseContainer(String name){
        if(containers.containsKey(name) && Objects.nonNull(containers.get(name))){
            return containers.get(name);
        }else {
            KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
            kafkaContainer.start();
            containers.put(name, kafkaContainer);
            return kafkaContainer;
        }
    }

}
