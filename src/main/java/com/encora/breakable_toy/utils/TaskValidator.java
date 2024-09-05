package com.encora.breakable_toy.utils;

import com.encora.breakable_toy.entitiy.Task;

public class TaskValidator {

    public static void validate(Task task) {
        if(task == null){
            throw new IllegalArgumentException("A null task is not valid.");
        }
        
        validateName(task.getName());
        validatePriority(task.getPriority());
    }

    private static void validateName(String name) {
        if(name == null || name.isEmpty() || name.length()>120) {
            throw new IllegalArgumentException("The task name is not valid.");
        }
    }

    private static void validatePriority(int priority) {
        if(priority < 0 || priority > 2 ){
            throw new IllegalArgumentException("The task priority is not valid, choose 2 = High, 1 = Medium, 0 = Low ");
        }
    }
}
