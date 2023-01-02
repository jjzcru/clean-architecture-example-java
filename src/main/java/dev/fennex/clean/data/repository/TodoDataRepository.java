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

public class TodoDataRepository implements TodoRepository{
    private final TodoRepository repository;
    public static final String STORAGE_TYPE = System.getenv("STORAGE_TYPE") != null ? System.getenv("STORAGE_TYPE") : "file";
    
    public TodoDataRepository() {
        this.repository = STORAGE_TYPE.equals("memory") ? new TodoDataMemoryRepository() : new TodoDataFileRepository();
    }

    @Override
    public List<Todo> getAll(String userId) {
        return this.repository.getAll(userId);
    }

    @Override
    public Todo get(String userId, String id) {
        return this.repository.get(userId, id);
    }

    @Override
    public Todo add(Todo todo) {
        return this.repository.add(todo);
    }

    @Override
    public Todo complete(String userId, String id) {
        return this.repository.complete(userId, id);
    }

    @Override
    public Todo delete(String userId, String id) {
        return this.repository.delete(userId, id);
    }

    public static class TodoDataFileRepository implements TodoRepository {
        private String workspaceDir;
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
            Path usersFilePath = Path.of(this.workspaceDir, fileSeparator, "users.json");
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
                if(!todos.isEmpty()) {
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

    public static class TodoDataMemoryRepository implements TodoRepository {
        private Store store;
        public TodoDataMemoryRepository() {
            this.store = Store.getInstance();
        }
        public TodoDataMemoryRepository(Store store) {
            this.store = store;
        }
        @Override
        public List<Todo> getAll(String userId) {
            return this.store.getFromUser(userId);
        }

        @Override
        public Todo get(String userId, String id) {
            return this.store.getFromIdAndUser(userId, id);
        }

        @Override
        public Todo add(Todo todo) {
            return this.store.add(todo);
        }

        @Override
        public Todo complete(String userId, String id) {
            return this.store.complete(userId, id);
        }

        @Override
        public Todo delete(String userId, String id) {
            return this.store.delete(userId, id);
        }

        public static class Store {
            private List<Todo> data = new ArrayList<>();
            public  String lastIdInserted = null;
            private static final Store instance = new Store();
            private Store() {}

            public static Store getInstance() {
                return instance;
            }

            public Integer size() {
                return this.data.size();
            }

            public Todo add(Todo todo) {
                todo.id = UUID.randomUUID().toString();
                lastIdInserted = todo.id;
                todo.createdAt = new Date();
                this.data.add(todo);
                return todo;
            }

            public List<Todo> getFromUser(String userId) {
                return this.data.stream().filter(t -> t.userId.equals(userId))
                        .collect(Collectors.toList());
            }

            public Todo getFromIdAndUser(String userId, String id) {
                return this.data.stream().filter(t -> t.userId.equals(userId) && t.id.equals(id))
                        .toList()
                        .get(0);
            }

            public Todo complete(String userId, String id) {
                this.data = this.data.stream()
                        .map(t -> {
                            if(t.userId.equals(userId) && t.id.equals(id)) {
                                t.complete = true;
                                t.completedAt = new Date();
                            }
                            return t;
                        })
                        .collect(Collectors.toList());
                return this.getFromIdAndUser(userId, id);
            }

            public Todo delete(String userId, String id) {
                Todo todo = getFromIdAndUser(userId, id);
                this.data = this.data.stream().filter(t -> !(t.userId.equals(userId) && t.id.equals(id)))
                        .toList();
                return todo;
            }
        }
    }

}
