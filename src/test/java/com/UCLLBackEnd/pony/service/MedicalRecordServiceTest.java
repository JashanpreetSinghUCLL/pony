package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.MedicalRecord;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import com.UCLLBackEnd.pony.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenNonExistingAnimalId_whenAddClosingDateToExistingMedicalRecordWithExistingAnimal_thenExceptionIsThrown() {

        Long animalId = 1L;
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 3, 4), "Idk what");
        medicalRecord.setId(1L);
        LocalDate date = LocalDate.of(2024, 3, 5);
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.of(medicalRecord));
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            medicalRecordService.addClosingDateToExistingMedicalRecordWithExistingAnimal(animalId, medicalRecord.getId(), date);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Animal with id " + animalId + " not found", ex.getMessage());
        verify(medicalRecordRepository, times(1)).findById(medicalRecord.getId());
        verify(animalRepository, times(1)).findById(animalId);
    }

    @Test
    public void givenNonExistingMedicalRecordId_whenAddClosingDateToExistingMedicalRecordWithExistingAnimal_thenExceptionIsThrown() {

        Animal animal = new Animal("Lingchan", 13);
        animal.setId(1L);
        Long medicalRecordId = 1L;
        LocalDate date = LocalDate.of(2024, 3, 5);
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.empty());

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            medicalRecordService.addClosingDateToExistingMedicalRecordWithExistingAnimal(animal.getId(), medicalRecordId, date);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Medical record with id " + medicalRecordId + " not found", ex.getMessage());
        verify(medicalRecordRepository, times(1)).findById(medicalRecordId);
    }

    @Test
    public void givenNullDate_whenAddClosingDateToExistingMedicalRecordWithExistingAnimal_thenExceptionIsThrown() {

        Animal animal = new Animal("Lingchan", 13);
        animal.setId(1L);
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 3, 4), "Idk what");
        medicalRecord.setId(1L);
        LocalDate date = LocalDate.of(2024, 3, 5);
        medicalRecord.setClosingDate(date);
        animal.setMedicalRecords(Set.of(medicalRecord));
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.of(medicalRecord));
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.of(animal));
        when(medicalRecordRepository.save(any())).thenReturn(medicalRecord);


        MedicalRecord result = medicalRecordService.addClosingDateToExistingMedicalRecordWithExistingAnimal(animal.getId(), medicalRecord.getId(), date);

        assertEquals(medicalRecord, result);
        verify(medicalRecordRepository, times(1)).findById(medicalRecord.getId());
        verify(animalRepository, times(1)).findById(animal.getId());
        verify(medicalRecordRepository, times(1)).save(any());
    }

    @Test
    public void givenValidEverything_whenAddClosingDateToExistingMedicalRecordWithExistingAnimal_thenExceptionIsThrown() {

        Animal animal = new Animal("Lingchan", 13);
        animal.setId(1L);
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 3, 4), "Idk what");
        medicalRecord.setId(1L);
        LocalDate date = null;
        when(medicalRecordRepository.findById(medicalRecord.getId())).thenReturn(Optional.of(medicalRecord));
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.of(animal));

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            medicalRecordService.addClosingDateToExistingMedicalRecordWithExistingAnimal(animal.getId(), medicalRecord.getId(), date);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Date cannot be null", ex.getMessage());
        verify(medicalRecordRepository, times(1)).findById(medicalRecord.getId());
        verify(animalRepository, times(1)).findById(animal.getId());
    }

    @Test
    public void givenValidAnimalNameAndDate_whenGetMedicalRecordsAfterGivenRegistrationDate_thenReturnMedicalRecords() {
        String animalName = "Lingchan";
        LocalDate registrationDate = LocalDate.of(2023, 1, 1);
        Animal animal = new Animal(animalName, 13);
        animal.setId(1L);
        MedicalRecord medicalRecord1 = new MedicalRecord(LocalDate.of(2024, 3, 4), "Checkup 1");
        medicalRecord1.setAnimal(animal);
        MedicalRecord medicalRecord2 = new MedicalRecord(LocalDate.of(2023, 5, 20), "Checkup 2");
        medicalRecord2.setAnimal(animal);
        when(medicalRecordRepository.findAll()).thenReturn(List.of(medicalRecord1, medicalRecord2));

        List<MedicalRecord> result = medicalRecordService.getMedicalRecordsAfterGivenRegistrationDate(animalName, registrationDate);

        assertEquals(2, result.size());
        assertTrue(result.contains(medicalRecord1));
        assertTrue(result.contains(medicalRecord2));
        verify(medicalRecordRepository, times(1)).findAll();
    }

    @Test
    public void givenNonExistingAnimalName_whenGetMedicalRecordsAfterGivenRegistrationDate_thenReturnEmptyList() {
        String animalName = "NonExistentAnimal";
        LocalDate registrationDate = LocalDate.of(2023, 1, 1);
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 3, 4), "Checkup 1");
        Animal animal = new Animal("Lingchan", 13);
        animal.setId(1L);
        medicalRecord.setAnimal(animal);
        when(medicalRecordRepository.findAll()).thenReturn(List.of(medicalRecord));

        List<MedicalRecord> result = medicalRecordService.getMedicalRecordsAfterGivenRegistrationDate(animalName, registrationDate);

        assertTrue(result.isEmpty());
        verify(medicalRecordRepository, times(1)).findAll();
    }

    @Test
    public void givenNoMedicalRecordsAfterDate_whenGetMedicalRecordsAfterGivenRegistrationDate_thenReturnEmptyList() {
        String animalName = "Lingchan";
        LocalDate registrationDate = LocalDate.of(2025, 1, 1);
        Animal animal = new Animal(animalName, 13);
        animal.setId(1L);
        MedicalRecord medicalRecord = new MedicalRecord(LocalDate.of(2024, 3, 4), "Checkup 1");
        medicalRecord.setAnimal(animal);
        when(medicalRecordRepository.findAll()).thenReturn(List.of(medicalRecord));

        List<MedicalRecord> result = medicalRecordService.getMedicalRecordsAfterGivenRegistrationDate(animalName, registrationDate);

        assertTrue(result.isEmpty());
        verify(medicalRecordRepository, times(1)).findAll();
    }

    @Test
    public void givenNullRegistrationDate_whenGetMedicalRecordsAfterGivenRegistrationDate_thenExceptionIsThrown() {
        String animalName = "Lingchan";
        LocalDate registrationDate = null;

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            medicalRecordService.getMedicalRecordsAfterGivenRegistrationDate(animalName, registrationDate);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Registration date cannot be null", ex.getMessage());
        verify(medicalRecordRepository, times(0)).findAll();
    }

}