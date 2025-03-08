package com.gftstart.ms.appointmentscheduling.controllers;

import com.gftstart.ms.appointmentscheduling.dtos.AppointmentSchedulingDTO;
import com.gftstart.ms.appointmentscheduling.models.AppointmentSchedulingModel;
import com.gftstart.ms.appointmentscheduling.services.impl.AppointmentSchedulingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
public class AppoimentSchedulingController {

    private final AppointmentSchedulingServiceImpl appointmentService;

    @PostMapping("/manual")
    public ResponseEntity<AppointmentSchedulingModel> createManualAppointments(@RequestBody AppointmentSchedulingDTO request) {
        AppointmentSchedulingModel appointment = appointmentService.createManualAppointment(
                request.getPetId(),
                request.getServiceType(),
                request.getAppointmentDate(),
                request.getNotes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @GetMapping("/automatic")
    public ResponseEntity<List<AppointmentSchedulingModel>> getAutomaticAppointments(@RequestParam UUID petId) {
        List<AppointmentSchedulingModel> appointments = appointmentService.getAppointmentsByPetId(petId);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentSchedulingModel> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentSchedulingDTO request) {
        AppointmentSchedulingModel updatedScheduling = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(updatedScheduling);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted!");

    }
}
