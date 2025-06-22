package com.pollify.pollify.controller;

import com.pollify.pollify.model.Complaint;
import com.pollify.pollify.model.User;
import com.pollify.pollify.repository.UserRepository;
import com.pollify.pollify.servis.ComplaintService;
import com.pollify.pollify.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pollify/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final UserRepository userRepository;

    public ComplaintController(ComplaintService complaintService, UserRepository userRepository) {
        this.complaintService = complaintService;
        this.userRepository = userRepository;
    }

    // ✅ 1. Kullanıcı şikayet oluşturur
    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@RequestBody Complaint complaint) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) throw new RuntimeException("Giriş yapılmamış");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        complaint.setUser(user);
        Complaint saved = complaintService.save(complaint);
        return ResponseEntity.ok(saved);
    }

    // ✅ 2. Kullanıcı kendi şikayetlerini listeler
    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> getMyComplaints() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) throw new RuntimeException("Giriş yapılmamış");

        return ResponseEntity.ok(complaintService.findByUserId(userId));
    }

    // ✅ 3. Admin tüm şikayetleri görür
    @GetMapping
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.findAll());
    }

    // ✅ 4. Admin şikayeti günceller (status veya adminNote değiştirir)
    @PutMapping("/admin-update/{id}")
    public ResponseEntity<Complaint> updateComplaintByAdmin(
            @PathVariable Long id,
            @RequestBody Complaint updatedData
    ) {
        Complaint existing = complaintService.findById(id)
                .orElseThrow(() -> new RuntimeException("Şikayet bulunamadı (id: " + id + ")"));

        existing.setStatus(updatedData.getStatus());
        existing.setAdminNote(updatedData.getAdminNote());

        Complaint updated = complaintService.updateComplaint(existing);
        return ResponseEntity.ok(updated);
    }

    // ✅ 5. Kullanıcı kendi şikayetini günceller (subject, description)
    @PutMapping("/user-update/{id}")
    public ResponseEntity<Complaint> updateComplaintByUser(
            @PathVariable Long id,
            @RequestBody Complaint updatedData
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) throw new RuntimeException("Giriş yapılmamış");

        Complaint existing = complaintService.findById(id)
                .orElseThrow(() -> new RuntimeException("Şikayet bulunamadı (id: " + id + ")"));

        if (!existing.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu şikayet size ait değil.");
        }

        existing.setSubject(updatedData.getSubject());
        existing.setDescription(updatedData.getDescription());

        Complaint updated = complaintService.updateComplaint(existing);
        return ResponseEntity.ok(updated);
    }

    // ✅ 6. Şikayet sil (Admin veya kullanıcı)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        Complaint complaint = complaintService.findById(id)
                .orElseThrow(() -> new RuntimeException("Şikayet bulunamadı (id: " + id + ")"));

        complaintService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
