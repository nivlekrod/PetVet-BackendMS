package com.gftstart.ms.petregister.services.impl;

import com.gftstart.ms.petregister.models.PetModel;
import com.gftstart.ms.petregister.repositories.PetRepository;
import com.gftstart.ms.petregister.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    private static final String CAT_API_URL = "https://api.thecatapi.com/v1";
    private static final String DOG_API_URL = "https://api.thedogapi.com/v1";

    @Value("${apikey}")
    private String apiKey;

    @Override
    @Transactional
    public PetModel createPet(PetModel pet) {
        try {
//            String imgUrl = getBreedImage(pet.getBreed());
//            pet.setReferenceImage(imgUrl);

            return petRepository.save(pet);
        } catch (Exception e) { /// ADICIONAR OUTRO EXCEPTION
            throw new RuntimeException(e);
        }
    }

    @Override
    public PetModel getPetById(Long id) {
        try {
            return petRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Pet não foi encontrado")); /// ADICIONAR OUTRO EXCEPTION
        } catch (Exception e) { /// ADICIONAR OUTRO EXCEPTION
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PetModel> getAllPets() {
        try {
            List<PetModel> pets = petRepository.findAll();

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado."); /// ADICIONAR OUTRO EXCEPTION
            }

            return pets;
        } catch (Exception e) { /// ADICIONAR OUTRO EXCEPTION
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PetModel> searchPetsByBreed(String breed) {
        try {
            List<PetModel> pets = petRepository.findByBreedIgnoreCase(breed);

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado."); /// ADICIONAR OUTRO EXCEPTION
            }

            return pets;
        } catch (Exception e) { /// ADICIONAR OUTRO EXCEPTION
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PetModel> searchPetsBySpecies(String species) {
        try {
            List<PetModel> pets = petRepository.findBySpecies(species);

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado."); /// ADICIONAR OUTRO EXCEPTION
            }

            return pets;
        } catch (Exception e) { /// ADICIONAR OUTRO EXCEPTION
            throw new RuntimeException(e);
        }
    }

    public List<PetModel> searchPetsBySpeciesAndBreed(String species, String breed) {
        try {
            List<PetModel> pets = petRepository.findBySpeciesAndBreed(species, breed);

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado."); /// ADICIONAR OUTRO EXCEPTION
            }

            return pets;
        } catch (Exception e) { /// ADICIONAR OUTRO EXCEPTION
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public PetModel updatePet(Long id, PetModel pet) {
        try {
            if (!petRepository.existsById(id)) {
                throw new NoSuchElementException("Nenhum elemento encontrado."); /// ADICIONAR OUTRO EXCEPTION
            }

            pet.setPetId(id);

//            String imgUrl = getBreedImage(pet.getBreed());
//            pet.setReferenceImage(imgUrl);

            return petRepository.save(pet);
        } catch (Exception e) { /// ADICIONAR OUTRO EXCEPTION
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePet(Long id) {
        try {
            if (!petRepository.existsById(id)) {
                throw new NoSuchElementException("Nenhum elemento encontrado."); /// ADICIONAR OUTRO EXCEPTION
            }

            petRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e); /// ADICIONAR OUTRO EXCEPTION
        }
    }

    @Override
    public List<String> getAllDogsBreeds() {
        return List.of();
    }

    @Override
    public List<String> getAllCatsBreeds() {
        return List.of();
    }

    @Override
    public String getBreedImage(String breed) {
        return "";
    }
}
