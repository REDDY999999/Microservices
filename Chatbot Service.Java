package com.onboarding.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootApplication
public class ChatbotServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatbotServiceApplication.class, args);
    }
}

@RestController
@RequestMapping("/chatbot")
class ChatbotController {

    private final ResumeScoringService scoringService;

    public ChatbotController(ResumeScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResult> evaluateResume(@RequestBody ResumeInput input) {
        EvaluationResult result = scoringService.evaluate(input);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

class ResumeInput {
    private String candidateId;
    private String resumeText;

    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }
}

class EvaluationResult {
    private String candidateId;
    private double score;
    private boolean passed;
    private List<String> feedback;

    public EvaluationResult(String candidateId, double score, boolean passed, List<String> feedback) {
        this.candidateId = candidateId;
        this.score = score;
        this.passed = passed;
        this.feedback = feedback;
    }

    public String getCandidateId() { return candidateId; }
    public double getScore() { return score; }
    public boolean isPassed() { return passed; }
    public List<String> getFeedback() { return feedback; }
}

@Service
class ResumeScoringService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public EvaluationResult evaluate(ResumeInput input) {
        // Simulate RAG or LLM scoring logic here (mocked for now)
        double score = Math.random() * 100;
        boolean passed = score >= 60;
        List<String> feedback = passed ? List.of("Strong communication", "Good technical background")
                                       : List.of("Lacks relevant experience", "Insufficient technical depth");

        return new EvaluationResult(input.getCandidateId(), score, passed, feedback);
    }
}

