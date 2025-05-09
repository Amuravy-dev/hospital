package com.kosmos.hospital.repository;

import com.kosmos.hospital.model.Appointment;
import com.kosmos.hospital.model.ConsultationRoom;
import com.kosmos.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    
    List<Appointment> findByDoctorAndAppointmentDateTimeBetweenAndCancelledFalseOrderByAppointmentDateTime(
            Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    List<Appointment> findByConsultationRoomAndAppointmentDateTimeBetweenAndCancelledFalseOrderByAppointmentDateTime(
            ConsultationRoom consultationRoom, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    List<Appointment> findByAppointmentDateTimeBetweenAndCancelledFalseOrderByAppointmentDateTime(
            LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    List<Appointment> findByPatientNameContainingIgnoreCaseAndAppointmentDateTimeBetweenAndCancelledFalseOrderByAppointmentDateTime(
            String patientName, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor = :doctor AND " +
           "FUNCTION('DATE', a.appointmentDateTime) = FUNCTION('DATE', :date) AND a.cancelled = false")
    int countByDoctorAndDate(@Param("doctor") Doctor doctor, @Param("date") LocalDate date);
    
    boolean existsByDoctorAndAppointmentDateTimeAndCancelledFalse(Doctor doctor, LocalDateTime dateTime);
    
    boolean existsByConsultationRoomAndAppointmentDateTimeAndCancelledFalse(ConsultationRoom consultationRoom, LocalDateTime dateTime);
    
    @Query("SELECT a FROM Appointment a WHERE a.patientName = :patientName AND " +
           "FUNCTION('DATE', a.appointmentDateTime) = FUNCTION('DATE', :date) AND a.cancelled = false " +
           "ORDER BY a.appointmentDateTime")
    List<Appointment> findByPatientNameAndDate(@Param("patientName") String patientName, @Param("date") LocalDate date);
}