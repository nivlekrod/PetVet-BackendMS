package com.gftstart.ms.petregister.models;

import com.gftstart.ms.petregister.enums.Species;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_PET")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID petId;

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
