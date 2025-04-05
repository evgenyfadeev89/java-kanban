package ru.yandex.practicum.taskmanager.parseAdapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        String localDateTimeString = Objects.nonNull(localDateTime)
                ? localDateTime.format(formatter)
                : null;
        jsonWriter.value(localDateTimeString);
    }


    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatter);
    }
}
