package com.encora.breakable_toy.controller;

import com.encora.breakable_toy.entity.Task;
import com.encora.breakable_toy.service.TaskService;
import com.encora.breakable_toy.utils.AverageTime;
import com.encora.breakable_toy.utils.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/todos")
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer priority) {

        Pages pages = taskService.getPages();

        // Validar que la página solicitada sea mayor o igual a 1
        if (page < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Page number must be greater than or equal to 1"));
        }

        List<Task> tasks;

        // Ordenar por prioridad y fecha de vencimiento
        if ("priority".equalsIgnoreCase(sortBy) && "dueDate".equalsIgnoreCase(sortOrder)) {
            tasks = taskService.getAllTasksSortedByPriorityAndDueDate();
        } else if ("priority".equalsIgnoreCase(sortBy)) {
            tasks = taskService.getAllTasksSortedByPriority();
        } else if ("dueDate".equalsIgnoreCase(sortBy)) {
            tasks = taskService.getAllTasksSortedByDueDate();
        } else {
            tasks = taskService.getAllTasks();
        }

        // Aplicar filtros
        if (done != null) {
            tasks = tasks.stream()
                    .filter(task -> task.isDone() == done)
                    .collect(Collectors.toList());
        }

        if (name != null && !name.isEmpty()) {
            tasks = tasks.stream()
                    .filter(task -> task.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (priority != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getPriority() == priority)
                    .collect(Collectors.toList());
        }

        // Validar si no hay tareas después de aplicar los filtros
        if (tasks.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No tasks found"));
        }
        // Actualizar la página actual en el componente Pages
        pages.setActualPage(page);

        // Obtener el número total de páginas
        int totalPages = pages.getTotalPages();

        // Validar que la página solicitada no exceda el número total de páginas
        if (page > totalPages) {
            return ResponseEntity.badRequest().body(Map.of("error", "Page number exceeds total pages"));
        }

        // Calcular el índice de inicio y fin para la paginación
        int start = (page - 1) * Pages.TASKS_PER_PAGE;
        int end = Math.min(start + Pages.TASKS_PER_PAGE, tasks.size());

        // Obtener la sublista de tareas para la página actual
        List<Task> paginatedTasks = tasks.subList(start, end);

        // Crear la respuesta con la lista de tareas y la información de paginación
        Map<String, Object> response = new HashMap<>();
        response.put("tasks", paginatedTasks);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/todos")
    public ResponseEntity<Map<String, String>> createTask(@RequestBody Task task) {
        // Validar que el campo 'done' no esté presente en la solicitud
        if (task.isDone()) {
            return ResponseEntity.badRequest().body(Map.of("error", "The 'done' field cannot be set when creating a new task"));
        }

        // Validar que los campos obligatorios estén presentes
        if (task.getName() == null || task.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Task name cannot be null or empty"));
        }
        if (task.getPriority() < 0 || task.getPriority() > 2) {
            return ResponseEntity.badRequest().body(Map.of("error", "Task priority must be between 0 and 2"));
        }

        // Crear la tarea
        Task createdTask = taskService.createTask(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();
        return ResponseEntity.created(location).body(Map.of("id", createdTask.getId().toString()));
    }

    @PostMapping("/todoslist")
    public ResponseEntity<Map<String, Object>> createTasks(@RequestBody List<Task> tasks) {
        List<Task> createdTasks = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (Task task : tasks) {
            try {
                // Validar que el campo 'done' no esté presente en la solicitud
                if (task.isDone()) {
                    throw new IllegalArgumentException("The 'done' field cannot be set when creating a new task");
                }

                // Validar que los campos obligatorios estén presentes
                if (task.getName() == null || task.getName().isEmpty()) {
                    throw new IllegalArgumentException("Task name cannot be null or empty");
                }
                if (task.getPriority() < 0 || task.getPriority() > 2) {
                    throw new IllegalArgumentException("Task priority must be between 0 and 2");
                }

                // Crear la tarea
                Task createdTask = taskService.createTask(task);
                createdTasks.add(createdTask);
            } catch (IllegalArgumentException e) {
                errors.add("Error creating task: " + task.getName() + " - " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("createdTasks", createdTasks));
    }


    @PutMapping("/todos/{id}")
    public ResponseEntity<Map<String, String>> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        try {
            // Validar que el campo 'done' no se pueda modificar
            if (updatedTask.isDone()) {
                return ResponseEntity.badRequest().body(Map.of("error", "The 'done' field cannot be modified"));
            }

            // Validar que los campos obligatorios estén presentes
            if (updatedTask.getName() == null || updatedTask.getName().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Task name cannot be null or empty"));
            }
            if (updatedTask.getPriority() < 0 || updatedTask.getPriority() > 2) {
                return ResponseEntity.badRequest().body(Map.of("error", "Task priority must be between 0 and 2"));
            }
            if (updatedTask.getDueDate() != null && updatedTask.getDueDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Due date cannot be in the past"));
            }

            Task task = taskService.updateTask(id, updatedTask);
            return ResponseEntity.ok(Map.of("message", "Task updated successfully", "id", task.getId().toString()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/todos/{id}/done")
    public ResponseEntity<Map<String, String>> markTaskAsDone(@PathVariable Long id) {
        try {
            Task task = taskService.markAsDone(id);
            return ResponseEntity.ok(Map.of("message", "Task marked as done", "id", task.getId().toString()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Endpoint para marcar una tarea como no completada
    @PutMapping("/todos/{id}/undone")
    public ResponseEntity<Map<String, String>> markTaskAsUndone(@PathVariable Long id) {
        try {
            Task task = taskService.markTaskAsUndone(id);
            if (!task.isDone()) {
                return ResponseEntity.ok(Map.of("message", "Task marked as undone", "id", task.getId().toString()));
            } else {
                return ResponseEntity.ok(Map.of("message", "Task is already undone", "id", task.getId().toString()));
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/todos/{id}/delete")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        // Llamar al servicio para eliminar la tarea
        taskService.deleteTask(id);
        // Devolver una respuesta sin contenido (204 No Content)
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/avg-time")
    public ResponseEntity<Map<String, Long>> getAverageTime() {
        AverageTime averageTime = taskService.getAverageTimes();
        Map<String, Long> response = new HashMap<>();
        response.put("totalTime", averageTime.getTotalTimeInMinutes());
        response.put("highTime", averageTime.getHighTimeInMinutes());
        response.put("mediumTime", averageTime.getMediumTimeInMinutes());
        response.put("lowTime", averageTime.getLowTimeInMinutes());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/pages")
    public ResponseEntity<Pages> getPages() {
        Pages pages = taskService.getPages();
        return ResponseEntity.ok(pages);
    }
}
