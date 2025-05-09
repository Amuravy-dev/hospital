package com.kosmos.hospital.service;

import com.kosmos.hospital.model.Appointment;
import com.kosmos.hospital.model.ConsultationRoom;
import com.kosmos.hospital.model.Doctor;
import com.kosmos.hospital.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findById(Integer id) {
        return appointmentRepository.findById(id);
    }

    @Transactional
    public Appointment save(Appointment appointment) throws IllegalArgumentException {
        validateAppointment(appointment);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment update(Appointment appointment) throws IllegalArgumentException {
        validateAppointmentForUpdate(appointment);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void cancelAppointment(Integer id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setCancelled(true);
            appointmentRepository.save(appointment);
        } else {
            throw new IllegalArgumentException("Cita no encontrada con ID: " + id);
        }
    }

    public List<Appointment> findByDoctorAndDate(Doctor doctor, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByDoctorAndAppointmentDateTimeBetweenAndCancelledFalseOrderByAppointmentDateTime(
                doctor, startOfDay, endOfDay);
    }

    public List<Appointment> findByConsultationRoomAndDate(ConsultationRoom consultationRoom, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByConsultationRoomAndAppointmentDateTimeBetweenAndCancelledFalseOrderByAppointmentDateTime(
                consultationRoom, startOfDay, endOfDay);
    }

    public List<Appointment> findByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByAppointmentDateTimeBetweenAndCancelledFalseOrderByAppointmentDateTime(
                startOfDay, endOfDay);
    }

    private void validateAppointment(Appointment appointment) {
        if (appointmentRepository.existsByConsultationRoomAndAppointmentDateTimeAndCancelledFalse(
                appointment.getConsultationRoom(), appointment.getAppointmentDateTime())) {
            throw new IllegalArgumentException("El consultorio ya está ocupado en ese horario.");
        }

        if (appointmentRepository.existsByDoctorAndAppointmentDateTimeAndCancelledFalse(
                appointment.getDoctor(), appointment.getAppointmentDateTime())) {
            throw new IllegalArgumentException("El doctor ya tiene una cita agendada en ese horario.");
        }

        LocalDate appointmentDate = appointment.getAppointmentDateTime().toLocalDate();
        List<Appointment> patientAppointments = appointmentRepository.findByPatientNameAndDate(
                appointment.getPatientName(), appointmentDate);
        
        for (Appointment existingAppointment : patientAppointments) {
            long hoursDifference = Math.abs(
                    appointment.getAppointmentDateTime().getHour() - 
                    existingAppointment.getAppointmentDateTime().getHour());
            
            if (hoursDifference < 2) {
                throw new IllegalArgumentException(
                        "El paciente ya tiene una cita programada a menos de 2 horas de diferencia en el mismo día.");
            }
        }

        int doctorAppointmentsCount = appointmentRepository.countByDoctorAndDate(
                appointment.getDoctor(), appointmentDate);
        
        if (doctorAppointmentsCount >= 8) {
            throw new IllegalArgumentException(
                    "El doctor ya tiene el máximo de 8 citas programadas para este día.");
        }
    }

    private void validateAppointmentForUpdate(Appointment appointment) {
        Optional<Appointment> existingAppointmentOpt = appointmentRepository.findById(appointment.getId());
        if (!existingAppointmentOpt.isPresent()) {
            throw new IllegalArgumentException("La cita que intenta actualizar no existe.");
        }
        
        Appointment existingAppointment = existingAppointmentOpt.get();
        
        if (existingAppointment.getAppointmentDateTime().equals(appointment.getAppointmentDateTime()) &&
            existingAppointment.getDoctor().getId().equals(appointment.getDoctor().getId()) &&
            existingAppointment.getConsultationRoom().getId().equals(appointment.getConsultationRoom().getId()) &&
            existingAppointment.getPatientName().equals(appointment.getPatientName())) {
            return;
        }
        
        validateAppointment(appointment);
    }
}