package model;


import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void shouldEpicsEqualsIfTheyIdEquals() {
        Epic epic = new Epic("name 1", "description 1", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        Epic epic1 = new Epic("name 2", "description 3", Duration.ofMinutes(3), LocalDateTime.of(2024, 2, 3, 2, 0));
        epic1.setId(1);
        epic.setId(1);
        assertEquals(epic, epic1);
    }
}