package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.data.repository.TodoDataRepository;
import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoRepository;

import java.util.List;

public class GetAllTodosUseCase {
    private final TodoRepository repository;
    public String userId;

    public GetAllTodosUseCase() {
        this.repository = new TodoDataRepository();
    }

    public GetAllTodosUseCase(TodoRepository repository) {
        this.repository = repository;
    }


    public List<Todo> execute() throws Exception {
        if(this.userId == null) {
            throw new Exception("Missing todo content");
        }

        return repository.getAll(this.userId);
    }
}
