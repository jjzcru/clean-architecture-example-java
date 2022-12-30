package dev.fennex.clean.data.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoRepository;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoDataFileRepository implements TodoRepository {
    private String workspaceDir;
    private Path usersFilePath;
    private Path todosFilePath;

    public TodoDataFileRepository() {
        this.workspaceDir = this.getWorkspaceDir();
        this.build();
    }

    private String getWorkspaceDir() {
        if (System.getenv("WORKING_DIRECTORY") != null) {
            return System.getenv("WORKING_DIRECTORY");
        }
        return this.getTempDirectory();
    }

    private String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    private void build() {
        this.validateWorkingDirectory();

        String fileSeparator = System.getProperty("file.separator");
        usersFilePath = Path.of(this.workspaceDir, fileSeparator, "users.json");
        todosFilePath = Path.of(this.workspaceDir, fileSeparator, "todos.json");

        // Create the file that handle the users
        File file = new File(usersFilePath.toString());
        if (!file.exists()) {
            try {
                file.createNewFile();
                Files.writeString(usersFilePath, "[]");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        // Create the file that handles the todos
        file = new File(todosFilePath.toString());
        if (!file.exists()) {
            try {
                file.createNewFile();
                Files.writeString(todosFilePath, "[]");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void validateWorkingDirectory() {
        File f = new File(this.workspaceDir);
        if (!f.exists()) {
            this.workspaceDir = this.getTempDirectory();
            return;
        }

        if (!f.isDirectory()) {
            this.workspaceDir = this.getTempDirectory();
        }
    }

    @Override
    public List<Todo> getAll(String userId) {
        List<Todo> todos;
        try {
            String contentString = Files.readString(this.todosFilePath);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Todo>>() {
            }.getType();
            ArrayList<Todo> outputList = gson.fromJson(contentString, listType);
            todos = outputList.stream().filter(t -> t.userId.equals(userId))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return todos;
    }

    public List<Todo> getAll() {
        List<Todo> todos;
        try {
            String contentString = Files.readString(this.todosFilePath);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Todo>>() {
            }.getType();
            todos = gson.fromJson(contentString, listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return todos;
    }

    @Override
    public Todo get(String userId, String id) {
        Todo todo = null;
        try {
            String contentString = Files.readString(this.todosFilePath);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Todo>>() {
            }.getType();
            ArrayList<Todo> outputList = gson.fromJson(contentString, listType);
            List<Todo> todos = outputList.stream().filter(t -> t.userId.equals(userId) && t.id.equals(id))
                    .toList();
            if(todos.size() > 0) {
                todo = todos.get(0);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return todo;
    }

    @Override
    public Todo add(Todo todo) {
        List<Todo> todos = this.getAll();
        todo.id = UUID.randomUUID().toString();
        todo.createdAt = new Date();
        todos.add(todo);
        this.save(todos);
        return todo;
    }

    @Override
    public Todo complete(String userId, String id) {
        List<Todo> todos = this.getAll();
        this.save(todos.stream()
                .map(t -> {
                    if (t.userId.equals(userId) && t.id.equals(id)) {
                        t.complete = true;
                        t.completedAt = new Date();
                    }
                    return t;
                })
                .collect(Collectors.toList()));


        return this.get(userId, id);
    }

    @Override
    public Todo delete(String userId, String id) {
        Todo todo = this.get(userId, id);
        this.save(this.getAll().stream()
                .filter(t -> !(t.userId.equals(userId) && t.id.equals(id)))
                .collect(Collectors.toList()));
        return todo;
    }

    private void save(List<Todo> todos) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type listType = new TypeToken<List<Todo>>() {
            }.getType();
            String data = gson.toJson(todos, listType);

            Files.writeString(todosFilePath, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}