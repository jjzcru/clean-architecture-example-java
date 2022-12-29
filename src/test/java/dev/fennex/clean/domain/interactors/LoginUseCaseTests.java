package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.domain.model.AuthenticationToken;
import dev.fennex.clean.domain.repository.LoginTestRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUseCaseTests {

    @Test
    void testSuccessLogin() {
        LoginUseCase useCase = new LoginUseCase(new LoginTestRepository());
        useCase.username = "username";
        useCase.password = "password";
        try {
            AuthenticationToken response = useCase.execute();
            assertTrue(response.token.length() > 0);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testInvalidCredentials() {
        LoginUseCase useCase = new LoginUseCase(new LoginTestRepository());
        useCase.username = "username1";
        useCase.password = "password";
        try {
            AuthenticationToken response = useCase.execute();
            assertEquals(0, response.token.length());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testMissingUsername() {
        LoginUseCase useCase = new LoginUseCase(new LoginTestRepository());
        useCase.username = "";
        useCase.password = "password";
        Exception exception = assertThrows(Exception.class, useCase::execute);

        String expectedMessage = "Missing required parameters 'username'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testMissingPassword() {
        LoginUseCase useCase = new LoginUseCase(new LoginTestRepository());
        useCase.username = "username";
        useCase.password = "";
        Exception exception = assertThrows(Exception.class, useCase::execute);

        String expectedMessage = "Missing required parameters 'password'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
