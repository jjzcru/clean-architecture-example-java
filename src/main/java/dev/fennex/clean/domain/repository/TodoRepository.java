package dev.fennex.clean.domain.repository;

import dev.fennex.clean.domain.model.Todo;

import java.util.List;

public interface TodoRepository {
    List<Todo> getAll(String userId);
    Todo get(String userId, String id);
    Todo add(Todo todo);
    Todo complete(String userId, String id);
    Todo delete(String userId, String id);
}
