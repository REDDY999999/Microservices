package com.onboarding.document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

// Main Application
@SpringBootApplication
public class DocumentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocumentServiceApplication.class, args);
    }
}

// Controller
@RestController
@RequestMapping("/documents")
class DocumentController {

    @Autowired
    private DocumentRepository repository;

    @PostMapping("/{candidateId}")
    public ResponseEntity<String> uploadDocument(
            @PathVariable String candidateId,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) {

        OnboardingDocument doc = new OnboardingDocument();
        doc.setCandidateId(candidateId);
        doc.setType(type);
        doc.setFilename(file.getOriginalFilename()); // mock file storage
        doc.setUploadDate(new Date());
        doc.setVersion(UUID.randomUUID().toString());

        repository.save(doc);
        return ResponseEntity.status(HttpStatus.CREATED).body("Document uploaded successfully.");
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<List<OnboardingDocument>> getDocuments(@PathVariable String candidateId) {
        return ResponseEntity.ok(repository.findByCandidateId(candidateId));
    }
}

// Entity
@Document(collection = "documents")
class OnboardingDocument {
    @Id
    private String id;
    private String candidateId;
    private String type;
    private String filename;
    private Date uploadDate;
    private String version;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}

// Repository
interface DocumentRepository extends MongoRepository<OnboardingDocument, String> {
    List<OnboardingDocument> findByCandidateId(String candidateId);
}

