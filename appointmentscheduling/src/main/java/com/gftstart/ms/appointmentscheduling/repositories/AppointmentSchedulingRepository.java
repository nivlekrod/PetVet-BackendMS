package com.gftstart.ms.appointmentscheduling.repositories;

import com.gftstart.ms.appointmentscheduling.models.AppointmentSchedulingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentSchedulingRepository extends JpaRepository<AppointmentSchedulingModel, UUID> {

    List<AppointmentSchedulingModel> findByPetModel_PetId(UUID petId);

    boolean existsByPetModel_PetId(UUID petId);

}
