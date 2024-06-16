package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.Toy;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import com.UCLLBackEnd.pony.repository.ToyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ToyServiceTest {

    @Mock
    private ToyRepository toyRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    ToyService toyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenAddToy_thenToyIsAdded() {

        Toy toy = new Toy("Whatever toy");
        when(toyRepository.save(toy)).thenReturn(toy);

        Toy result = toyService.addToy(toy);

        assertEquals(toy, result);
        verify(toyRepository, times(1)).save(toy);
    }

    @Test
    public void givenNonExistingToyId_whenAddAnimalToToy_thenExceptionIsThrown() {

        Long toyId = 1L;
        Animal animal = new Animal("Bellaera", 12);
        animal.setId(1L);
        when(animalRepository.findById(toyId)).thenReturn(Optional.of(animal));

        ServiceException ex = assertThrows(ServiceException.class,
                () -> toyService.addAnimalToToy(animal.getId(), toyId));

        assertEquals("ServiceException", ex.getField());
        assertEquals("Toy with " + toyId + " does not exist", ex.getMessage());
        verify(toyRepository).findById(1L);
    }

    @Test
    public void givenNonExistingAnimalId_whenAddAnimalToToy_thenExceptionIsThrown() {

        Long animalId = 1L;
        Toy toy = new Toy("Whatever toy");
        toy.setId(1L);
        when(toyRepository.findById(1L)).thenReturn(Optional.of(toy));

        ServiceException ex = assertThrows(ServiceException.class,
                () -> toyService.addAnimalToToy(animalId, toy.getId()));

        assertEquals("ServiceException", ex.getField());
        assertEquals("Animal with " + animalId + " does not exist", ex.getMessage());
        verify(toyRepository).findById(1L);
    }

    @Test
    public void givenValidAnimalIdAndToyId_whenAddAnimalToToy_thenAnimalIsAddedToToy() {

        Animal animal = new Animal("Bellaera", 12);
        animal.setId(1L);
        Toy toy = new Toy("Whatever toy");
        toy.setId(1L);
        when(toyRepository.findById(1L)).thenReturn(Optional.of(toy));
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        toy.setAnimals(new HashSet<>(Set.of(animal)));
        animal.setToys(new HashSet<>(Set.of(toy)));
        when(toyRepository.save(toy)).thenReturn(toy);

        Toy result = toyService.addAnimalToToy(animal.getId(), toy.getId());

        assertEquals(toy, result);
        verify(toyRepository, times(1)).findById(1L);
        verify(animalRepository, times(1)).findById(1L);
        verify(toyRepository, times(1)).save(toy);
    }


    @Test
    public void givenNonExistingToyName_whenGetToyWithSpecificName_thenExceptionIsThrown() {

        String toyName = "Whatever toy";

        when(toyRepository.findByName(toyName)).thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            toyService.getToyWithSpecificName(toyName);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Toy with " + toyName + " does not exist", ex.getMessage());
        verify(toyRepository, times(1)).findByName(toyName);
    }

    @Test
    public void givenValidToyName_whenGetToyWithSpecificName_thenToyWithSpecificNameIsReturned() {

        Toy toy = new Toy("Whatever toy");
        toy.setId(1L);
        when(toyRepository.findByName(toy.getName())).thenReturn(toy);

        Toy result = toyService.getToyWithSpecificName(toy.getName());

        assertEquals(toy, result);
        verify(toyRepository, times(2)).findByName(toy.getName());
    }
}