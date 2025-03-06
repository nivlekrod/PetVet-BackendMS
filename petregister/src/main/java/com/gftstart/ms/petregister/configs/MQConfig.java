package com.gftstart.ms.petregister.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.pet_created}")
    private String petCreatedQueue;

    @Bean
    public Queue queuePetCreated(){
        return new Queue(petCreatedQueue, true);
    }

}
