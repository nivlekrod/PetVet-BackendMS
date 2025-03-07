package com.gftstart.ms.appointmentscheduling.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gftstart.ms.appointmentscheduling.dtos.PetCreatedEventDTO;
import com.gftstart.ms.appointmentscheduling.models.PetModel;
import com.gftstart.ms.appointmentscheduling.repositories.PetModelRepository;
import com.gftstart.ms.appointmentscheduling.services.AppointmentSchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppointmentSchedulingConsumer {

    private final PetModelRepository petRepository;
    private final AppointmentSchedulingService appointmentService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Evita criar um novo ObjectMapper em cada chamada

    @RabbitListener(queues = "${mq.queues.pet_created}")
    public void receivePetCreated(@Payload PetCreatedEventDTO petCreatedEventDTO) {
        System.out.println("Mensagem recebida do RabbitMQ: " + petCreatedEventDTO);

        Optional<PetModel> optionalPet = petRepository.findById(petCreatedEventDTO.getPetId());

        PetModel petModel;

        if (optionalPet.isPresent()) {
            // Se o Pet já existir, apenas atualize os dados necessários
            petModel = optionalPet.get();
            petModel.setName(petCreatedEventDTO.getName());
            petModel.setSpecies(petCreatedEventDTO.getSpecies());
            petModel.setBreed(petCreatedEventDTO.getBreed());
            petModel.setAge(petCreatedEventDTO.getAge());
            petModel.setWeight(petCreatedEventDTO.getWeight());
            petModel.setTutor(petCreatedEventDTO.getTutor());
            petModel.setEmailTutor(petCreatedEventDTO.getEmailTutor());
            System.out.println("Pet atualizado: " + petModel);
        } else {
            // Se o Pet não existir, crie uma nova entidade
            petModel = new PetModel(
                    petCreatedEventDTO.getPetId(),
                    petCreatedEventDTO.getName(),
                    petCreatedEventDTO.getSpecies(),
                    petCreatedEventDTO.getBreed(),
                    petCreatedEventDTO.getAge(),
                    petCreatedEventDTO.getWeight(),
                    petCreatedEventDTO.getTutor(),
                    petCreatedEventDTO.getEmailTutor()
            );
            System.out.println("Pet criado: " + petModel);
        }

        petRepository.save(petModel);

        appointmentService.generatePetAppointments(petModel);
        System.out.println("Agendamentos gerados com sucesso para o pet: " + petModel.getName());
    }

}

