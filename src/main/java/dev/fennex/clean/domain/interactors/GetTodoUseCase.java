package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.data.repository.TodoDataRepository;
import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoRepository;

public class GetTodoUseCase {
    private final TodoRepository repository;
    public String id;
    public String userId;

    public GetTodoUseCase() {
        this.repository = new TodoDataRepository();
    }


    public GetTodoUseCase(TodoRepository repository) {
        this.repository = repository;
    }

    public Todo execute() throws Exception {
        if (this.userId == null) {
            throw new Exception("Missing userId");
        }

        if (this.id == null) {
            throw new Exception("Missing id");
        }

        return repository.get(this.userId, this.id);
    }
}
