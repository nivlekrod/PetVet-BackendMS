package com.gftstart.ms.petregister.controllers;

import com.gftstart.ms.petregister.enums.Species;
import com.gftstart.ms.petregister.models.PetModel;
import com.gftstart.ms.petregister.services.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/pets")
public class PetController {

    @Autowired
    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetModel> createPet(@RequestBody PetModel pet) {
        PetModel createdPet = petService.createPet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @GetMapping("{id}")
    public ResponseEntity<PetModel> getPetById(@PathVariable("id") UUID id) {
        PetModel pet = petService.getPetById(id);
        return ResponseEntity.status(HttpStatus.OK).body(pet);
    }

    @GetMapping
    public ResponseEntity<List<PetModel>> getAllPets() {
        List<PetModel> pets = petService.getAllPets();
        return ResponseEntity.status(HttpStatus.OK).body(pets);
    }

    /// TENTAR FAZER O ENUM IGNORAR CASE
    /// corrigir url ?
    @GetMapping("filter")
    public ResponseEntity<List<PetModel>> searchPetsBySpeciesOrBreed(@RequestParam(value = "species", required = false) Species species,
                                                                     @RequestParam(value = "breed", required = false) String breed) {
        List<PetModel> pets;

        if (species != null && breed != null) {
            pets = petService.searchPetsBySpeciesAndBreed(species, breed);
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } else if (breed != null) {
            pets = petService.searchPetsByBreed(breed);
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } else if (species != null) {
            pets = petService.searchPetsBySpecies(species);
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } else {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<PetModel> updatePet(@PathVariable("id") UUID id, @RequestBody PetModel pet) {
        PetModel updatedPet = petService.updatePet(id, pet);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPet);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePet(@PathVariable("id") UUID id) {
        petService.deletePet(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted!");
    }
}