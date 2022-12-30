package dev.fennex.clean.domain.repository;

import dev.fennex.clean.domain.model.Todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoTestRepository implements TodoRepository {
    private Store store;
    public TodoTestRepository() {
        this.store = Store.getInstance();
    }
    public TodoTestRepository(Store store) {
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
        public String lastIdInserted = null;
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




