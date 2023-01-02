package dev.fennex.clean.domain.repository;

public interface LoginRepository {
    String authenticate(String username, String password);
}
