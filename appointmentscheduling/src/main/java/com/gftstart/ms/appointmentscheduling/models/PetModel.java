package com.gftstart.ms.appointmentscheduling.models;

import com.gftstart.ms.appointmentscheduling.enums.Species;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_PETDATA")
@Data
public class PetModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID petId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Species species;

    private String breed;
    private Integer age;
    private Double weight;
    private String tutor;
    private String emailTutor;
}
