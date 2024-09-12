package com.encora.breakable_toy.utils;

import org.springframework.stereotype.Component;

@Component
public class Pages {
    // Número total de páginas
    private int totalPages;
    // Página actual que el usuario está viendo
    private int actualPage;
    // Constante que define el número de tareas por página
    public static final int TASKS_PER_PAGE = 10;

    // Getter para obtener el número total de páginas
    public int getTotalPages() {
        return totalPages;
    }

    // Setter para establecer el número total de páginas basado en el número total de tareas
    public void setTotalPages(int totalTasks) {
        // Calcula el número total de páginas redondeando hacia arriba
        this.totalPages = (int) Math.ceil((double) totalTasks / TASKS_PER_PAGE);
    }

    // Getter para obtener la página actual
    public int getActualPage() {
        return actualPage;
    }

    // Setter para establecer la página actual, asegurándose de que esté dentro del rango válido
    public void setActualPage(int actualPage) {
        if (actualPage < 1 || actualPage > totalPages) {
            throw new IllegalArgumentException("Invalid page number");
        }
        this.actualPage = actualPage;
    }

    // Método para actualizar el número total de páginas
    public void updateTotalPages(int totalTasks) {
        setTotalPages(totalTasks);
    }

    // Método para avanzar a la siguiente página, si es posible
    public void nextPage() {
        if (actualPage < totalPages) {
            actualPage++;
        }
    }

    // Método para retroceder a la página anterior, si es posible
    public void previousPage() {
        if (actualPage > 1) {
            actualPage--;
        }
    }
}
