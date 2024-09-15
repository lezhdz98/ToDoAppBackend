package com.encora.breakable_toy.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

@Component
public class AverageTime {
    // Variables para almacenar el tiempo total y los tiempos por prioridad
    private LocalTime totalTime = LocalTime.of(0, 0);
    private LocalTime highTime = LocalTime.of(0, 0);
    private LocalTime mediumTime = LocalTime.of(0, 0);
    private LocalTime lowTime = LocalTime.of(0, 0);

    // Contadores para el número de tareas por prioridad
    private long totalCount = 0;
    private long highCount = 0;
    private long mediumCount = 0;
    private long lowCount = 0;

    // Getters para obtener los tiempos promedio en minutos
    public long getTotalTimeInMinutes() {
        return getTimeInMinutes(calculateAverageTime(totalTime, totalCount));
    }

    public long getHighTimeInMinutes() {
        return getTimeInMinutes(calculateAverageTime(highTime, highCount));
    }

    public long getMediumTimeInMinutes() {
        return getTimeInMinutes(calculateAverageTime(mediumTime, mediumCount));
    }

    public long getLowTimeInMinutes() {
        return getTimeInMinutes(calculateAverageTime(lowTime, lowCount));
    }

    // Método genérico para añadir una duración a un tiempo específico
    private LocalTime addDuration(Duration duration, LocalTime time) {
        return time.plusSeconds(duration.getSeconds());
    }

    // Método genérico para restar una duración de un tiempo específico
    private LocalTime subtractDuration(Duration duration, LocalTime time) {
        return time.minusSeconds(duration.getSeconds());
    }

    // Métodos públicos para añadir duraciones a los tiempos correspondientes
    public void addTotalDuration(Duration duration) {
        totalTime = addDuration(duration, totalTime);
        totalCount++;
    }

    public void addHighDuration(Duration duration) {
        highTime = addDuration(duration, highTime);
        highCount++;
    }

    public void addMediumDuration(Duration duration) {
        mediumTime = addDuration(duration, mediumTime);
        mediumCount++;
    }

    public void addLowDuration(Duration duration) {
        lowTime = addDuration(duration, lowTime);
        lowCount++;
    }

    // Métodos públicos para restar duraciones de los tiempos correspondientes
    public void subtractTotalDuration(Duration duration) {
        totalTime = subtractDuration(duration, totalTime);
        totalCount--;
    }

    public void subtractHighDuration(Duration duration) {
        highTime = subtractDuration(duration, highTime);
        highCount--;
    }

    public void subtractMediumDuration(Duration duration) {
        mediumTime = subtractDuration(duration, mediumTime);
        mediumCount--;
    }

    public void subtractLowDuration(Duration duration) {
        lowTime = subtractDuration(duration, lowTime);
        lowCount--;
    }

    // Método para calcular el tiempo promedio basado en el tiempo total y el conteo
    private LocalTime calculateAverageTime(LocalTime totalTime, long count) {
        if (count == 0) {
            return LocalTime.of(0, 0);
        }
        long averageSeconds = totalTime.toSecondOfDay() / count;
        return LocalTime.ofSecondOfDay(averageSeconds);
    }

    // Método genérico para obtener el tiempo en minutos
    public long getTimeInMinutes(LocalTime time) {
        return time.toSecondOfDay() / 60;
    }
}
