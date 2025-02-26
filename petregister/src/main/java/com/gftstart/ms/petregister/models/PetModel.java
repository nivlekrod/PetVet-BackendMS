package com.gftstart.ms.petregister.models;

import com.gftstart.ms.petregister.enums.Species;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long petId;

    private String name;

    @Enumerated(EnumType.STRING)
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
