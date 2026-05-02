# `secret-generator` console application

## first project layout

Assuming all files are at the same level.
With the following command:

```shell
tree -L 1
```

I got the following project tree:

```txt
.
├── pom.xml
├── README.md
└── SecretGenerator.java
```

### how to compile with the manual method

**Warning: if the class source file is located in the root of the project, the package declaration must be omitted because in that case it will be considered as a class belonging to the `default package`.**

Shell command to download dependencies associated with the `pom.xml` file:

```shell
[ -f classpath.txt ] && rm classpath.txt && [ -f SecretGenerator.class ] && rm SecretGenerator.class
mvn clean && mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt
```

Once the `classpath.txt` file has been generated, I can proceed with compiling the application:

```shell
javac --class-path "$(cat classpath.txt)" -Xlint:deprecation SecretGenerator.java
```

and with the execution:

```shell
java --class-path ".:$(cat classpath.txt)" SecretGenerator
```

## how to use standard methods

### project layout

The Java main class must now be moved to a standard location, ensuring that the project is structured as illustrated in the layout below.

With the following command:

```shell
tree -L 8
```

I got the following project tree:

```txt
.
├── pom.xml
├── README.md
└── src
    └── main
        └── java
            └── local
                └── example
                    └── secret
                        └── generator
                            └── SecretGenerator.java
```

### shell commands

```shell
mkdir --parents src/main/java/local/example/secret/generator && mv SecretGenerator.java ./src/main/java/local/example/secret/generator/
mvn clean compile
mvn exec:java -Dexec.mainClass="local.example.secret.generator.SecretGenerator" -e
```

Or better yet::

```shell
mkdir --parents src/main/java/local/example/secret/generator && mv SecretGenerator.java ./src/main/java/local/example/secret/generator/
mvn clean compile
mvn exec:java
```

after making sure that the `pom.xml` file has the following setting:

```xml
            <!-- 4.2 Exec plugin to run classes with main. -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.6.3</version>
                <configuration>
                    <mainClass>local.example.secret.generator.SecretGenerator</mainClass>
                </configuration>
            </plugin>
```

a secret key will be generated to add, for example, to file `application.yaml` as follows:

```yaml
spring:
  application:
    name: sample
server:
  port: 8080
datasource:
  url: jdbc:h2:mem:sample_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
  driver-class-name: org.h2.Driver
  username: sa
  password:
jpa:
  hibernate:
    ddl-auto: update
  properties:
    hibernate:
      format_sql: true
h2:
  console:
    enabled: true
    path: /h2-console

# JWT properties, simple secret for demo, rotate in prod.
app:
  jwt:
    secret: "complete_secret_key_with_final_equal_sign"
    expiration-ms: 3600000 # one hour

logging:
  level:
    root: INFO
    local.sample: DEBUG
```
