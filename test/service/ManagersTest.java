package service;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldManagerCanReturnManagersReadyToWork() {
        assertNotNull(Managers.getDefault());
        assertNotNull(Managers.getDefaultHistory());
        assertNotNull(Managers.getDefaultFileBackedTaskManager());
    }
}