package com.gftstart.ms.petregister.events;

import com.gftstart.ms.petregister.enums.Species;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetCreatedEvent {
    private UUID petId;
    private String name;
    private Species species;
    private String tutor;
    private String emailTutor;
    private String breed;
    private Integer age;
    private Double weight;
    private String color;
    private String description;
    private String referenceImage;
}
