package com.gftstart.ms.petregister.repositories;

import com.gftstart.ms.petregister.models.PetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetModel, Long> {
    List<PetModel> findByBreedIgnoreCase(String breed);

    List<PetModel> findBySpecies(String species);

    /// CORRIGIR ///
    List<PetModel> findBySpeciesAndBreed(String species, String breed);
}
