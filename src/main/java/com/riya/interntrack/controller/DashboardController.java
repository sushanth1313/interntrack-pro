package com.riya.interntrack.controller;

import com.riya.interntrack.model.AppUser;
import com.riya.interntrack.model.InternshipApplication;
import com.riya.interntrack.repository.InternshipRepository;
import com.riya.interntrack.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;

    public DashboardController(UserRepository userRepository, InternshipRepository internshipRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        AppUser user = getCurrentUser(authentication);
        List<InternshipApplication> internships = internshipRepository.findByUserOrderByDeadlineAsc(user);

        long total = internships.size();
        long applied = countStatus(internships, "APPLIED");
        long interview = countStatus(internships, "INTERVIEW");
        long selected = countStatus(internships, "SELECTED");
        long rejected = countStatus(internships, "REJECTED");
        long pending = countStatus(internships, "PENDING");

        List<InternshipApplication> upcoming = internships.stream()
                .filter(i -> i.getDeadline() != null)
                .filter(i -> !i.getDeadline().isBefore(LocalDate.now()))
                .limit(5)
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("total", total);
        model.addAttribute("applied", applied);
        model.addAttribute("interview", interview);
        model.addAttribute("selected", selected);
        model.addAttribute("rejected", rejected);
        model.addAttribute("pending", pending);
        model.addAttribute("upcoming", upcoming);
        model.addAttribute("recent", internships.stream().limit(6).toList());

        return "dashboard";
    }

    @GetMapping("/analytics")
    public String analytics(Authentication authentication, Model model) {
        AppUser user = getCurrentUser(authentication);
        List<InternshipApplication> internships = internshipRepository.findByUserOrderByDeadlineAsc(user);

        long total = internships.size();
        long selected = countStatus(internships, "SELECTED");
        long rejected = countStatus(internships, "REJECTED");
        long active = total - rejected - selected;
        int successRate = total == 0 ? 0 : (int) Math.round((selected * 100.0) / total);
        int rejectionRate = total == 0 ? 0 : (int) Math.round((rejected * 100.0) / total);

        model.addAttribute("user", user);
        model.addAttribute("total", total);
        model.addAttribute("active", active);
        model.addAttribute("selected", selected);
        model.addAttribute("rejected", rejected);
        model.addAttribute("successRate", successRate);
        model.addAttribute("rejectionRate", rejectionRate);
        model.addAttribute("applied", countStatus(internships, "APPLIED"));
        model.addAttribute("interview", countStatus(internships, "INTERVIEW"));
        model.addAttribute("pending", countStatus(internships, "PENDING"));

        return "analytics";
    }

    private long countStatus(List<InternshipApplication> internships, String status) {
        return internships.stream()
                .filter(i -> status.equalsIgnoreCase(i.getStatus()))
                .count();
    }

    private AppUser getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
    }
}
