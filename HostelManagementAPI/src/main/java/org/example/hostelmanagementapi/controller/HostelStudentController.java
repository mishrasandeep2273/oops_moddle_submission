package org.example.hostelmanagementapi.controller;

import org.example.hostelmanagementapi.entity.HostelStudent;
import org.example.hostelmanagementapi.repository.HostelStudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class HostelStudentController {

    @Autowired
    private HostelStudentRepository repository;

    // Add Hostel Student Record
    @PostMapping
    public HostelStudent addStudent(
            @RequestBody HostelStudent student) {

        return repository.save(student);
    }

    // Retrieve All Students
    @GetMapping
    public List<HostelStudent> getAllStudents() {

        return repository.findAll();
    }

    // Retrieve Student by ID
    @GetMapping("/{id}")
    public ResponseEntity<HostelStudent> getStudentById(
            @PathVariable int id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // Update Hostel Student Details
    @PutMapping("/{id}")
    public ResponseEntity<HostelStudent> updateStudent(
            @PathVariable int id,
            @RequestBody HostelStudent updatedStudent) {

        return repository.findById(id)
                .map(student -> {

                    student.setStudentName(
                            updatedStudent.getStudentName());

                    student.setRollNumber(
                            updatedStudent.getRollNumber());

                    student.setHostelName(
                            updatedStudent.getHostelName());

                    student.setRoomNumber(
                            updatedStudent.getRoomNumber());

                    student.setFee(
                            updatedStudent.getFee());

                    HostelStudent savedStudent =
                            repository.save(student);

                    return ResponseEntity.ok(savedStudent);
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // Delete Hostel Student Record
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable int id) {

        if (repository.existsById(id)) {

            repository.deleteById(id);

            return ResponseEntity.ok(
                    "Student deleted successfully!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Student record not found!");
    }
}
