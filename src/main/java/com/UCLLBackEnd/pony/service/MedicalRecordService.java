package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Animal;
import com.UCLLBackEnd.pony.model.MedicalRecord;
import com.UCLLBackEnd.pony.repository.AnimalRepository;
import com.UCLLBackEnd.pony.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private MedicalRecordRepository medicalRecordRepository;

    private AnimalRepository animalRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, AnimalRepository animalRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.animalRepository = animalRepository;
    }

    public MedicalRecord addClosingDateToExistingMedicalRecordWithExistingAnimal(Long animalId, Long medicalRecordId, LocalDate date) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ServiceException("ServiceException", "Medical record with id " + medicalRecordId + " not found"));

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ServiceException("ServiceException", "Animal with id " + animalId + " not found"));

        if (date == null) {
            throw new ServiceException("ServiceException", "Date cannot be null");
        }

        medicalRecord.setClosingDate(date);
        animal.setMedicalRecords(Set.of(medicalRecord));
        return medicalRecordRepository.save(medicalRecord);
    }

    public List<MedicalRecord> getMedicalRecordsAfterGivenRegistrationDate(String animalName, LocalDate registrationDate) {

        if (registrationDate == null) {
            throw new ServiceException("ServiceException", "Registration date cannot be null");
        }

        return medicalRecordRepository.findAll()
                .stream()
                .filter(m -> Objects.equals(m.getAnimal().getName(), animalName) && m.getRegistrationDate().isAfter(registrationDate))
                .collect(Collectors.toList());

    }











}
