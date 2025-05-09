package com.kosmos.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Column(nullable = false)
    private String lastName;

    @Column
    private String maternalLastName;

    @NotBlank(message = "La especialidad es obligatoria")
    @Column(nullable = false)
    private String specialty;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor(String firstName, String lastName, String maternalLastName, String specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.maternalLastName = maternalLastName;
        this.specialty = specialty;
    }

    public String getFullName() {
        return firstName + " " + lastName + (maternalLastName != null ? " " + maternalLastName : "");
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", maternalLastName='" + maternalLastName + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}