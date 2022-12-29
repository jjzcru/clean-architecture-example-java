package dev.fennex.clean.data.repository;

import dev.fennex.clean.domain.repository.LoginRepository;

public class LoginDataRepository implements LoginRepository {
    @Override
    public String authenticate(String username, String password) {
        if (username.equals("username") && password.equals("password")) {
           return "testtoken";
        }
        return "";
    }
}
