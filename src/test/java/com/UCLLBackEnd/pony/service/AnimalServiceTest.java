package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
}