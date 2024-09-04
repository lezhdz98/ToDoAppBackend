package com.encora.breakable_toy.controller;


import com.encora.breakable_toy.entitiy.Task;
import com.encora.breakable_toy.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/task")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService ;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task newTask){
        return taskService.create(newTask);
    }

    @PutMapping("/")
    public Task update(@RequestBody Task newTask){
        return taskService.update(newTask);
    }

    @GetMapping(value = "/")
    public List<Task> list(){
        return taskService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Task getById(@PathVariable(name = "id") Long id){
        return taskService.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") Long id){
        taskService.delete(id);
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response)
            throws IOException {
        logger.error("The request contains illegal arguments: {}", e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
