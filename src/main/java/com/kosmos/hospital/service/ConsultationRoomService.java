package com.kosmos.hospital.service;

import com.kosmos.hospital.model.ConsultationRoom;
import com.kosmos.hospital.repository.ConsultationRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationRoomService {

    private final ConsultationRoomRepository consultationRoomRepository;

    @Autowired
    public ConsultationRoomService(ConsultationRoomRepository consultationRoomRepository) {
        this.consultationRoomRepository = consultationRoomRepository;
    }

    public List<ConsultationRoom> findAll() {
        return consultationRoomRepository.findAll();
    }

    public Optional<ConsultationRoom> findById(Integer id) {
        return consultationRoomRepository.findById(id);
    }

    public ConsultationRoom save(ConsultationRoom consultationRoom) {
        return consultationRoomRepository.save(consultationRoom);
    }

    public void deleteById(Integer id) {
        consultationRoomRepository.deleteById(id);
    }
}