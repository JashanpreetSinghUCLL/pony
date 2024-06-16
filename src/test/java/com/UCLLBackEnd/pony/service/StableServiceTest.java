package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.Stable;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import com.UCLLBackEnd.pony.repository.StableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StableServiceTest {

    @Mock
    private StableRepository stableRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private StableService stableService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenNonExistingAnimal_whenAssignAnimalToNewStable_thenThrowException() {

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            String animalName = "Lloyd";
            Stable stable = new Stable("Big Farm", 5, "Desi Owner");
            stableService.assignAnimalToNewStable(animalName, stable);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Animal doesn't exist", ex.getMessage());
    }

    @Test
    public void givenNullStable_whenAssignAnimalToNewStable_thenThrowException() {
        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));
        Stable stable = null;

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.assignAnimalToNewStable(animal.getName(), stable);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Stable not given", ex.getMessage());
    }

    @Test
    public void givenExistingStable_whenAssignAnimalToNewStable_thenThrowException() {
        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));
        Stable stable = new Stable("Big Farm", 5, "Desi Owner");
        when(stableRepository.existsByName(stable.getName())).thenReturn(true);

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.assignAnimalToNewStable(animal.getName(), stable);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Stable already exists", ex.getMessage());
    }

    @Test
    public void givenStableWith0MaxNumberOfAnimals_whenAssignAnimalToNewStable_thenThrowException() {
        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));
        Stable stable = new Stable("Big Farm", 0, "Desi Owner");

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.assignAnimalToNewStable(animal.getName(), stable);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("No animals can be assigned because of max number of animals is 0", ex.getMessage());
    }

    @Test
    public void givenValidAnimalAndNewStable_whenAssignAnimalToNewStable_thenAnimalIsAssignedToNewStable() {
        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));
        Stable stable = new Stable("Big Farm", 5, "Desi Owner");
        when(animalRepository.save(any(Animal.class))).thenAnswer(i -> i.getArgument(0));
        when(stableRepository.save(any(Stable.class))).thenAnswer(i -> i.getArgument(0));

        Stable newStable = stableService.assignAnimalToNewStable(animal.getName(), stable);

        assertEquals(animal.getStable(), newStable);
        verify(animalRepository, times(1)).findByName(animal.getName());
    }

    @Test
    public void givenNonExistingAnimal_whenAssignAnimalToExistingStable_thenThrowException() {

        String animalName = "LLoyd";
        Stable stable = new Stable("Big Farm", 5, "Desi Owner");
        stable.setId(1L);
        when(stableRepository.findById(1L)).thenReturn(Optional.of(stable));

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.assignAnimalToExistingStable(animalName, stable.getId());
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Animal doesn't exist", ex.getMessage());
    }

    @Test
    public void givenNonExistingStableId_whenAssignAnimalToExistingStable_thenThrowException() {

        Long stableId = 1L;
        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.assignAnimalToExistingStable(animal.getName(), stableId);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Stable with id " + stableId + " doesn't exist", ex.getMessage());
    }

    @Test
    public void givenMax_number_of_animals_is_0_whenAssignAnimalToExistingStable_thenThrowException() {

        Stable stable = new Stable("Big Farm", 0, "Desi Owner");
        stable.setId(1L);
        when(stableRepository.findById(1L)).thenReturn(Optional.of(stable));
        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.assignAnimalToExistingStable(animal.getName(), stable.getId());
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("No animals can be assigned to this stable, It's maxed out of animals", ex.getMessage());
    }

    @Test
    public void givenAnimalToAssignAndStableIsMaxedOut_whenAssignAnimalToExistingStable_thenThrowException() {

        Stable stable = new Stable("Big Farm", 1, "Desi Owner");
        stable.setId(1L);
        when(stableRepository.findById(1L)).thenReturn(Optional.of(stable));

        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));

        Animal animal2 = new Animal("Fred", 10);
        when(animalRepository.findByName(animal2.getName())).thenReturn(Optional.of(animal2));

        List<Animal> animalsInStable = List.of(animal);
        when(animalRepository.findByStableWithId(stable.getId())).thenReturn(animalsInStable);

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.assignAnimalToExistingStable(animal2.getName(), stable.getId());
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Max number of animals reached", ex.getMessage());
    }

    @Test
    public void givenAnimalAndExistingStable_whenAssignAnimalToExistingStable_thenAnimalIsAssignedToExistingStable() {

        Stable stable = new Stable("Big Farm", 1, "Desi Owner");
        stable.setId(1L);
        when(stableRepository.findById(1L)).thenReturn(Optional.of(stable));

        Animal animal = new Animal("LLoyd", 8);
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));

        when(animalRepository.save(any(Animal.class))).thenAnswer(i -> i.getArgument(0));
        when(stableRepository.save(any(Stable.class))).thenAnswer(i -> i.getArgument(0));

        Stable stableWithAnimal = stableService.assignAnimalToExistingStable(animal.getName(), stable.getId());

        assertEquals(animal.getStable(), stableWithAnimal);
        verify(stableRepository, times(1)).findById(1L);
        verify(animalRepository, times(1)).findByName(animal.getName());
    }

    @Test
    public void whenGetAllStablesWithAnimalsSleepingInThem_thenAllStablesWithAnimalsAreReturned() {

        Animal animal1 = new Animal("Lloyd", 8);
        Animal animal2 = new Animal("Fred", 10);

        Stable stable1 = new Stable("Big Farm", 5, "Desi Owner");
        stable1.setId(1L);
        stable1.setAnimals(Collections.singleton(animal1));

        Stable stable2 = new Stable("Small Farm", 3, "Desi Owner");
        stable2.setId(2L);
        stable2.setAnimals(Collections.singleton(animal2));

        List<Stable> allStables = Arrays.asList(stable1, stable2);

        when(stableRepository.findAll()).thenReturn(allStables);

        List<Stable> result = stableService.getAllStablesWithAnimalsSleepingInThem();

        assertEquals(2, result.size());
        assertEquals("Big Farm", result.get(0).getName());
        assertEquals("Small Farm", result.get(1).getName());
    }

    @Test
    public void givenNonExistingAnimal_whenGetStablesOfAnimal_thenThrowException() {

        String animalName = "Big Dude";

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            stableService.getStableOfAnimal(animalName);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("The given animal doesn't exists", ex.getMessage());
    }

    @Test
    public void givenValidAnimal_whenGetStablesOfAnimal_thenStableOfAnimalIsReturned() {

        Stable stable = new Stable("Big Farm", 5, "Desi Owner");
        stable.setId(1L);

        Animal animal = new Animal("Lloyd", 8);
        animal.setStable(stable);

        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));

        Stable stableOfAnimal = stableService.getStableOfAnimal(animal.getName());

        assertEquals(animal.getStable(), stableOfAnimal);
        verify(animalRepository, times(1)).findByName(animal.getName());
    }

}