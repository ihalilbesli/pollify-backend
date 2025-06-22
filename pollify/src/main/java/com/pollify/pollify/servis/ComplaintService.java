package com.pollify.pollify.servis;

import com.pollify.pollify.model.Complaint;

import java.util.List;
import java.util.Optional;

public interface ComplaintService {
    Complaint save(Complaint complaint);

    Optional<Complaint> findById(Long id);

    List<Complaint> findAll();

    void deleteById(Long id);

    Complaint updateComplaint(Complaint complaint);

    List<Complaint> findByUserId(Long userId);

}
