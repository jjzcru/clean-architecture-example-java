package dev.fennex.clean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> this.printEnv();
    }

    private void printEnv() {
        logger.info("The application is running on port :8080");
        logger.info("ENV VARIABLES");
        logger.info(String.format("STORAGE_TYPE: \"%s\"", System.getenv("STORAGE_TYPE")));
        logger.info(String.format("WORKING_DIRECTORY: \"%s\"", System.getenv("WORKING_DIRECTORY")));
        logger.info(String.format("JWT_ISSUER: \"%s\"", System.getenv("JWT_ISSUER")));
        logger.info(String.format("JWT_SECRET: \"%s\"", System.getenv("JWT_SECRET")));
    }

}