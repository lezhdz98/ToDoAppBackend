package com.encora.breakable_toy.service;

import com.encora.breakable_toy.entity.Task;
import com.encora.breakable_toy.utils.AverageTime;
import com.encora.breakable_toy.repository.TaskRepository;
import com.encora.breakable_toy.utils.Pages;
import com.encora.breakable_toy.utils.TaskValidator;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;
    private final AverageTime averageTime;
    private final Pages pages;

    public TaskService(TaskRepository taskRepository, TaskValidator taskValidator, AverageTime averageTime, Pages pages) {
        this.taskRepository = taskRepository;
        this.taskValidator = taskValidator;
        this.averageTime = averageTime;
        this.pages = pages;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        taskValidator.validate(task);
        Task createdTask = taskRepository.save(task);
        // Actualiza el número total de páginas después de añadir una tarea
        pages.updateTotalPages(taskRepository.findAll().size());
        return createdTask;
    }

    public Task updateTask(Long id, Task updatedTask) {
        taskValidator.validate(updatedTask);
        Optional<Task> existingTaskOpt = taskRepository.findById(id);
        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();

            // Actualizar solo los campos permitidos
            existingTask.setName(updatedTask.getName());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDueDate(updatedTask.getDueDate());

            Task savedTask = taskRepository.save(existingTask);


            return savedTask;
        } else {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
    }

    public Task markAsDone(Long id) {
        Optional<Task> existingTaskOpt = taskRepository.findById(id);
        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();
            if (!existingTask.isDone()) {
                existingTask.setDone(true);
                existingTask.setDoneDate(LocalDateTime.now());
                taskRepository.save(existingTask);

                // Actualizar el tiempo promedio
                updateAverageTime(existingTask);
            }
            return existingTask;
        } else {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
    }

    public Task markTaskAsUndone(Long id) {
        // Buscar la tarea por su ID en el repositorio
        Optional<Task> existingTaskOpt = taskRepository.findById(id);

        // Verificar si la tarea existe
        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();

            // Verificar si la tarea está marcada como completada
            if (existingTask.isDone()) {
                // Marcar la tarea como no completada
                existingTask.setDone(false);
                // Borrar la fecha de finalización de la tarea
                existingTask.setDoneDate(null);
                // Guardar los cambios en el repositorio
                taskRepository.save(existingTask);
                // Actualizar el tiempo promedio eliminando la duración de esta tarea
                removeAverageTime(existingTask);
            }
            // Devolver la tarea actualizada
            return existingTask;
        } else {
            // Lanzar una excepción si la tarea no se encuentra
            throw new NoSuchElementException("Task not found with id: " + id);
        }
    }


    public void deleteTask(Long id) {
        // Buscar la tarea por su ID en el repositorio
        Optional<Task> taskOpt = taskRepository.findById(id);

        // Verificar si la tarea existe
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();

            // Verificar si la tarea está marcada como completada
            if (task.isDone()) {
                // Actualizar el tiempo promedio eliminando la duración de esta tarea
                removeAverageTime(task);
            }

            // Eliminar la tarea del repositorio
            taskRepository.deleteById(id);

            // Actualizar el número total de páginas después de eliminar una tarea
            pages.updateTotalPages(taskRepository.findAll().size());
        } else {
            // Lanzar una excepción si la tarea no se encuentra
            throw new NoSuchElementException("Task not found with id: " + id);
        }
    }


    private void updateAverageTime(Task task) {
        Duration duration = Duration.between(task.getCreationDate(), task.getDoneDate());

        // Actualizar el tiempo total
        averageTime.addTotalDuration(duration);

        // Actualizar tiempos según la prioridad
        switch (task.getPriority()) {
            case 2:
                averageTime.addHighDuration(duration);
                break;
            case 1:
                averageTime.addMediumDuration(duration);
                break;
            case 0:
                averageTime.addLowDuration(duration);
                break;
        }
    }

    private void removeAverageTime(Task task) {
        Duration duration = Duration.between(task.getCreationDate(), task.getDoneDate());

        // Actualizar el tiempo total
        averageTime.subtractTotalDuration(duration);

        // Actualizar tiempos según la prioridad
        switch (task.getPriority()) {
            case 2:
                averageTime.subtractHighDuration(duration);
                break;
            case 1:
                averageTime.subtractMediumDuration(duration);
                break;
            case 0:
                averageTime.subtractLowDuration(duration);
                break;
        }
    }

    public AverageTime getAverageTimes() {
        return averageTime;
    }

    public Pages getPages() {
        return pages;
    }


    /**
     * Obtener todas las tareas ordenadas por prioridad.
     * @return Lista de tareas ordenadas por prioridad.
     */
    public List<Task> getAllTasksSortedByPriority() {
        return taskRepository.findAllSortedByPriority();
    }

    /**
     * Obtener todas las tareas ordenadas por fecha de vencimiento.
     * @return Lista de tareas ordenadas por fecha de vencimiento.
     */
    public List<Task> getAllTasksSortedByDueDate() {
        return taskRepository.findAllSortedByDueDate();
    }

    /**
     * Obtener todas las tareas ordenadas por prioridad y fecha de vencimiento.
     * @return Lista de tareas ordenadas por prioridad y fecha de vencimiento.
     */
    public List<Task> getAllTasksSortedByPriorityAndDueDate() {
        return taskRepository.findAllSortedByPriorityAndDueDate();
    }
}
