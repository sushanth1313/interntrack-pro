package com.riya.interntrack.repository;

import com.riya.interntrack.model.AppUser;
import com.riya.interntrack.model.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InternshipRepository extends JpaRepository<InternshipApplication, Long> {

    List<InternshipApplication> findByUserOrderByDeadlineAsc(AppUser user);

    Optional<InternshipApplication> findByIdAndUser(Long id, AppUser user);

    long countByUserAndStatus(AppUser user, String status);

    @Query("""
        SELECT i FROM InternshipApplication i
        WHERE i.user = :user
        AND (
            :keyword IS NULL OR :keyword = ''
            OR LOWER(i.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(i.role) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (:status IS NULL OR :status = '' OR i.status = :status)
        ORDER BY i.deadline ASC
    """)
    List<InternshipApplication> searchForUser(
            @Param("user") AppUser user,
            @Param("keyword") String keyword,
            @Param("status") String status
    );
}
