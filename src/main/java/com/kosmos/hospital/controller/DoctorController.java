package com.kosmos.hospital.controller;

import com.kosmos.hospital.model.Doctor;
import com.kosmos.hospital.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String listDoctors(Model model) {
        List<Doctor> doctors = doctorService.findAll();
        model.addAttribute("doctors", doctors);
        return "doctors/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @PostMapping
    public String createDoctor(@Valid @ModelAttribute Doctor doctor, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "doctors/form";
        }
        
        doctorService.save(doctor);
        redirectAttributes.addFlashAttribute("message", "Doctor creado exitosamente");
        return "redirect:/doctors";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Doctor doctor = doctorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado con ID: " + id));
        model.addAttribute("doctor", doctor);
        return "doctors/form";
    }

    @PostMapping("/{id}")
    public String updateDoctor(@PathVariable Integer id, 
                              @Valid @ModelAttribute Doctor doctor, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "doctors/form";
        }
        
        doctor.setId(id);
        doctorService.save(doctor);
        redirectAttributes.addFlashAttribute("message", "Doctor actualizado exitosamente");
        return "redirect:/doctors";
    }

    @GetMapping("/{id}/delete")
    public String deleteDoctor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Doctor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el doctor porque tiene citas asociadas");
        }
        return "redirect:/doctors";
    }
}