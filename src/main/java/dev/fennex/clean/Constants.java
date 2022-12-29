package dev.fennex.clean;

public class Constants {
    public static String JWT_SECRET = System.getenv("JWT_SECRET") != null ? System.getenv("JWT_SECRET") : "4IDDUnV0cgjLejsKMrUc";
    public static String JWT_ISSUER = System.getenv("JWT_ISSUER") != null ? System.getenv("JWT_ISSUER") : "Fennex";
    public static String JWT_SUBJECT = "User details";
}


