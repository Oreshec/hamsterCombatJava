package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ReadKeyJson {
    private static final Logger LOGGER = Logger.getLogger(ReadKeyJson.class.getName());

    public static String[] readJson(String pathFile) {
        try {
            LOGGER.info("Reading JSON passwords");

            File file = new File(pathFile); // путь к файлу
            LOGGER.info("Path file: " + pathFile);
            LOGGER.info("file: " + file);
            FileReader reader = new FileReader(file);
            LOGGER.info("Reader: " + reader);

            Type keyListType = new TypeToken<List<Key>>() {
            }.getType();
            List<Key> keys = new Gson().fromJson(reader, keyListType);
            reader.close();

            // Преобразование списка объектов Key в массив строк
            return keys.stream().map(Key::getKey).toArray(String[]::new);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File not found" + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Critical error: " + e.getMessage(), e);
        }
        return new String[0];
    }
}
