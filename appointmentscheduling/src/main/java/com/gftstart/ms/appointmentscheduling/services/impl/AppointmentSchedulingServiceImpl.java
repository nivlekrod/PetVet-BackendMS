package com.gftstart.ms.appointmentscheduling.services.impl;

import com.gftstart.ms.appointmentscheduling.clients.PetRegisterResourceClient;
import com.gftstart.ms.appointmentscheduling.dtos.AppointmentSchedulingDTO;
import com.gftstart.ms.appointmentscheduling.dtos.PetDataDTO;
import com.gftstart.ms.appointmentscheduling.enums.ServiceType;
import com.gftstart.ms.appointmentscheduling.models.AppointmentSchedulingModel;
import com.gftstart.ms.appointmentscheduling.models.PetModel;
import com.gftstart.ms.appointmentscheduling.repositories.AppointmentSchedulingRepository;
import com.gftstart.ms.appointmentscheduling.repositories.PetModelRepository;
import com.gftstart.ms.appointmentscheduling.services.AppointmentSchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentSchedulingServiceImpl implements AppointmentSchedulingService {

    private final AppointmentSchedulingRepository appointmentRepository;
    private final PetModelRepository petRepository;
    private final PetRegisterResourceClient petRegisterResourceClient;

    // Gera uma data aleatória dentro do intervalo especificado
    public LocalDate generateRandomAppointmentDate(int minDaysAhead, int maxDaysAhead) {
        Random random = new Random();
        int randomDays = random.nextInt(maxDaysAhead - minDaysAhead + 1) + minDaysAhead;
        return LocalDate.now().plusDays(randomDays);
    }

    // Calcula a data de acordo com o tipo de serviço
    public LocalDate calculateRandomDateForService(ServiceType serviceType) {
        return switch (serviceType) {
            case FIRST_FREE_BATH -> generateRandomAppointmentDate(1, 7);
            case INITIAL_VACCINATION -> generateRandomAppointmentDate(5, 15);
            case INITIAL_CHECKUP -> generateRandomAppointmentDate(10, 30);
            case GROOMING -> generateRandomAppointmentDate(7, 14);
            case VACCINATION -> generateRandomAppointmentDate(30, 60);
            case VETERINARY_CONSULTATION -> generateRandomAppointmentDate(14, 30);
            case MEDICATION_ADMINISTRATION -> generateRandomAppointmentDate(1, 5);
            default -> generateRandomAppointmentDate(1, 30);
        };
    }

    // Cria um agendamento automático para o pet baseado no tipo de serviço e uma data aleatória calculada
    public AppointmentSchedulingModel createAutomaticAppointment(PetModel pet, ServiceType serviceType, String notes) {
        LocalDate appointmentDate = calculateRandomDateForService(serviceType);

        return AppointmentSchedulingModel.builder()
                .petModel(pet)
                .serviceType(serviceType)
                .appointmentDate(appointmentDate)
                .notes(notes)
                .build();
    }

    // Gera os agendamentos automáticos para o pet com base na lógica fornecida
    public void generatePetAppointments(PetModel pet) {
        List<AppointmentSchedulingModel> appointments = new ArrayList<>();

        boolean isFirstAppointment = !appointmentRepository.existsByPetModel_PetId(pet.getPetId());

        // Lógica para vacinação com base na idade
        if (pet.getAge() < 6) {
            appointments.add(createAutomaticAppointment(pet, ServiceType.INITIAL_VACCINATION, "Initial Vaccination"));
        } else {
            appointments.add(createAutomaticAppointment(pet, ServiceType.VACCINATION, "Regular Vaccination"));
        }

        // Lógica para consultas veterinárias
        if (pet.getAge() >= 6) {
            appointments.add(createAutomaticAppointment(pet, ServiceType.INITIAL_CHECKUP, "Initial Health Check-up"));
        } else {
            appointments.add(createAutomaticAppointment(pet, ServiceType.VETERINARY_CONSULTATION, "Routine Veterinary Consultation"));
        }

        // Agendamento de banho grátis para o primeiro agendamento
        if (isFirstAppointment) {
            appointments.add(createAutomaticAppointment(pet, ServiceType.FIRST_FREE_BATH, "Free Bath"));
        }

        appointmentRepository.saveAll(appointments);

        System.out.println("Agendamentos gerados para o pet: " + appointments);
    }

    public AppointmentSchedulingModel createManualAppointment(UUID petId, ServiceType serviceType, LocalDate appointmentDate, String notes) {
        ResponseEntity<PetDataDTO> petData = petRegisterResourceClient.getPetById(petId);

        if (petData != null) {
            // Mapeando os dados do pet para o modelo PetModel
            PetModel petModel = PetModel.builder()
                    .petId(petData.getBody().getPetId())
                    .name(petData.getBody().getName())
                    .species(petData.getBody().getSpecies())
                    .breed(petData.getBody().getBreed())
                    .age(petData.getBody().getAge())
                    .weight(petData.getBody().getWeight())
                    .tutor(petData.getBody().getTutor())
                    .emailTutor(petData.getBody().getEmailTutor())
                    .build();

            // Criar um novo agendamento
            AppointmentSchedulingModel appointment = AppointmentSchedulingModel.builder()
                    .petModel(petModel)
                    .serviceType(serviceType)
                    .appointmentDate(appointmentDate)
                    .notes(notes)
                    .build();

            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Não foi possível obter os dados do pet.");
        }
    }

    public List<AppointmentSchedulingModel> getAppointmentsByPetId(UUID petId) {
        return appointmentRepository.findAllByPetModel_PetId(petId);
    }

    public AppointmentSchedulingModel updateAppointment(UUID id, AppointmentSchedulingDTO request) {
        Optional<AppointmentSchedulingModel> optionalAppointment = appointmentRepository.findById(id);

        if (optionalAppointment.isPresent()) {
            AppointmentSchedulingModel appointment = optionalAppointment.get();
            appointment.setServiceType(request.getServiceType());
            appointment.setAppointmentDate(request.getAppointmentDate());
            appointment.setNotes(request.getNotes());

            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Agendamento não encontrado.");
        }
    }

    public void deleteAppointment(UUID id) {
        try {
            if (!appointmentRepository.existsById(id)) {
                throw new NoSuchElementException("Agendamento não encontrado.");
            }

            appointmentRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<AppointmentSchedulingModel> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
