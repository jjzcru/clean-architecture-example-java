package dev.fennex.clean.presentation.controllers.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.fennex.clean.Constants;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
    private static final String secret = Constants.JWT_SECRET;
    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(Constants.JWT_SUBJECT)
                .withIssuer(Constants.JWT_ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userId").asString();
    }
}
