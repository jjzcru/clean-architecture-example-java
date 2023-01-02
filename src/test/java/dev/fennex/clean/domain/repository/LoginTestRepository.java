package dev.fennex.clean.domain.repository;

public class LoginTestRepository implements LoginRepository{
    @Override
    public String authenticate(String username, String password) {
        if (username.equals("username") && password.equals("password")) {
            return "testtoken";
        }
        return "";
    }
}
