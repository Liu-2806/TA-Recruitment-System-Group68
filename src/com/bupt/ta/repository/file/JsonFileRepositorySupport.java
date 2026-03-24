package com.bupt.ta.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

abstract class JsonFileRepositorySupport {
    private static final Type LIST_OF_MAPS = new TypeToken<List<Map<String, Object>>>() { }.getType();
    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path repositoryRoot = Paths.get("data");

    protected List<Map<String, Object>> readList(String relativePath, List<Map<String, Object>> defaultValue) {
        Path path = repositoryRoot.resolve(relativePath);
        ensureFile(path, defaultValue);
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<Map<String, Object>> values = gson.fromJson(reader, LIST_OF_MAPS);
            return values == null ? Collections.emptyList() : values;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read data file: " + path, ex);
        }
    }

    protected void writeList(String relativePath, List<Map<String, Object>> values) {
        Path path = repositoryRoot.resolve(relativePath);
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(values, LIST_OF_MAPS, writer);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write data file: " + path, ex);
        }
    }

    private void ensureFile(Path path, List<Map<String, Object>> defaultValue) {
        if (Files.exists(path)) {
            return;
        }
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(defaultValue, LIST_OF_MAPS, writer);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to initialize data file: " + path, ex);
        }
    }
}
