package com.encora.breakable_toy.utils;

import com.encora.breakable_toy.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskValidator {

    public void validate(Task task) {
        if (task.getName() == null || task.getName().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        if (task.getName().length() > 120) {
            throw new IllegalArgumentException("Task name cannot exceed 120 characters");
        }
        if (task.getPriority() < 0 || task.getPriority() > 2) {
            throw new IllegalArgumentException("Task priority must be 0 = Low, 1 = Medium or 2 = High");
        }
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }
    }
}
