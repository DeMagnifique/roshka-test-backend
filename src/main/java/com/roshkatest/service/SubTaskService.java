package com.roshkatest.service;

import com.roshkatest.dto.ApiResponse;
import com.roshkatest.dto.SubTaskCreateRequest;
import com.roshkatest.entity.SubTask;
import com.roshkatest.entity.Task;
import com.roshkatest.repository.SubTaskRepository;
import com.roshkatest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubTaskService {

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    public ApiResponse<Page<SubTask>> getSubTasksByTaskId(Long taskId, int page, int limit, String sortBy, String order) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = new PageRequest(page, limit, new Sort(direction, sortBy));
        
        Page<SubTask> subTasks = subTaskRepository.findByTaskId(taskId, pageable);
        
        ApiResponse.MetaData meta = new ApiResponse.MetaData(
            subTasks.getTotalElements(),
            subTasks.getNumber(),
            subTasks.getTotalPages()
        );
        
        return new ApiResponse<>(subTasks, meta);
    }

    public SubTask createSubTask(Long taskId, SubTaskCreateRequest request) {
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new RuntimeException("Task no fue encontrada");
        }
        
        SubTask subTask = new SubTask();
        subTask.setTitle(request.getTitle());
        subTask.setDescription(request.getDescription());
        subTask.setTask(task);
        
        task.setUpdatedAt(new java.util.Date());
        taskRepository.save(task);
        
        return subTaskRepository.save(subTask);
    }

    public Optional<SubTask> getSubTaskById(Long taskId, Long subTaskId) {
        SubTask subTask = subTaskRepository.findOne(subTaskId);
        if (subTask != null && subTask.getTask().getId().equals(taskId)) {
            return Optional.of(subTask);
        }
        return Optional.empty();
    }

    public SubTask updateSubTask(Long taskId, Long subTaskId, SubTaskCreateRequest request) {
        SubTask subTask = subTaskRepository.findOne(subTaskId);
        if (subTask == null || !subTask.getTask().getId().equals(taskId)) {
            throw new RuntimeException("SubTask no fue encontrada");
        }
        
        subTask.setTitle(request.getTitle());
        subTask.setDescription(request.getDescription());
        
        // Actualizar el updatedAt de la tarea padre
        Task task = subTask.getTask();
        task.setUpdatedAt(new java.util.Date());
        taskRepository.save(task);
        
        return subTaskRepository.save(subTask);
    }

    public void deleteSubTask(Long taskId, Long subTaskId) {
        SubTask subTask = subTaskRepository.findOne(subTaskId);
        if (subTask == null || !subTask.getTask().getId().equals(taskId)) {
            throw new RuntimeException("SubTask no fue encontrada");
        }
        
        Task task = subTask.getTask();
        task.setUpdatedAt(new java.util.Date());
        taskRepository.save(task);
        
        subTaskRepository.delete(subTask);
    }
}
