import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.taskmanager.httpServer.HttpClassServer;
import ru.yandex.practicum.taskmanager.manager.Managers;
import ru.yandex.practicum.taskmanager.manager.TaskManager;
import ru.yandex.practicum.taskmanager.parseAdapter.DurationAdapter;
import ru.yandex.practicum.taskmanager.parseAdapter.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;


public abstract class HttpClassServerTest {
    protected TaskManager manager;
    protected HttpClassServer httpServer;
    protected String HOST = "http://localhost";
    protected int PORT = 8080;
    protected Gson gson;


    @BeforeEach
    void init() {
        manager = Managers.getDefault();
        httpServer = new HttpClassServer(manager);
        httpServer.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }


    @AfterEach
    void stop() {
        httpServer.stop();
    }
}
