package com.riya.interntrack.config;

import com.riya.interntrack.model.AppUser;
import com.riya.interntrack.model.InternshipApplication;
import com.riya.interntrack.repository.InternshipRepository;
import com.riya.interntrack.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DemoDataLoader {

    @Bean
    CommandLineRunner seedDemoData(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepository.existsByEmail("demo@interntrack.com")) {
                AppUser demo = new AppUser(
                        "Demo Student",
                        "demo@interntrack.com",
                        passwordEncoder.encode("demo123")
                );
                userRepository.save(demo);

                InternshipApplication google = new InternshipApplication();
                google.setCompanyName("Google");
                google.setRole("Software Engineering Intern");
                google.setLocation("Bengaluru");
                google.setStipend("₹80,000/month");
                google.setApplicationDate(LocalDate.now().minusDays(8));
                google.setDeadline(LocalDate.now().plusDays(14));
                google.setStatus("INTERVIEW");
                google.setApplicationLink("https://careers.google.com");
                google.setNotes("Prepare DSA, OS, DBMS, and resume projects.");
                google.setUser(demo);
                internshipRepository.save(google);

                InternshipApplication microsoft = new InternshipApplication();
                microsoft.setCompanyName("Microsoft");
                microsoft.setRole("AI Intern");
                microsoft.setLocation("Hyderabad");
                microsoft.setStipend("₹75,000/month");
                microsoft.setApplicationDate(LocalDate.now().minusDays(4));
                microsoft.setDeadline(LocalDate.now().plusDays(5));
                microsoft.setStatus("APPLIED");
                microsoft.setApplicationLink("https://careers.microsoft.com");
                microsoft.setNotes("Need to update GitHub portfolio before follow-up.");
                microsoft.setUser(demo);
                internshipRepository.save(microsoft);

                InternshipApplication startup = new InternshipApplication();
                startup.setCompanyName("AIML Startup");
                startup.setRole("Machine Learning Intern");
                startup.setLocation("Remote");
                startup.setStipend("₹20,000/month");
                startup.setApplicationDate(LocalDate.now().minusDays(15));
                startup.setDeadline(LocalDate.now().minusDays(1));
                startup.setStatus("REJECTED");
                startup.setApplicationLink("https://example.com");
                startup.setNotes("Rejected. Improve model deployment project.");
                startup.setUser(demo);
                internshipRepository.save(startup);
            }
        };
    }
}
