package dev.fennex.clean.domain.repository;

import dev.fennex.clean.domain.model.Todo;

import java.util.*;
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
        return null;
    }
    
    @Override
    public Todo delete(String id) {
        return null;
    }
}

class Store {
    private final List<Todo> data = new ArrayList<>();
    private static final Store instance = new Store();
    private Store() {}

    public static Store getInstance() {
        return instance;
    }

    public Todo add(Todo todo) {
        todo.id = UUID.randomUUID().toString();
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
}


