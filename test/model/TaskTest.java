package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void shouldTasksEqualsIfTheyIdEquals() {
        // arrange
        Task task1 = new Task("name 1", Status.NEW, "description 1");
        Task task2 = new Task("name 2", Status.NEW, "description 2");
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2);
    }
}