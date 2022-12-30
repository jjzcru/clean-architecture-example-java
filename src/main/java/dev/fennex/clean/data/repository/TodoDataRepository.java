package dev.fennex.clean.data.repository;

import java.util.List;

import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoRepository;

public class TodoDataRepository implements TodoRepository{
    private TodoRepository repository;
    
    public TodoDataRepository() {
        this.repository = new TodoDataMemoryRepository();
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
}
