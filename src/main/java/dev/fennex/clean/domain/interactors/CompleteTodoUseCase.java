package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.data.repository.TodoDataRepository;
import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoRepository;

public class CompleteTodoUseCase {
    private final TodoRepository repository;
    public String id;
    public String userId;


    public CompleteTodoUseCase() {
        this.repository = new TodoDataRepository();
    }

    public CompleteTodoUseCase(TodoRepository repository) {
        this.repository = repository;
    }

    public Todo execute() throws Exception {
        if(this.userId == null) {
            throw new Exception("Missing userId");
        }

        if(this.id == null) {
            throw new Exception("Missing id");
        }

        return repository.complete(this.userId, this.id);
    }
}
