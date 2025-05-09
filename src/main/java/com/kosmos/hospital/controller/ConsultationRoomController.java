package com.kosmos.hospital.controller;

import com.kosmos.hospital.model.ConsultationRoom;
import com.kosmos.hospital.service.ConsultationRoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class ConsultationRoomController {

    private final ConsultationRoomService roomService;

    @Autowired
    public ConsultationRoomController(ConsultationRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String listRooms(Model model) {
        List<ConsultationRoom> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "rooms/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new ConsultationRoom());
        return "rooms/form";
    }

    @PostMapping
    public String createRoom(@Valid @ModelAttribute("room") ConsultationRoom room, 
                            BindingResult result, 
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "rooms/form";
        }
        
        roomService.save(room);
        redirectAttributes.addFlashAttribute("message", "Consultorio creado exitosamente");
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        ConsultationRoom room = roomService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado con ID: " + id));
        model.addAttribute("room", room);
        return "rooms/form";
    }

    @PostMapping("/{id}")
    public String updateRoom(@PathVariable Integer id, 
                            @Valid @ModelAttribute("room") ConsultationRoom room, 
                            BindingResult result, 
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "rooms/form";
        }
        
        room.setId(id);
        roomService.save(room);
        redirectAttributes.addFlashAttribute("message", "Consultorio actualizado exitosamente");
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/delete")
    public String deleteRoom(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Consultorio eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el consultorio porque tiene citas asociadas");
        }
        return "redirect:/rooms";
    }
}