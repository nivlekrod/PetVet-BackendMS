package com.gftstart.ms.appointmentscheduling.services;

import com.gftstart.ms.appointmentscheduling.dtos.PetDataDTO;
import com.gftstart.ms.appointmentscheduling.enums.ServiceType;
import com.gftstart.ms.appointmentscheduling.models.AppointmentSchedulingModel;
import com.gftstart.ms.appointmentscheduling.models.PetModel;
import com.gftstart.ms.appointmentscheduling.repositories.AppointmentSchedulingRepository;
import com.gftstart.ms.appointmentscheduling.repositories.PetModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AppointmentSchedulingService {

    private final AppointmentSchedulingRepository appointmentRepository;
    private final PetModelRepository petRepository;

    public PetModel convertDTO2Entity(PetDataDTO petDataDTO) {
        PetModel pet = new PetModel();
        pet.setPetId(petDataDTO.getPetId());
        pet.setName(petDataDTO.getName());
        pet.setSpecies(petDataDTO.getSpecies());
        pet.setBreed(petDataDTO.getBreed());
        pet.setTutor(petDataDTO.getTutor());
        pet.setEmailTutor(petDataDTO.getEmailTutor());
        pet.setAge(petDataDTO.getAge());
        pet.setWeight(petDataDTO.getWeight());

        return pet;
    }

    public LocalDate generateRandomAppointmentDate(int minDaysAhead, int maxDaysAhead) {
        Random random = new Random();
        int randomDays = random.nextInt(maxDaysAhead - minDaysAhead + 1) + minDaysAhead;
        return LocalDate.now().plusDays(randomDays);
    }

    public LocalDate calculateRandomDateForService(ServiceType serviceType) {
        switch (serviceType) {
            case FIRST_FREE_BATH:
                return generateRandomAppointmentDate(1, 7); // Banho grátis entre 1 e 7 dias
            case INITIAL_VACCINATION:
                return generateRandomAppointmentDate(5, 15); // Vacinação inicial entre 5 e 15 dias
            case INITIAL_CHECKUP:
                return generateRandomAppointmentDate(10, 30); // Check-up inicial entre 10 e 30 dias
            case GROOMING:
                return generateRandomAppointmentDate(7, 14); // Banho e tosa entre 7 e 14 dias
            case VACCINATION:
                return generateRandomAppointmentDate(30, 60); // Vacinações regulares entre 30 e 60 dias
            case VETERINARY_CONSULTATION:
                return generateRandomAppointmentDate(14, 30); // Consultas veterinárias entre 14 e 30 dias
            case MEDICATION_ADMINISTRATION:
                return generateRandomAppointmentDate(1, 5); // Administração de medicamentos entre 1 e 5 dias
            default:
                return generateRandomAppointmentDate(1, 30); // Padrão de 1 a 30 dias
        }
    }

    public AppointmentSchedulingModel createAutomaticAppointment(PetModel pet, ServiceType serviceType, String notes) {
        LocalDate appointmentDate = calculateRandomDateForService(serviceType);

        AppointmentSchedulingModel appointment = new AppointmentSchedulingModel();
        appointment.setPetModel(pet);
        appointment.setServiceType(serviceType);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setNotes(notes);

        return appointment;
    }

    @Transactional
    public void generatePetAppointments(PetModel pet) {
        List<AppointmentSchedulingModel> appointments = new ArrayList<>();
//        AppointmentSchedulingModel appointments = new AppointmentSchedulingModel();

        boolean isFirstAppointment = !appointmentRepository.existsByPetModel_PetId(pet.getPetId());

        if (pet.getAge() < 6) {
            appointments.add(createAutomaticAppointment(pet, ServiceType.INITIAL_VACCINATION, "Initial Vaccination"));
//            appointments = createAutomaticAppointment(pet, ServiceType.INITIAL_VACCINATION, "Initial Vaccination");
        } else {
            appointments.add(createAutomaticAppointment(pet, ServiceType.VACCINATION, "Regular Vaccination"));
//            appointments = createAutomaticAppointment(pet, ServiceType.VACCINATION, "Regular Vaccination");
        }

        if (pet.getAge() >= 6) {
            appointments.add(createAutomaticAppointment(pet, ServiceType.INITIAL_CHECKUP, "Initial Health Check-up"));
//            appointments = createAutomaticAppointment(pet, ServiceType.INITIAL_CHECKUP, "Initial Health Check-up");
        } else {
            appointments.add(createAutomaticAppointment(pet, ServiceType.VETERINARY_CONSULTATION, "Routine Veterinary Consultation"));
//            appointments = createAutomaticAppointment(pet, ServiceType.VETERINARY_CONSULTATION, "Routine Veterinary Consultation");
        }

        if (isFirstAppointment) {
            appointments.add(createAutomaticAppointment(pet, ServiceType.FIRST_FREE_BATH, "Free Bath"));
//            appointments = createAutomaticAppointment(pet, ServiceType.FIRST_FREE_BATH, "Free Bath");
        }

        appointmentRepository.saveAll(appointments);
        System.out.println("Agendamentos gerados para o pet: " + appointments);
    }
}
