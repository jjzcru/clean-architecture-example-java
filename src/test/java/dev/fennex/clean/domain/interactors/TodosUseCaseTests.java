package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.domain.repository.TodoTestRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TodosUseCaseTests {
    String userId = "0";
    String createdId;

    TodoTestRepository.Store store = TodoTestRepository.Store.getInstance();

    @Test
    void _1_testSuccessAddTodo() throws Exception {
        AddTodoUseCase useCase = new AddTodoUseCase(new TodoTestRepository(store));
        Todo todo = new Todo();
        todo.content = "Hello world";
        todo.userId = userId;

        useCase.todo = todo;

        Todo response = useCase.execute();
        assertNotNull(response.id);
        createdId = response.id;
        assertEquals(todo.userId, response.userId);
        assertEquals(todo.content, response.content);
    }

    @Test
    void _2_testSuccessGetAllTodos() throws Exception {
        GetAllTodosUseCase useCase = new GetAllTodosUseCase(new TodoTestRepository(store));
        useCase.userId = userId;

        List<Todo> response = useCase.execute();
        assertEquals(1, response.size());
    }

    @Test
    void _3_testSuccessGetTodoById() throws Exception {
        GetTodoUseCase useCase = new GetTodoUseCase(new TodoTestRepository(store));
        useCase.userId = userId;
        useCase.id = store.lastIdInserted;

        Todo response = useCase.execute();
        assertNotNull(response);
        assertEquals(response.id, store.lastIdInserted);
    }

    @Test
    void _4_testSuccessCompleteTodoById() throws Exception {
        CompleteTodoUseCase useCase = new CompleteTodoUseCase(new TodoTestRepository(store));
        useCase.userId = userId;
        useCase.id = store.lastIdInserted;

        Todo response = useCase.execute();
        assertNotNull(response);
        assertEquals(response.id, store.lastIdInserted);
        assertTrue(response.complete);
    }

    @Test
    void _5_testSuccessDeleteTodoById() throws Exception {
        DeleteTodoUseCase useCase = new DeleteTodoUseCase(new TodoTestRepository(store));
        useCase.userId = userId;
        useCase.id = store.lastIdInserted;

        Todo response = useCase.execute();
        assertNotNull(response);
        assertEquals(response.id, store.lastIdInserted);
        assertTrue(response.complete);
        assertEquals(store.size(), 0);
    }
}
