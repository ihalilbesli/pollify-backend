package com.pollify.pollify.controller;

import com.pollify.pollify.model.AccessLog;
import com.pollify.pollify.servis.AccessLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pollify/logs")
public class AccessLogController {

    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    //  Tüm logları getir
    @GetMapping
    public ResponseEntity<List<AccessLog>> getAllLogs() {
        return ResponseEntity.ok(accessLogService.getAllLogs());
    }

    //  Kullanıcı email’e göre logları getir
    @GetMapping("/email/{email}")
    public ResponseEntity<List<AccessLog>> getLogsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(accessLogService.getLogsByEmail(email));
    }

    //  Role göre logları getir
    @GetMapping("/role/{role}")
    public ResponseEntity<List<AccessLog>> getLogsByRole(@PathVariable String role) {
        return ResponseEntity.ok(accessLogService.getLogsByRole(role));
    }

    //  Duruma göre logları getir (BAŞARILI, HATA)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccessLog>> getLogsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(accessLogService.getLogsByStatus(status));
    }

    //  Zaman filtresine göre logları getir (day, week, month, year)
    @GetMapping("/period")
    public ResponseEntity<List<AccessLog>> getLogsByPeriod(@RequestParam String period) {
        return ResponseEntity.ok(accessLogService.getLogsByPeriod(period));
    }
}
