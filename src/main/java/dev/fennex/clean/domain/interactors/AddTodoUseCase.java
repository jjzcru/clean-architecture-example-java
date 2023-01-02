package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.data.repository.TodoDataRepository;
import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoRepository;

public class AddTodoUseCase {
    private final TodoRepository repository;
    public Todo todo;

    public AddTodoUseCase() {
        this.repository = new TodoDataRepository();
    }

    public AddTodoUseCase(TodoRepository repository) {
        this.repository = repository;
    }

    public Todo execute() throws Exception {
        if(todo == null) {
            throw new Exception("Missing todo content");
        }

        return repository.add(this.todo);
    }
}
