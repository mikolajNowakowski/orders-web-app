package com.app.transformer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(formatter.format(value));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.hasNext()) {
            String dateString = in.nextString();
            return LocalDate.parse(dateString, formatter);
        }
        return null;
    }
}
