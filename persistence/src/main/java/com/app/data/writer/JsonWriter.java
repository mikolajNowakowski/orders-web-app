package com.app.data.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {

    public static <T> void saveToJsonFile(T object, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
