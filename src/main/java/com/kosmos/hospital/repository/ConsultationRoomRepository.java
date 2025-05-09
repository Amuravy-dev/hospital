package com.kosmos.hospital.repository;

import com.kosmos.hospital.model.ConsultationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRoomRepository extends JpaRepository<ConsultationRoom, Integer> {
}