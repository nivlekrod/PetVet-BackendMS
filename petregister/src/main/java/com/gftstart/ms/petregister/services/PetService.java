package com.gftstart.ms.petregister.services;

import com.gftstart.ms.petregister.models.PetModel;

import java.util.List;

public interface PetService {
    PetModel createPet(PetModel pet);

    PetModel getPetById(Long id);

    List<PetModel> getAllPets();

    List<PetModel> searchPetsByBreed(String breed);

    List<PetModel> searchPetsBySpecies(String species);

    List<PetModel> searchPetsBySpeciesAndBreed(String species, String breed);

    PetModel updatePet(Long id, PetModel pet);

    void deletePet(Long id);

    List<String> getAllDogsBreeds();

    List<String> getAllCatsBreeds();

    String getBreedImage(String breed);
}
