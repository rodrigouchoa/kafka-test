package com.rodrigouchoa.kafkatest.config.props;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//silly test to ensure the wiring of the yaml file is ok
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplicationPropsTest {

    @Autowired
    private ApplicationProps props;

    @Test
    public void shouldReadProperty() {
        assertEquals("localhost:9093", props.getServer());
        assertEquals(false, props.getProducer().getAddTypeInfoHeaders());
    }
}
