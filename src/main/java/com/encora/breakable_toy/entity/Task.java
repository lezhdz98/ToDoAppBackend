package com.encora.breakable_toy.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDateTime;
import java.util.Objects;


public class Task {

    private Long id;
    private String name;
    private boolean done;
    private int priority;
    private LocalDateTime creationDate;
    private LocalDateTime doneDate;
    private LocalDateTime dueDate;

    // Constructor por defecto
    @JsonCreator
    public Task() {
        this.creationDate = LocalDateTime.now();
        this.done = false;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        if (name.length() > 120) {
            throw new IllegalArgumentException("Task name cannot exceed 120 characters");
        }
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
        if (done) {
            this.doneDate = LocalDateTime.now();
        } else {
            this.doneDate = null;
        }
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority < 0 || priority > 2) {
            throw new IllegalArgumentException("Priority must be between 0 and 2");
        }
        this.priority = priority;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return done == task.done &&
                priority == task.priority &&
                Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(creationDate, task.creationDate) &&
                Objects.equals(doneDate, task.doneDate) &&
                Objects.equals(dueDate, task.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, done, priority, creationDate, doneDate, dueDate);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", done=" + done +
                ", priority=" + priority +
                ", creationDate=" + creationDate +
                ", doneDate=" + doneDate +
                ", dueDate=" + dueDate +
                '}';
    }
}
