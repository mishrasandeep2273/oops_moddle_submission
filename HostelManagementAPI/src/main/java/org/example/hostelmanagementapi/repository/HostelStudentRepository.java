package org.example.hostelmanagementapi.repository;

import org.example.hostelmanagementapi.entity.HostelStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelStudentRepository
        extends JpaRepository<HostelStudent, Integer> {

}
