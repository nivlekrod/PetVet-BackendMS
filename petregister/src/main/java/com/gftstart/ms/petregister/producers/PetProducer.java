package com.gftstart.ms.petregister.producers;

import com.gftstart.ms.petregister.events.PetCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.appointment-scheduling}") //exchange default: routing key é o mesmo nome da queue(fila)
    private String routingKey;

    public void publishPetCreated(PetCreatedEvent event) {
        rabbitTemplate.convertAndSend("", routingKey, event);
        System.out.println("Evento 'pet_created' publicado: " + event);
    }
}
