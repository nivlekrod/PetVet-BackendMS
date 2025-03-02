package com.gftstart.ms.petregister.services.impl;

import com.gftstart.ms.petregister.dtos.PetApiInfosDTO;
import com.gftstart.ms.petregister.dtos.PetImagesUrlDTO;
import com.gftstart.ms.petregister.enums.Species;
import com.gftstart.ms.petregister.models.PetModel;
import com.gftstart.ms.petregister.repositories.PetRepository;
import com.gftstart.ms.petregister.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CAT_API_URL = "https://api.thecatapi.com/v1";
    private static final String DOG_API_URL = "https://api.thedogapi.com/v1";

    @Value("${apikey}")
    private String apiKey;

    @Override
    @Transactional
    public PetModel createPet(PetModel pet) {
        try {
            if (pet == null || pet.getSpecies() == null) {
                throw new IllegalArgumentException("O pet ou a espécie não podem ser nulos.");
            }

            String imgUrl;

            if (pet.getSpecies() == Species.CAT) {
                imgUrl = getCatImage(pet.getBreed());
                pet.setReferenceImage(imgUrl);
            } else if (pet.getSpecies() == Species.DOG) {
                imgUrl = getDogImage(pet.getBreed());
                pet.setReferenceImage(imgUrl);
            } else {
                throw new IllegalArgumentException("Espécie não suportada: " + pet.getSpecies());
            }

            return petRepository.save(pet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PetModel getPetById(UUID id) {
        try {
            return petRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Pet não foi encontrado"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PetModel> getAllPets() {
        try {
            List<PetModel> pets = petRepository.findAll();

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado.");
            }

            return pets;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PetModel> searchPetsByBreed(String breed) {
        try {
            List<PetModel> pets = petRepository.findByBreedIgnoreCase(breed);

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado.");
            }

            return pets;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PetModel> searchPetsBySpecies(Species species) {
        try {
            List<PetModel> pets = petRepository.findBySpeciesIgnoreCase(species);

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado.");
            }

            return pets;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //CORRIGIR
    public List<PetModel> searchPetsBySpeciesAndBreed(Species species, String breed) {
        try {
            List<PetModel> pets = petRepository.findBySpeciesAndBreedIgnoreCase(species, breed);

            if (pets.isEmpty()) {
                throw new NoSuchElementException("Lista está vazia, nenhum elemento encontrado.");
            }

            return pets;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public PetModel updatePet(UUID id, PetModel pet) {
        try {
            if (!petRepository.existsById(id)) {
                throw new NoSuchElementException("Nenhum elemento encontrado.");
            }

            pet.setPetId(id);

            String imgUrl;

            if (pet.getSpecies() == Species.CAT) {
                imgUrl = getCatImage(pet.getBreed());
                pet.setReferenceImage(imgUrl);
            } else if (pet.getSpecies() == Species.DOG) {
                imgUrl = getDogImage(pet.getBreed());
                pet.setReferenceImage(imgUrl);
            } else {
                throw new IllegalArgumentException("Espécie não suportada: " + pet.getSpecies());
            }

            return petRepository.save(pet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deletePet(UUID id) {
        try {
            if (!petRepository.existsById(id)) {
                throw new NoSuchElementException("Nenhum elemento encontrado.");
            }

            petRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

//    @Override
//    public String getBreedImage(Species species, String breed) {
//        return "";
//    }

    @Override
    public String getCatImage(String breed) {
        String url = CAT_API_URL + "/breeds";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        try {
            ResponseEntity<List<PetApiInfosDTO>> responseImageId = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<PetApiInfosDTO>>() {
                    }
            );

            String imgReferenceId = responseImageId.getBody().stream()
                    .filter(pet -> breed.equalsIgnoreCase(pet.getBreed()))
                    .map(PetApiInfosDTO::getReferenceImage)
                    .findFirst()
                    .orElse("Imagem da raça não encontrada");

            String imageUrl = CAT_API_URL + "/images/" + imgReferenceId; // link da api externa que
                                                                         // mostra as urls de imagens referencias

            ResponseEntity<PetImagesUrlDTO> responseImages = restTemplate.exchange(
                    imageUrl,
                    HttpMethod.GET,
                    entity,
                    PetImagesUrlDTO.class
            );

            PetImagesUrlDTO imgsReference = responseImages.getBody();

            if (imgReferenceId.equals("Imagem da raça não encontrada")) {
                return "Imagem da raça não encontrada";
            }

            return imgsReference.getUrl();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDogImage(String breed) {
        String url = DOG_API_URL + "/breeds";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        try {
            ResponseEntity<List<PetApiInfosDTO>> responseImageId = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<PetApiInfosDTO>>() {
                    }
            );

            String imgReferenceId = responseImageId.getBody().stream()
                    .filter(pet -> breed.equalsIgnoreCase(pet.getBreed()))
                    .map(PetApiInfosDTO::getReferenceImage)
                    .findFirst()
                    .orElse("Imagem da raça não encontrada");

            String imageUrl = DOG_API_URL + "/images/" + imgReferenceId; // link da api externa que
                                                                          // mostra as urls de imagens referencias

            ResponseEntity<PetImagesUrlDTO> responseImages = restTemplate.exchange(
                    imageUrl,
                    HttpMethod.GET,
                    entity,
                    PetImagesUrlDTO.class
            );

            PetImagesUrlDTO imgsReference = responseImages.getBody();

            if (imgReferenceId.equals("Imagem da raça não encontrada")) {
                return "Imagem da raça não encontrada";
            }

            return imgsReference.getUrl();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}