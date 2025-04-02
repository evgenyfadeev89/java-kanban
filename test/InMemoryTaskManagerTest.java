import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.files.*;
import ru.yandex.practicum.taskmanager.manager.*;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest {
    @Override
    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}