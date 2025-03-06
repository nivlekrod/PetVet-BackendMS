package com.gftstart.ms.petregister.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gftstart.ms.petregister.events.PetCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.pet_created}")
    private final Queue petCreatedQueue;

    public void publishPetCreated(PetCreatedEvent event) throws JsonProcessingException {
        var json = convertIntoJson(event);
        System.out.println("Enviando mensagem para a fila: " + petCreatedQueue.getName() + " | Conteúdo: " + json);
        rabbitTemplate.convertAndSend(petCreatedQueue.getName(), json);
    }

    private String convertIntoJson(PetCreatedEvent event) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(event);
        return json;
    }
}
