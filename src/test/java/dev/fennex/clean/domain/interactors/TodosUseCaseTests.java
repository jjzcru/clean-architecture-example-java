package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoTestRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TodosUseCaseTests {
    String userId = "0";
    String createdId;

    @Test
    void testSuccessAddTodo() {
        AddTodoUseCase useCase = new AddTodoUseCase(new TodoTestRepository());
        Todo todo = new Todo();
        todo.content = "Hello world";
        todo.userId = userId;

        useCase.todo = todo;

        try {
            Todo response = useCase.execute();
            assertNotNull(response.id);
            createdId = response.id;
            assertEquals(todo.userId, response.userId);
            assertEquals(todo.content, response.content);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testSuccessGetAllTodos() {
        GetAllTodosUseCase useCase = new GetAllTodosUseCase(new TodoTestRepository());
        useCase.userId = userId;

        try {
            List<Todo> response = useCase.execute();
            assertEquals(1, response.size());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testSuccessGetTodoById() {
        GetTodoUseCase useCase = new GetTodoUseCase(new TodoTestRepository());
        useCase.userId = userId;
        useCase.id = createdId;

        try {
            Todo response = useCase.execute();
            assertNotNull(response);
            assertEquals(response.id, createdId);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
