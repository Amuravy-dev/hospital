package com.kosmos.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "El doctor es obligatorio")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "consultation_room_id", nullable = false)
    @NotNull(message = "El consultorio es obligatorio")
    private ConsultationRoom consultationRoom;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Column(nullable = false)
    private String patientName;

    @NotNull(message = "La fecha y hora de la cita son obligatorias")
    @FutureOrPresent(message = "La fecha de la cita debe ser actual o futura")
    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(nullable = false)
    private boolean cancelled = false;

    public Appointment(Doctor doctor, ConsultationRoom consultationRoom, String patientName, LocalDateTime appointmentDateTime) {
        this.doctor = doctor;
        this.consultationRoom = consultationRoom;
        this.patientName = patientName;
        this.appointmentDateTime = appointmentDateTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", consultationRoom=" + consultationRoom +
                ", patientName='" + patientName + '\'' +
                ", appointmentDateTime=" + appointmentDateTime +
                ", cancelled=" + cancelled +
                '}';
    }
}