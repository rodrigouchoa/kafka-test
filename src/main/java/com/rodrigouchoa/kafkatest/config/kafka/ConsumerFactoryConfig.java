package com.rodrigouchoa.kafkatest.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.rodrigouchoa.kafkatest.config.props.ApplicationProps;
import com.rodrigouchoa.kafkatest.domain.Person;

@Configuration
@EnableKafka
public class ConsumerFactoryConfig {
	
    @Autowired
    private ApplicationProps props;
	
	
    @Bean
    public Map<String, Object> consumerProps() {
        Map<String, Object> configProps = new HashMap<String, Object>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getServer());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, props.getConsumer().getGroupId());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, props.getConsumer().getAutoOffsetReset());
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, props.getSecurityProtocol());
        return configProps;
    }
	
    @Bean
	public ConsumerFactory<Long, Person> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerProps(), new LongDeserializer(), new JsonDeserializer<>(Person.class));
	}
	
    //this Bean will be used by the @KafkaListener annotation on the consumer
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Person> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, Person> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setBatchListener(true); 
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3); //we're assuming 3 partitions
        return factory;
    }
}
