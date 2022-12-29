package dev.fennex.clean.domain.interactors;

import dev.fennex.clean.data.repository.LoginDataRepository;
import dev.fennex.clean.domain.model.AuthenticationToken;
import dev.fennex.clean.domain.repository.LoginRepository;

public class LoginUseCase {
    private final LoginRepository loginRepository;
    public String username;
    public String password;
    public LoginUseCase() {
        this.loginRepository = new LoginDataRepository();
    }
    public LoginUseCase(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public AuthenticationToken execute() throws Exception {
        if(username == null || username.length() == 0) {
            throw new Exception("Missing required parameters 'username'");
        }

        if(password == null || password.length() == 0) {
            throw new Exception("Missing required parameters 'password'");
        }
        String token = this.loginRepository.authenticate(this.username, this.password);
        return new AuthenticationToken(token);
    }
}
