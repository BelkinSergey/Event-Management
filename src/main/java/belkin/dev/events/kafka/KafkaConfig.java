package belkin.dev.events.kafka;

import belkin.dev.events.kafka.dto.NotificationDto;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<Integer, NotificationDto> kafkaTemplate(
            KafkaProperties kafkaProperties
    ) {

        var props = kafkaProperties.buildProducerProperties(
        );

        ProducerFactory<Integer, NotificationDto> producerFactory = new
                DefaultKafkaProducerFactory<>(props);

        return new KafkaTemplate<>(producerFactory);
    }
}
