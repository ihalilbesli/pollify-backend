package com.pollify.pollify.repository;

import com.pollify.pollify.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByUserEmail(String email);
    List<AccessLog> findByRole(String role);
    List<AccessLog> findByStatus(String status);
    List<AccessLog> findByTimestampAfter(LocalDateTime after);
}