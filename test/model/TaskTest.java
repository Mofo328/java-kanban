package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void shouldTasksEqualsIfTheyIdEquals() {
        Task task1 = new Task("name 1", Status.NEW, "description 1", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Task task2 = new Task("name 2", Status.NEW, "description 2", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2);
    }
}