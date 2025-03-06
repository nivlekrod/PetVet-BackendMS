package com.gftstart.ms.appointmentscheduling.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gftstart.ms.appointmentscheduling.dtos.PetDataDTO;
import com.gftstart.ms.appointmentscheduling.models.PetModel;
import com.gftstart.ms.appointmentscheduling.repositories.PetModelRepository;
import com.gftstart.ms.appointmentscheduling.services.AppointmentSchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AppointmentSchedulingConsumer {

    private final PetModelRepository petRepository;
    private final AppointmentSchedulingService appointmentService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Evita criar um novo ObjectMapper em cada chamada

    @RabbitListener(queues = "${mq.queues.pet_created}")
    @Transactional
    public void receivePetCreated(@Payload String payload) {
        try {
            System.out.println("Mensagem recebida do RabbitMQ: " + payload);

            PetDataDTO petDataDTO = objectMapper.readValue(payload, PetDataDTO.class);

            PetModel pet;
            pet = appointmentService.convertDTO2Entity(petDataDTO);
            System.out.println(pet);
            petRepository.save(pet);
            appointmentService.generatePetAppointments(pet);
            System.out.println("Agendamentos gerados com sucesso para o pet: " + pet.getName());

        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e.getMessage(), e);

        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            throw new AmqpRejectAndDontRequeueException("Erro ao processar a mensagem.", e);
        }
    }

}

