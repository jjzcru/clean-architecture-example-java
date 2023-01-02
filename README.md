# Clean Architecture Example

This project works as an implementation for the clean architecture, this application is build using [Spring Web](https://spring.io/guides/gs/serving-web-content/) and [Spring Security](https://spring.io/projects/spring-security).

This application required:
- Gradle 4+
- Java 17+

## Getting Started

To run the application run `./gradlew bootRun` in the root directory this will start the server in the port `8080`.

### Build
To build the application you can run the command `./gradlew build` this will generate a `.jar` file in `build/libs/clean-{version}.jar` which can be run using the following command.

```
java -jar build/libs/clean-{version}.jar
```

## API
The API of the application can be found using the [OpenApi Specification](docs/OpenApi.yml) in the [Swagger Editor](https://editor.swagger.io/).

There is also a set of requests for different Rest clients
- [Paw](https://paw.cloud/): You can download the file from [here](docs/To-do-example.paw). 
- [Insomnia](https://insomnia.rest/): You can download the file from [here](docs/Insomnia.json).

## Env Variables
This are the env variables that were used to build the application.

| Variable | Values | Description |
|:--|:--|:--|
| `STORAGE_TYPE` | `memory`, `file` | <p>This variable controls the type of storage used by the application.</p> <p>- `memory`: handles everything in the RAM if the server restart all the store data is loss.</p> <p>- `file`: Handles the data using the file system it uses the env variable `WORKING_DIRECTORY` if set, if the path does not exist or is invalid it will use the temporary directory of the OS.</p>  |
| `WORKING_DIRECTORY` | `directory path` | <p>Is the directory that is used the the `file` storage type, it uses files to store the data of the application:</p><p>- `users.json`: Store the information about the users</p><p>- `todos.json`: Store the information about the `todos` and the `users` that are attach to it.|
| `JWT_ISSUER`  | `string` | Is the application that is signing the `jwt` token.  |
| `JWT_SECRET` | `string` | Is the secret used to sign the `jwt`. |