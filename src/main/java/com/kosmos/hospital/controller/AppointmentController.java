package com.kosmos.hospital.controller;

import com.kosmos.hospital.model.Appointment;
import com.kosmos.hospital.model.ConsultationRoom;
import com.kosmos.hospital.model.Doctor;
import com.kosmos.hospital.service.AppointmentService;
import com.kosmos.hospital.service.ConsultationRoomService;
import com.kosmos.hospital.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final ConsultationRoomService roomService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, 
                                DoctorService doctorService, 
                                ConsultationRoomService roomService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.roomService = roomService;
    }

    @GetMapping
    public String listAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer roomId,
            Model model) {
        
        List<Appointment> appointments;
        
        if (date == null) {
            date = LocalDate.now();
        }
        
        if (doctorId != null) {
            Optional<Doctor> doctor = doctorService.findById(doctorId);
            if (doctor.isPresent()) {
                appointments = appointmentService.findByDoctorAndDate(doctor.get(), date);
                model.addAttribute("selectedDoctor", doctor.get());
            } else {
                appointments = appointmentService.findByDate(date);
            }
        } else if (roomId != null) {
            Optional<ConsultationRoom> room = roomService.findById(roomId);
            if (room.isPresent()) {
                appointments = appointmentService.findByConsultationRoomAndDate(room.get(), date);
                model.addAttribute("selectedRoom", room.get());
            } else {
                appointments = appointmentService.findByDate(date);
            }
        } else {
            appointments = appointmentService.findByDate(date);
        }
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("selectedDate", date);
        
        return "appointments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        return "appointments/form";
    }

    @PostMapping
    public String createAppointment(@ModelAttribute Appointment appointment, 
                                   BindingResult result, 
                                   @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                   @RequestParam("doctorId") Integer doctorId,
                                   @RequestParam("roomId") Integer roomId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        try {
            Doctor doctor = doctorService.findById(doctorId)
                    .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));
            appointment.setDoctor(doctor);

            ConsultationRoom room = roomService.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado"));
            appointment.setConsultationRoom(room);

            appointment.setAppointmentDateTime(LocalDateTime.of(date, time));
            
            jakarta.validation.Validator validator = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
            var violations = validator.validate(appointment);
            
            if (!violations.isEmpty()) {
                for (var violation : violations) {
                    String propertyPath = violation.getPropertyPath().toString();
                    String message = violation.getMessage();
                    result.rejectValue(propertyPath, "error." + propertyPath, message);
                }
            }
            
            if (result.hasErrors()) {
                model.addAttribute("doctors", doctorService.findAll());
                model.addAttribute("rooms", roomService.findAll());
                model.addAttribute("date", date);
                model.addAttribute("time", time);
                return "appointments/form";
            }

            appointmentService.save(appointment);
            redirectAttributes.addFlashAttribute("message", "Cita creada exitosamente");
            return "redirect:/appointments";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("rooms", roomService.findAll());
            model.addAttribute("date", date);
            model.addAttribute("time", time);
            return "appointments/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("date", appointment.getAppointmentDateTime().toLocalDate());
        model.addAttribute("time", appointment.getAppointmentDateTime().toLocalTime());
        
        return "appointments/form";
    }

    @PostMapping("/{id}")
    public String updateAppointment(@PathVariable Integer id, 
                                   @ModelAttribute Appointment appointment, 
                                   BindingResult result, 
                                   @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                   @RequestParam("doctorId") Integer doctorId,
                                   @RequestParam("roomId") Integer roomId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        
        try {
            Doctor doctor = doctorService.findById(doctorId)
                    .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));
            appointment.setDoctor(doctor);
            
            ConsultationRoom room = roomService.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado"));
            appointment.setConsultationRoom(room);
            
            appointment.setAppointmentDateTime(LocalDateTime.of(date, time));
            
            appointment.setId(id);
            
            jakarta.validation.Validator validator = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
            var violations = validator.validate(appointment);
            
            if (!violations.isEmpty()) {
                for (var violation : violations) {
                    String propertyPath = violation.getPropertyPath().toString();
                    String message = violation.getMessage();
                    result.rejectValue(propertyPath, "error." + propertyPath, message);
                }
            }
            
            if (result.hasErrors()) {
                model.addAttribute("doctors", doctorService.findAll());
                model.addAttribute("rooms", roomService.findAll());
                return "appointments/form";
            }
            
            appointmentService.update(appointment);
            redirectAttributes.addFlashAttribute("message", "Cita actualizada exitosamente");
            return "redirect:/appointments";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("rooms", roomService.findAll());
            return "appointments/form";
        }
    }

    @GetMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.cancelAppointment(id);
            redirectAttributes.addFlashAttribute("message", "Cita cancelada exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/appointments";
    }
}