package com.pollify.pollify.controller;

import com.pollify.pollify.model.Complaint;
import com.pollify.pollify.model.Complaint.ComplaintStatus;
import com.pollify.pollify.model.User;
import com.pollify.pollify.repository.UserRepository;
import com.pollify.pollify.servis.ComplaintService;
import com.pollify.pollify.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        userRepository.findById(userId).ifPresent(complaint::setUser);
        return ResponseEntity.ok(complaintService.save(complaint));
    }

    // ✅ 2. Kullanıcı kendi şikayetlerini listeler
    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> getMyComplaints() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(complaintService.findByUserId(userId));
    }

    // ✅ 3. Admin tüm şikayetleri görür
    @GetMapping
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.findAll());
    }

    //  4. Admin şikayeti günceller (status veya adminNot değiştirir)
    @PutMapping("/admin-update/{id}")
    public ResponseEntity<Complaint> updateComplaintByAdmin(
            @PathVariable Long id,
            @RequestBody Complaint updatedData
    ) {
        return complaintService.findById(id)
                .map(existing -> {
                    existing.setStatus(updatedData.getStatus());
                    existing.setAdminNote(updatedData.getAdminNote());
                    return ResponseEntity.ok(complaintService.updateComplaint(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 5. Kullanıcı kendi şikayetini günceller (subject, description)
    @PutMapping("/user-update/{id}")
    public ResponseEntity<Complaint> updateComplaintByUser(
            @PathVariable Long id,
            @RequestBody Complaint updatedData
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        return complaintService.findById(id)
                .filter(c -> c.getUser().getId().equals(userId))
                .map(existing -> {
                    existing.setSubject(updatedData.getSubject());
                    existing.setDescription(updatedData.getDescription());
                    return ResponseEntity.ok(complaintService.updateComplaint(existing));
                })
                .orElse(ResponseEntity.status(403).build());
    }

    // ✅ 6. Şikayet sil (Admin veya kullanıcı)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        Optional<Complaint> optionalComplaint = complaintService.findById(id);

        if (optionalComplaint.isPresent()) {
            complaintService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
