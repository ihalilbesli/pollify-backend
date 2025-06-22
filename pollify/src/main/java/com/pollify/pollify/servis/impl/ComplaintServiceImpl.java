package com.pollify.pollify.servis.impl;

import com.pollify.pollify.model.Complaint;
import com.pollify.pollify.repository.ComplaintRepository;
import com.pollify.pollify.servis.ComplaintService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Override
    public Complaint save(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    @Override
    public Optional<Complaint> findById(Long id) {
        return complaintRepository.findById(id);
    }

    @Override
    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        complaintRepository.deleteById(id);
    }

    @Override
    public Complaint updateComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);  // save aynı zamanda update olarak da çalışır
    }

    @Override
    public List<Complaint> findByUserId(Long userId) {
        return complaintRepository.findByUserId(userId);
    }
}
