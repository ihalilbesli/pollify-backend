package com.pollify.pollify.servis;


import com.pollify.pollify.model.AccessLog;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogService {

    void saveLog(AccessLog log);

    void deleteLogsBefore(LocalDateTime before);

    List<AccessLog> getAllLogs();

    List<AccessLog> getLogsByEmail(String email);

    List<AccessLog> getLogsByRole(String role);

    List<AccessLog> getLogsByStatus(String status);

    List<AccessLog> getLogsByPeriod(String period); // day, week, month, year
}
