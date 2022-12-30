package dev.fennex.clean.presentation.controllers;

import dev.fennex.clean.domain.interactors.*;
import dev.fennex.clean.domain.model.Todo;
import dev.fennex.clean.presentation.controllers.error.RequestError;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @GetMapping(
            value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> getAll() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GetAllTodosUseCase useCase = new GetAllTodosUseCase();
        useCase.userId = userId;

        try {
            return ResponseEntity.ok().body(useCase.execute());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(new RequestError(e.getMessage()));
        }
    }


    @PostMapping(
            value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> add(@RequestBody AddTodoRequest request) {
        String content = request.content;
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Set todo data
        Todo todo = new Todo();
        todo.userId = userId;
        todo.content = content;

        // Create use case and assign todo
        AddTodoUseCase useCase = new AddTodoUseCase();
        useCase.todo = todo;

        try {
            return ResponseEntity.ok().body(useCase.execute());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(new RequestError(e.getMessage()));
        }
    }
    
    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> get(@PathVariable(value="id") String id) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GetTodoUseCase useCase = new GetTodoUseCase();
        useCase.userId = userId;
        useCase.id = id;

        try {
            return ResponseEntity.ok().body(useCase.execute());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(new RequestError(e.getMessage()));
        }
    }
    
    @PutMapping(
            value = "/{id}/complete",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> complete(@PathVariable(value="id") String id) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompleteTodoUseCase useCase = new CompleteTodoUseCase();
        useCase.userId = userId;
        useCase.id = id;

        try {
            return ResponseEntity.ok().body(useCase.execute());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(new RequestError(e.getMessage()));
        }
    }

    @DeleteMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> delete(@PathVariable(value="id") String id) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DeleteTodoUseCase useCase = new DeleteTodoUseCase();
        useCase.userId = userId;
        useCase.id = id;

        try {
            return ResponseEntity.ok().body(useCase.execute());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(new RequestError(e.getMessage()));
        }
    }
}

class AddTodoRequest {
    public String content;
}
