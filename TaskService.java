package com.onboarding.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@SpringBootApplication
public class TaskServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}

@RestController
@RequestMapping("/tasks")
class TaskBoardController {

    @Autowired
    private TaskBoardRepository repository;

    @PostMapping("/create")
    public ResponseEntity<TaskBoard> createBoard(@RequestBody TaskBoard board) {
        return new ResponseEntity<>(repository.save(board), HttpStatus.CREATED);
    }

    @PostMapping("/{boardId}/task")
    public ResponseEntity<TaskBoard> addTask(@PathVariable String boardId, @RequestBody Task task) {
        Optional<TaskBoard> opt = repository.findById(boardId);
        if (opt.isPresent()) {
            TaskBoard board = opt.get();
            board.getTasks().add(task);
            return new ResponseEntity<>(repository.save(board), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{boardId}/task/{taskId}/status")
    public ResponseEntity<TaskBoard> updateStatus(@PathVariable String boardId, @PathVariable String taskId, @RequestBody String status) {
        Optional<TaskBoard> opt = repository.findById(boardId);
        if (opt.isPresent()) {
            TaskBoard board = opt.get();
            for (Task task : board.getTasks()) {
                if (task.getId().equals(taskId)) {
                    task.setStatus(status);
                    break;
                }
            }
            return new ResponseEntity<>(repository.save(board), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<TaskBoard> getBoard(@PathVariable String boardId) {
        return repository.findById(boardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taskboards")
class TaskBoard {
    @Id
    private String id;
    private String candidateId;
    private List<Task> tasks = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}

class Task {
    private String id;
    private String title;
    private String description;
    private String status; // e.g., TODO, IN_PROGRESS, DONE

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

import org.springframework.data.mongodb.repository.MongoRepository;

interface TaskBoardRepository extends MongoRepository<TaskBoard, String> {
}
