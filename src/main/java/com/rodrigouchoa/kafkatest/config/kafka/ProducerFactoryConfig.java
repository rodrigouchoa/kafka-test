package com.rodrigouchoa.kafkatest.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.rodrigouchoa.kafkatest.config.props.ApplicationProps;
import com.rodrigouchoa.kafkatest.domain.Person;

@Configuration
public class ProducerFactoryConfig {
	
	@Autowired
	private ApplicationProps props;
	
	
	@Bean
	public ProducerFactory<Long, Person> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getServer());
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, props.getProducer().getBufferMemory());

        // By default Kafka adds headers when JsonSerializer is used, however, this results in an error message stating
        // "Magic v1 does not support record headers". This can be solved by disabling the headers, as detailed on
        // https://stackoverflow.com/a/50572151/1433614
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, props.getProducer().getAddTypeInfoHeaders());

        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, props.getSecurityProtocol());

        return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
    public KafkaTemplate<Long, Person> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
	
}
