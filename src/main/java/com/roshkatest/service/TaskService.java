package com.roshkatest.service;

import com.roshkatest.dto.ApiResponse;
import com.roshkatest.dto.TaskCreateRequest;
import com.roshkatest.entity.Task;
import com.roshkatest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public ApiResponse<Page<Task>> getAllTasks(int page, int limit, String sortBy, String order, String category) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = new PageRequest(page, limit, new Sort(direction, sortBy));
        
        Page<Task> tasks;
        if (category != null && !category.isEmpty()) {
            tasks = taskRepository.findByCategoryContainingIgnoreCase(category, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        
        ApiResponse.MetaData meta = new ApiResponse.MetaData(
            tasks.getTotalElements(),
            tasks.getNumber(),
            tasks.getTotalPages()
        );
        
        return new ApiResponse<>(tasks, meta);
    }

    public Task createTask(TaskCreateRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategory(request.getCategory());
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        Task task = taskRepository.findOne(id);
        return task != null ? Optional.of(task) : Optional.empty();
    }

    public Task updateTask(Long id, TaskCreateRequest request) {
        Task task = taskRepository.findOne(id);
        if (task == null) {
            throw new RuntimeException("Task no encontrada");
        }
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategory(request.getCategory());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.delete(id);
    }
}
