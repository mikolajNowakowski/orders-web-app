package com.app.data.reader.loader.json.json_loader;

import com.app.data.reader.loader.json.json_loader.exception.ObjectLoaderException;
import com.google.gson.Gson;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class FromJsonToObjectLoader<T> {

    private final Gson gson;
    private final Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public FromJsonToObjectLoader(Gson gson) {
        this.gson = gson;
    }

    public T loadObject(String path) {
        try (var fileReader = new FileReader(path)){
            return gson.fromJson(fileReader, type);
        } catch (Exception e) {
            throw new ObjectLoaderException(e.getMessage());
        }
    }

}
