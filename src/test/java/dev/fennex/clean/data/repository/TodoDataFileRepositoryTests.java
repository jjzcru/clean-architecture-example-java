package dev.fennex.clean.data.repository;

import dev.fennex.clean.domain.model.Todo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TodoDataFileRepositoryTests {
    private TodoDataRepository.TodoDataFileRepository repository;
    private final String userId = "0";
    @BeforeAll
    public void beforeAllTests() throws Exception {
        repository = new TodoDataRepository.TodoDataFileRepository();
    }

    @Test
    void testSuccessGetAll() throws Exception {
        List<Todo> todos = repository.getAll(userId);
        assertNotNull(todos);
    }

    @Test
    void testSuccessAddTodo() throws Exception {
        List<Todo> todos = repository.getAll();
        int totalBefore = todos.size();

        Todo todo = new Todo();
        todo.userId = userId;
        todo.content = "Front the test";
        repository.add(todo);

        List<Todo> todosAfter = repository.getAll();
        int totalAfter = todosAfter.size();

        assertTrue(totalAfter > totalBefore);
    }

    @Test
    void testSuccessDelete() throws Exception {
        Todo todo = new Todo();
        todo.userId = userId;
        todo.content = "Front the test";

        Todo createdTodo = repository.add(todo);

        List<Todo> todos = repository.getAll();
        int totalBefore = todos.size();

        repository.delete(userId, createdTodo.id);

        List<Todo> todosAfter = repository.getAll();
        int totalAfter = todosAfter.size();

        assertTrue(totalAfter < totalBefore);
    }

    @Test
    void testSuccessCompleteTodo() throws Exception {
        Todo todo = new Todo();
        todo.userId = userId;
        todo.content = "Front the test";

        Todo createdTodo = repository.add(todo);

        repository.complete(userId, createdTodo.id);

        Todo completedTodo = repository.get(userId, createdTodo.id);

        assertTrue(completedTodo.complete);
    }

    @Test
    void testSuccessGetTodo() throws Exception {
        Todo t = new Todo();
        t.userId = userId;
        t.content = "Front the test";

        Todo createdTodo = repository.add(t);

        Todo todo = repository.get(userId, createdTodo.id);

        assertEquals(todo.id, createdTodo.id);
    }
}
