package com.encora.breakable_toy.service;


import com.encora.breakable_toy.entitiy.Task;
import com.encora.breakable_toy.repository.TaskRepository;
import com.encora.breakable_toy.utils.TaskValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(Task newTask) {
        TaskValidator.validate(newTask);
        Task task = taskRepository.save(newTask);
        //logger.debug("Person created: {}", task);
        return task;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task update(Task newTask) {
        TaskValidator.validate(newTask);
        if(newTask.getId() < 1){
            throw new IllegalArgumentException("Can't update task with id ="+newTask.getId());
        }
        Task task;
        task = taskRepository.findById(newTask.getId())
                .orElseThrow(() -> new IllegalArgumentException("The task can't be updated."));

        task.setName(newTask.getName());
        task.setCheckDone(newTask.getCheckDone());
        task.setPriority(newTask.getPriority());
        //task.setCreationDate(newTask.getCreationDate());
        task.setDoneDate(newTask.getDoneDate());
        task.setDueDate(newTask.getDueDate());
        task = taskRepository.save(task);

        //logger.debug("Task updated: {}", task);
        return task;
    }

    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't delete task with id ="+id));
        //logger.debug("Deleting task: {}", task);
        taskRepository.delete(task);
    }

    public Task findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no task with id ="+id));
        //logger.debug("Task found: {}", task);
        return task;
    }

}
