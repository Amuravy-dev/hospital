package com.kosmos.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consultation_rooms")
@Getter
@Setter
@NoArgsConstructor
public class ConsultationRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El número de consultorio es obligatorio")
    @Min(value = 1, message = "El número de consultorio debe ser mayor a 0")
    @Column(nullable = false)
    private Integer roomNumber;

    @NotNull(message = "El piso es obligatorio")
    @Min(value = 1, message = "El piso debe ser mayor a 0")
    @Column(nullable = false)
    private Integer floor;

    @OneToMany(mappedBy = "consultationRoom", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    public ConsultationRoom(Integer roomNumber, Integer floor) {
        this.roomNumber = roomNumber;
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Consultorio " + roomNumber + " (Piso " + floor + ")";
    }
}