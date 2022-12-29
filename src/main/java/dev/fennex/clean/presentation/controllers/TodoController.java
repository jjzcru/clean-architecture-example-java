package dev.fennex.clean.presentation.controllers;

import dev.fennex.clean.domain.model.Todo;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @GetMapping(
            value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Todo>> getTodos() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok().body(Collections.emptyList());
    }

    // TODO Get Todo by ID
    // TODO Create To-Do
    // TODO Update a to by id
    // TODO Delete a To-Do
}

