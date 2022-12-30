package dev.fennex.clean.data.repository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import dev.fennex.clean.Constants;
import dev.fennex.clean.domain.repository.LoginRepository;

import java.util.Calendar;
import java.util.Date;

public class LoginDataRepository implements LoginRepository {
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
