package com.encora.breakable_toy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.encora.breakable_toy.entitiy.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
}

