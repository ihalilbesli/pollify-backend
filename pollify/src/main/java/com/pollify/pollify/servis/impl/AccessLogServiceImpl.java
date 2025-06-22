package com.pollify.pollify.servis.impl;

import com.pollify.pollify.model.AccessLog;
import com.pollify.pollify.repository.AccessLogRepository;
import com.pollify.pollify.servis.AccessLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Override
    public void saveLog(AccessLog log) {
        accessLogRepository.save(log);
    }

    @Override
    public void deleteLogsBefore(LocalDateTime before) {
        accessLogRepository.deleteByTimestampBefore(before);
    }

    @Override
    public List<AccessLog> getAllLogs() {
        return accessLogRepository.findAll();
    }

    @Override
    public List<AccessLog> getLogsByEmail(String email) {
        return accessLogRepository.findByUserEmail(email);
    }

    @Override
    public List<AccessLog> getLogsByRole(String role) {
        return accessLogRepository.findByRole(role);
    }

    @Override
    public List<AccessLog> getLogsByStatus(String status) {
        return accessLogRepository.findByStatus(status);
    }

    @Override
    public List<AccessLog> getLogsByPeriod(String period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after;
        switch (period.toLowerCase()) {
            case "day" -> after = now.minusDays(1);
            case "week" -> after = now.minusWeeks(1);
            case "month" -> after = now.minusMonths(1);
            case "year" -> after = now.minusYears(1);
            default -> throw new RuntimeException("Geçersiz zaman filtresi: " + period);
        }
        return accessLogRepository.findByTimestampAfter(after);
    }
}
