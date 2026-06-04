package com.marketplace.todomeetusvr.service;

import com.marketplace.todomeetusvr.dto.TaskRequest;
import com.marketplace.todomeetusvr.dto.TaskResponse;
import com.marketplace.todomeetusvr.model.Task;
import com.marketplace.todomeetusvr.model.User;
import com.marketplace.todomeetusvr.repository.TaskRepository;
import com.marketplace.todomeetusvr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }

    public TaskResponse createTask(TaskRequest request, String email) {
        User user = getUserByEmail(email);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .user(user)
                .build();

        return TaskResponse.fromTask(taskRepository.save(task));
    }

    public List<TaskResponse> getAllTasks(String email) {
        User user = getUserByEmail(email);
        return taskRepository.findAllByUserId(user.getId())
                .stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long taskId, TaskRequest request, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have access to this task");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        return TaskResponse.fromTask(taskRepository.save(task));
    }

    public void deleteTask(Long taskId, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have access to this task");
        }

        taskRepository.delete(task);
    }
}