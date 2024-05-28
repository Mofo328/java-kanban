package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    @Test
    void shouldSubTasksEqualsIfTheyIdEquals() {
        SubTask subTask = new SubTask("name 1", Status.NEW, "description 1", 2);
        SubTask subTask1 = new SubTask("name 2", Status.NEW, "description 3", 2);
        subTask.setId(1);
        subTask1.setId(1);
        assertEquals(subTask, subTask1);
    }
}