package com.encora.breakable_toy.entitiy;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(length = 120)
    private String name;

    private boolean checkDone;
    //@Min(0)
    //@Max(2)
    private int priority;
    private LocalDateTime creationDate;
    private LocalDateTime doneDate;
    private LocalDateTime dueDate;




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
        this.name = name;
    }

    public boolean getCheckDone() {
        return checkDone;
    }
    public void setCheckDone(boolean checkDone) {
        this.checkDone = checkDone;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
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
        this.dueDate = dueDate;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name='" + name + '\'' + ", checkDone='" + checkDone + '\''
                + ", priority=" + priority + ", creationDate='" + creationDate
                + ", doneDate='" + doneDate+ ", dueDate='" + dueDate + '\'' + '}';
    }
}
