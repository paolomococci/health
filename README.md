# health

Web application developed for demonstration and example purposes.

![](care/src/main/resources/static/img/healthcare.png)

---

## prerequisites for a minimal application

Make sure `curl` is installed on your Linux system:

```shell
curl --version
```

## request the list of available options

Test connectivity to `Spring Initializr`:

```shell
curl --silent --write-out "HTTP status: %{http_code}\n" --output /dev/null https://start.spring.io/
```

Request for available settings:

```shell
curl --verbose --get https://start.spring.io/
```

**Attention:** Carefully read the output from the online service at this stage.

## download a minimal project

Example of request to generate and download the project:

```shell
curl --verbose --get https://start.spring.io/starter.zip \
  --data language=java \
  --data bootVersion=4.0.6 \
  --data type=gradle-project \
  --data javaVersion=17 \
  --data groupId=local.demo \
  --data artifactId=tiny \
  --data configurationFileFormat=yaml \
  --output tiny.zip \
```

- `--data "parameter=value"`: Adds a query parameter to the request.
- `--output tiny.zip`: Declares the output file where the compressed project will be saved.
- `--get`: Specifies that the request should be made with the HTTP GET verb.
- `https://start.spring.io/starter.zip`: URL of the Spring Initializr service that generates and returns a pre-configured ZIP.

## check the archive

To verify the success of the download you can use the following commands:

```shell
ls -l
file tiny.zip
```

The file obtained must be a valid archive of type `.zip`.

## extract the project

To extract the contents of `.zip`, you can use the `unzip` command:

```shell
unzip -q tiny.zip -d ./tiny-demo
```

## execution of the project

Navigate to the extracted folder and run the application:

```shell
cd tiny-project
# For Gradle
./gradlew bootRun
# For Maven
./mvnw spring-boot:run
```

The `Spring Boot` application will start, log its activity, and then pause because additional starters are required in `build.gradle` or `pom.xml`.

## summary

1. Verify `Spring Boot` starters with `curl`.
2. Download a pre-configured `Spring Boot` project via `Spring Initializr` service.
3. Extract and run the generated boilerplate.

Happy coding!
