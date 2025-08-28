package com.roshkatest.controller;

import com.roshkatest.dto.ApiResponse;
import com.roshkatest.dto.SubTaskCreateRequest;
import com.roshkatest.dto.TaskCreateRequest;
import com.roshkatest.entity.SubTask;
import com.roshkatest.entity.Task;
import com.roshkatest.service.SubTaskService;
import com.roshkatest.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SubTaskService subTaskService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Task>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(required = false) String category) {
        
        ApiResponse<Page<Task>> response = taskService.getAllTasks(page, limit, sortBy, order, category);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskCreateRequest request) {
        Task task = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskCreateRequest request) {
        try {
            Task task = taskService.updateTask(id, request);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/subtasks")
    public ResponseEntity<ApiResponse<Page<SubTask>>> getSubTasks(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        
        ApiResponse<Page<SubTask>> response = subTaskService.getSubTasksByTaskId(id, page, limit, sortBy, order);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/subtasks")
    public ResponseEntity<SubTask> createSubTask(@PathVariable Long id, @Valid @RequestBody SubTaskCreateRequest request) {
        try {
            SubTask subTask = subTaskService.createSubTask(id, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(subTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/subtasks/{subTaskId}")
    public ResponseEntity<SubTask> getSubTaskById(@PathVariable Long id, @PathVariable Long subTaskId) {
        Optional<SubTask> subTask = subTaskService.getSubTaskById(id, subTaskId);
        return subTask.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/subtasks/{subTaskId}")
    public ResponseEntity<SubTask> updateSubTask(
            @PathVariable Long id, 
            @PathVariable Long subTaskId, 
            @Valid @RequestBody SubTaskCreateRequest request) {
        try {
            SubTask subTask = subTaskService.updateSubTask(id, subTaskId, request);
            return ResponseEntity.ok(subTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/subtasks/{subTaskId}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long id, @PathVariable Long subTaskId) {
        try {
            subTaskService.deleteSubTask(id, subTaskId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
