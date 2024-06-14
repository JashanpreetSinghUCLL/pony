package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalService animalService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenAnimal_whenAddAnimal_thenReturnAnimal() {

        Animal animal = new Animal("Rambo", 9);

        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.empty());
        when(animalRepository.save(animal)).thenReturn(animal);

        Animal savedAnimal = animalService.addAnimal(animal);

        assertEquals(animal, savedAnimal);
        verify(animalRepository, times(1)).save(animal);
        verify(animalRepository, times(1)).findByName(animal.getName());
    }

    @Test
    public void givenExistingAnimal_whenAddAnimal_thenThrowServiceException() {

        Animal animal = new Animal("Rambo", 9);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            animalService.addAnimal(animal);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Animal already exists", ex.getMessage());
        verify(animalRepository, never()).save(animal);
        verify(animalRepository, times(1)).findByName(animal.getName());
    }

    @Test
    public void whenGetAllAnimals_thenReturnAllAnimals() {

        List<Animal> animals = new ArrayList<>(List.of(
                new Animal("Leo", 2),
                new Animal("Jack The Fauji", 43)
        ));
        when(animalRepository.findAll()).thenReturn(animals);

        List<Animal> allAnimals = animalService.getAllAnimals();

        assertEquals(animals, allAnimals);
        verify(animalRepository, times(1)).findAll();
    }

    @Test
    public void givenAge_whenGetAnimalOlderThan_thenReturnTheOldestAnimal() {

        List<Animal> animals = new ArrayList<>(List.of(
                new Animal("Leo", 2),
                new Animal("Jack The Fauji", 43),
                new Animal("Rambo", 8)
        ));

        when(animalRepository.findByAgeGreaterThan(5)).thenReturn(animals);

        List<Animal> allAnimals = animalService.getAllAnimalsOlderThan(5);

        assertEquals(animals, allAnimals);
        verify(animalRepository, times(1)).findByAgeGreaterThan(5);
    }

    @Test
    public void givenNegativeAge_whenGetAnimalOlderThan_thenThrowServiceException() {

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            animalService.getAllAnimalsOlderThan(-1);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Age cannot be negative", ex.getMessage());
    }

    @Test
    public void givenInvalidAge_whenGetAnimalOlderThan_thenThrowServiceException() {

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            animalService.getAllAnimalsOlderThan(55);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Age must be between 1 and 50", ex.getMessage());
    }

    @Test
    public void whenGetOldestAnimal_thenReturnOldestAnimal() {

        Animal animal = new Animal("Leo", 2);

        when(animalRepository.findOldestAnimals()).thenReturn(animal);

        Animal OldestAnimal = animalService.getOldestAnimal();

        assertEquals(animal, OldestAnimal);
        verify(animalRepository, times(1)).findOldestAnimals();
    }

}