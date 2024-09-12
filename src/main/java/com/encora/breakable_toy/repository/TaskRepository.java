package com.encora.breakable_toy.repository;

import com.encora.breakable_toy.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private HashMap<Long, Task> taskTable = new HashMap<>();
    private long currentId = 1;

    /**
     * Obtener todas las tareas.
     * @return Lista de todas las tareas.
     */
    public List<Task> findAll() {
        return new ArrayList<>(taskTable.values());
    }

    /**
     * Buscar una tarea por su ID.
     * @param id ID de la tarea.
     * @return Tarea encontrada, si existe.
     */
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskTable.get(id));
    }

    /**
     * Guardar una tarea.
     * @param task Tarea a guardar.
     * @return Tarea guardada.
     */
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(currentId++);
        }
        taskTable.put(task.getId(), task);
        return task;
    }

    /**
     * Eliminar una tarea por su ID.
     * @param id ID de la tarea a eliminar.
     */
    public void deleteById(Long id) {
        taskTable.remove(id);
    }

    // Métodos para ordenar las tareas

    /**
     * Obtener todas las tareas ordenadas por prioridad (descendente).
     * @return Lista de tareas ordenadas por prioridad.
     */
    public List<Task> findAllSortedByPriority() {
        return taskTable.values().stream()
                .sorted(Comparator.comparingInt(Task::getPriority).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las tareas ordenadas por fecha de vencimiento.
     * @return Lista de tareas ordenadas por fecha de vencimiento.
     */
    public List<Task> findAllSortedByDueDate() {
        return taskTable.values().stream()
                .sorted(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las tareas ordenadas por prioridad (descendente) y fecha de vencimiento.
     * @return Lista de tareas ordenadas por prioridad y fecha de vencimiento.
     */
    public List<Task> findAllSortedByPriorityAndDueDate() {
        return taskTable.values().stream()
                .sorted(Comparator.comparingInt(Task::getPriority).reversed()
                        .thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}
