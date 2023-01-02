package dev.fennex.clean.presentation.controllers;

import dev.fennex.clean.domain.interactors.LoginUseCase;
import dev.fennex.clean.domain.model.AuthenticationToken;
import dev.fennex.clean.presentation.controllers.error.RequestError;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @PostMapping(
            value = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> index(@RequestBody LoginRequest request) {
        String username = request.username;
        String password = request.password;

        LoginUseCase useCase = new LoginUseCase();
        useCase.username = username;
        useCase.password = password;

        try {
            AuthenticationToken response = useCase.execute();
            if (response.token.length() == 0) {
                return ResponseEntity.status(403).body(new RequestError("Invalid credentials"));
            }
            return ResponseEntity.ok().body(response);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(new RequestError(e.getMessage()));
        }
    }

    static class LoginRequest {
        public String username;
        public String password;
    }
}

