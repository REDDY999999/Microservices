package com.onboarding.candidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@SpringBootApplication
public class CandidateServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandidateServiceApplication.class, args);
    }
}

@RestController
@RequestMapping("/candidates")
class CandidateController {

    @Autowired
    private CandidateRepository repository;

    @PostMapping
    public ResponseEntity<Candidate> registerCandidate(@RequestBody Candidate candidate) {
        candidate.setStatus("REGISTERED");
        return new ResponseEntity<>(repository.save(candidate), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<String> uploadResume(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        Optional<Candidate> opt = repository.findById(id);
        if (opt.isPresent()) {
            Candidate c = opt.get();
            c.setResumeFilename(file.getOriginalFilename()); // Store filename for now (mocking file storage)
            repository.save(c);
            return ResponseEntity.ok("Resume uploaded successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidate(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "candidates")
class Candidate {
    @Id
    private String id;
    private String name;
    private String email;
    private String resumeFilename;
    private String status;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getResumeFilename() { return resumeFilename; }
    public void setResumeFilename(String resumeFilename) { this.resumeFilename = resumeFilename; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

import org.springframework.data.mongodb.repository.MongoRepository;

interface CandidateRepository extends MongoRepository<Candidate, String> {
}
