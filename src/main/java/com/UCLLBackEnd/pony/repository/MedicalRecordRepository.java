package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}
