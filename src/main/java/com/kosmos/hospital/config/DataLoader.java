package com.kosmos.hospital.config;

import com.kosmos.hospital.model.ConsultationRoom;
import com.kosmos.hospital.model.Doctor;
import com.kosmos.hospital.service.ConsultationRoomService;
import com.kosmos.hospital.service.DoctorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final DoctorService doctorService;
    private final ConsultationRoomService roomService;

    @Autowired
    public DataLoader(DoctorService doctorService, ConsultationRoomService roomService) {
        this.doctorService = doctorService;
        this.roomService = roomService;
    }

    @PostConstruct
    public void loadData() {
        if (doctorService.findAll().isEmpty()) {
            loadDoctors();
        }

        if (roomService.findAll().isEmpty()) {
            loadRooms();
        }
    }
    
    private void loadDoctors() {
        Doctor doctor1 = new Doctor();
        doctor1.setFirstName("Carlos");
        doctor1.setLastName("Ramirez");
        doctor1.setMaternalLastName("Lopez");
        doctor1.setSpecialty("Cardiologia");
        doctorService.save(doctor1);
        
        Doctor doctor2 = new Doctor();
        doctor2.setFirstName("Ana");
        doctor2.setLastName("Martinez");
        doctor2.setMaternalLastName("Gomez");
        doctor2.setSpecialty("Infectologia");
        doctorService.save(doctor2);
        
        Doctor doctor3 = new Doctor();
        doctor3.setFirstName("Roberto");
        doctor3.setLastName("Fernandez");
        doctor3.setMaternalLastName("Diaz");
        doctor3.setSpecialty("Medicina Interna");
        doctorService.save(doctor3);
        
        Doctor doctor4 = new Doctor();
        doctor4.setFirstName("Laura");
        doctor4.setLastName("Sanchez");
        doctor4.setMaternalLastName("Perez");
        doctor4.setSpecialty("Medicina Interna");
        doctorService.save(doctor4);
        
        Doctor doctor5 = new Doctor();
        doctor5.setFirstName("Miguel");
        doctor5.setLastName("Torres");
        doctor5.setMaternalLastName("Vega");
        doctor5.setSpecialty("Medicina Interna");
        doctorService.save(doctor5);
    }
    
    private void loadRooms() {
        ConsultationRoom room1 = new ConsultationRoom();
        room1.setRoomNumber(101);
        room1.setFloor(1);
        roomService.save(room1);
        
        ConsultationRoom room2 = new ConsultationRoom();
        room2.setRoomNumber(102);
        room2.setFloor(1);
        roomService.save(room2);
        
        ConsultationRoom room3 = new ConsultationRoom();
        room3.setRoomNumber(201);
        room3.setFloor(2);
        roomService.save(room3);
        
        ConsultationRoom room4 = new ConsultationRoom();
        room4.setRoomNumber(202);
        room4.setFloor(2);
        roomService.save(room4);
        
        ConsultationRoom room5 = new ConsultationRoom();
        room5.setRoomNumber(301);
        room5.setFloor(3);
        roomService.save(room5);
    }
}