package com.riya.interntrack.controller;

import com.riya.interntrack.model.AppUser;
import com.riya.interntrack.model.InternshipApplication;
import com.riya.interntrack.repository.InternshipRepository;
import com.riya.interntrack.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/internships")
public class InternshipController {

    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;

    public InternshipController(InternshipRepository internshipRepository, UserRepository userRepository) {
        this.internshipRepository = internshipRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listInternships(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            Authentication authentication,
            Model model
    ) {
        AppUser user = getCurrentUser(authentication);
        List<InternshipApplication> internships = internshipRepository.searchForUser(user, keyword, status);

        model.addAttribute("internships", internships);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("today", LocalDate.now());
        return "internships";
    }

    @GetMapping("/new")
    public String newInternship(Model model) {
        InternshipApplication internship = new InternshipApplication();
        internship.setApplicationDate(LocalDate.now());
        model.addAttribute("internship", internship);
        model.addAttribute("pageTitle", "Add Internship");
        return "internship-form";
    }

    @PostMapping("/save")
    public String saveInternship(
            @ModelAttribute("internship") InternshipApplication formInternship,
            Authentication authentication
    ) {
        AppUser user = getCurrentUser(authentication);

        InternshipApplication internshipToSave;

        if (formInternship.getId() != null) {
            internshipToSave = internshipRepository.findByIdAndUser(formInternship.getId(), user)
                    .orElseThrow(() -> new RuntimeException("Internship not found"));
        } else {
            internshipToSave = new InternshipApplication();
            internshipToSave.setUser(user);
        }

        internshipToSave.setCompanyName(formInternship.getCompanyName());
        internshipToSave.setRole(formInternship.getRole());
        internshipToSave.setLocation(formInternship.getLocation());
        internshipToSave.setStipend(formInternship.getStipend());
        internshipToSave.setApplicationDate(formInternship.getApplicationDate());
        internshipToSave.setDeadline(formInternship.getDeadline());
        internshipToSave.setStatus(formInternship.getStatus());
        internshipToSave.setApplicationLink(formInternship.getApplicationLink());
        internshipToSave.setNotes(formInternship.getNotes());

        internshipRepository.save(internshipToSave);

        return "redirect:/internships";
    }

    @GetMapping("/edit/{id}")
    public String editInternship(@PathVariable Long id, Authentication authentication, Model model) {
        AppUser user = getCurrentUser(authentication);
        InternshipApplication internship = internshipRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        model.addAttribute("internship", internship);
        model.addAttribute("pageTitle", "Edit Internship");
        return "internship-form";
    }

    @PostMapping("/delete/{id}")
    public String deleteInternship(@PathVariable Long id, Authentication authentication) {
        AppUser user = getCurrentUser(authentication);
        InternshipApplication internship = internshipRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        internshipRepository.delete(internship);
        return "redirect:/internships";
    }

    private AppUser getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
    }
}
