package com.encora.breakable_toy.config;

import com.encora.breakable_toy.utils.AverageTime;
import com.encora.breakable_toy.utils.Pages;
import com.encora.breakable_toy.utils.TaskValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TaskValidator taskValidator() {
        return new TaskValidator();
    }
    @Bean
    public AverageTime averageTime() {
        return new AverageTime();
    }
    @Bean
    public Pages pages() {
        return new Pages();
    }
}
