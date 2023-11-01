package com.app.data.reader.loader;

import com.app.data.reader.model.filename.DataSource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public interface Loader<T> {

    T load(DataSource datasource);

    static <T> List<T> read(String filename, Function<String, T> converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter is null");
        }
        try (var lines = Files.lines(Paths.get(filename))) {
            return lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(converter)
                    .toList();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
