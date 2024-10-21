package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

class ReadKeyJson {
    public static String[] readJson(String pathFile) {
        try {
            File file = new File(pathFile); // путь к файлу
            FileReader reader = new FileReader(file);
            System.out.println("reader: " + reader);
            Type keyListType = new TypeToken<List<Key>>() {
            }.getType();
            System.out.println("keyListType: " + keyListType);
            List<Key> keys = new Gson().fromJson(reader, keyListType);
            reader.close();

            // Преобразование списка объектов Key в массив строк
            String[] keyArray = keys.stream().map(Key::getKey).toArray(String[]::new);
            System.out.println("keyArray: " + Arrays.toString(keyArray));
            if (!keys.isEmpty()) {
                return keyArray;
            } else {
                System.out.println("Ключи не найдены или список пуст.");
                return new String[0]; // Возвращаем пустой массив
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
