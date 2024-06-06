package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    @Test
    void shouldSubTasksEqualsIfTheyIdEquals() {
        SubTask subTask = new SubTask("name 1", Status.NEW, "description 1", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        SubTask subTask1 = new SubTask("name 2", Status.NEW, "description 3", Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        subTask.setId(1);
        subTask1.setId(1);
        assertEquals(subTask, subTask1);
    }
}