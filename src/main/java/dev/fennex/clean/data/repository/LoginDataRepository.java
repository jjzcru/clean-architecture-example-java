package dev.fennex.clean.data.repository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.fennex.clean.Constants;
import dev.fennex.clean.domain.repository.LoginRepository;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LoginDataRepository implements LoginRepository {
    private final LoginRepository repository;

    public String type = System.getenv("STORAGE_TYPE") != null ? System.getenv("STORAGE_TYPE") : "file";

    public LoginDataRepository() {
        this.repository = type.equals("memory") ? new LoginDataMemoryRepository() : new LoginDataFileRepository();
     }

    @Override
    public String authenticate(String username, String password) throws IllegalArgumentException, JWTCreationException {
        return repository.authenticate(username, password);
    }


    static class LoginDataMemoryRepository implements LoginRepository {
        @Override
        public String authenticate(String username, String password) throws IllegalArgumentException, JWTCreationException {
            if (username.equals("username") && password.equals("password")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                // The token will expire 1 hour
                calendar.add(Calendar.HOUR_OF_DAY, 12);

                return JWT.create()
                        .withSubject(Constants.JWT_SUBJECT)
                        .withClaim("userId", "0")
                        .withIssuedAt(new Date())
                        .withExpiresAt(calendar.getTime())
                        .withIssuer(Constants.JWT_ISSUER)
                        .sign(Algorithm.HMAC256(Constants.JWT_SECRET));
            }

            return "";
        }
    }

    static class LoginDataFileRepository implements LoginRepository {

        private String workspaceDir;
        private Path usersFilePath;

        public LoginDataFileRepository() {
            this.workspaceDir = this.getWorkspaceDir();
            this.build();
        }

        private String getWorkspaceDir() {
            if (System.getenv("WORKING_DIRECTORY") != null) {
                return System.getenv("WORKING_DIRECTORY");
            }
            return this.getTempDirectory();
        }

        private String getTempDirectory() {
            return System.getProperty("java.io.tmpdir");
        }

        private void build() {
            this.validateWorkingDirectory();

            String fileSeparator = System.getProperty("file.separator");
            usersFilePath = Path.of(this.workspaceDir, fileSeparator, "users.json");

            // Create the file that handle the users
            File file = new File(usersFilePath.toString());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    Files.writeString(usersFilePath, "[]");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        private void validateWorkingDirectory() {
            File f = new File(this.workspaceDir);
            if (!f.exists()) {
                this.workspaceDir = this.getTempDirectory();
                return;
            }

            if (!f.isDirectory()) {
                this.workspaceDir = this.getTempDirectory();
            }
        }


        @Override
        public String authenticate(String username, String password) throws IllegalArgumentException, JWTCreationException {
            List<User> users;
            User user;
            try {
                String contentString = Files.readString(this.usersFilePath);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<User>>() {}.getType();
                ArrayList<User> outputList = gson.fromJson(contentString, listType);
                users = outputList.stream().filter(t -> t.username.equals(username) && t.password.equals(password))
                        .collect(Collectors.toList());
                if(users.size() == 0) {
                    return "";
                }
                user = users.get(0);
            } catch(Exception e) {
                return "";
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            // The token will expire 1 hour
            calendar.add(Calendar.HOUR_OF_DAY, 12);

            return JWT.create()
                    .withSubject(Constants.JWT_SUBJECT)
                    .withClaim("userId", user.id)
                    .withIssuedAt(new Date())
                    .withExpiresAt(calendar.getTime())
                    .withIssuer(Constants.JWT_ISSUER)
                    .sign(Algorithm.HMAC256(Constants.JWT_SECRET));
        }

        static class User {
            public String id;
            public String username;
            public String password;
        }
    }

}
