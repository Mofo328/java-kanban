package model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void shouldEpicsEqualsIfTheyIdEquals() {
        // arrange
        Epic epic = new Epic("name 1", "description 1");
        Epic epic1 = new Epic("name 1", "description 1");
        epic1.setId(1);
        epic.setId(1);
        assertEquals(epic, epic1);
    }
}