package com.pollify.pollify.repository;

import com.pollify.pollify.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {

    List<Complaint> findByUserId(Long userId);


    List<Complaint> findByStatus(Complaint.ComplaintStatus status);


    List<Complaint> findByUserIdAndStatus(Long userId, Complaint.ComplaintStatus status);
}
