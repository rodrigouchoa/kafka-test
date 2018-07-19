package com.rodrigouchoa.kafkatest.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * POJO to hold the configs from the application.yml config file.
 * 
 * @author rodrigo.uchoa at gmail.com
 *
 */
@Component
@ConfigurationProperties(prefix = "kafka")
public class ApplicationProps {
    private String server;
    private String securityProtocol;
    private String topic;
    private ProducerProps producer;
    private ConsumerProps consumer;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ProducerProps getProducer() {
        return producer;
    }

    public void setProducer(ProducerProps producer) {
        this.producer = producer;
    }

    public ConsumerProps getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerProps consumer) {
        this.consumer = consumer;
    }

}
