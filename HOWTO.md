# How to generate a JWT secret key for a Spring Boot application

## 1. What is this for?

Spring Boot applications often use **JSON Web Tokens (JWTs)** to authenticate users.
A JWT is signed with a _secret key_ - a random string that the server knows but an attacker does not.
The file you will generate in this tutorial contains that key, and you will paste it into `application.yaml` so the application can sign and verify JWTs.

---

## 2. Prerequisites

| Item                             | What it is                                                               | Why you need it                                                           |
| -------------------------------- | ------------------------------------------------------------------------ | ------------------------------------------------------------------------- |
| **Java 17 JDK**                  | The compiler & runtime that runs the code                                | The project is built for Java 17 (see `<java.version>` in `pom.xml`).     |
| **Apache Maven**                 | Build tool that downloads libraries & compiles code                      | The project uses a `pom.xml` that tells Maven what dependencies to fetch. |
| **A terminal / command line**    | Where you will type commands                                             | All instructions are shell commands.                                      |
| **Access to the project folder** | The folder that contains the `pom.xml`, `README.md`, and the Java source | You need to be inside this folder to run the commands.                    |
|                                  |

---

## 3. Project layout

The repository is organized as a standard Maven project.

```txt
.
├── pom.xml
├── README.md
└── SecretGenerator.java
```

**Warning: if the class source file is located in the root of the project, the package declaration must be omitted because in that case it will be considered as a class belonging to the `default package`.**

`SecretGenerator.java` contains the code that creates a random HMAC-SHA256 key and prints it as a Base-64 string.

---

## 4. Build & run the key generator

Open a terminal and navigate to the project folder (the one that contains `pom.xml`).

### 4.1 Clean any old files

```bash
# Remove any previous class-path file or compiled class.
[ -f classpath.txt ] && rm classpath.txt
[ -f SecretGenerator.class ] && rm SecretGenerator.class
```

### 4.2 Download Maven dependencies

```bash
mvn clean && mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt
```

- `mvn clean` removes any old compiled files.
- The second command tells Maven to fetch all the libraries the project needs (the `jjwt-*` jars) and writes the resulting class-path to `classpath.txt`.

### 4.3 Compile the Java class

```bash
javac --class-path "$(cat classpath.txt)" -Xlint:deprecation SecretGenerator.java
```

> _Why we use `--class-path`?_  
> The Java compiler needs to know where the external libraries are; they are listed in `classpath.txt`.

### 4.4 Run the generator

```bash
java --class-path ".:$(cat classpath.txt)" SecretGenerator
```

The program prints a long Base-64 string - that is your JWT secret.
Copy the entire string exactly as shown with final equal sign.

---

## 5. Add the key to `application.yaml`

Open the file that contains your Spring Boot configuration (`application.yaml`).
Find the section that looks like this:

```yaml
app:
  jwt:
    secret: "existing-secret"
    expiration-ms: 3600000
```

Replace `"existing-secret"` with the string you just copied, making sure to keep the quotes:

```yaml
app:
  jwt:
    secret: "complete_secret_key_with_final_equal_sign"
    expiration-ms: 3600000
```

> **Important** - Do _not_ share this file (or the key) in public repositories or with anyone who does not have permission to deploy the application.

---

## 6. Verify everything works

You can optionally start the Spring Boot application to confirm it starts without error.
With the Maven `exec` plugin already configured in `pom.xml`, run:

```bash
mvn exec:java
```

If the application starts and shows no "missing secret" errors, you're all set.

---

## 7. Clean-up (optional)

If you want to tidy the folder after generating the key:

```bash
rm classpath.txt
rm SecretGenerator.class
```

---

## Frequently Asked Questions

| Question                                      | Answer                                                                                                                                                         |
| --------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Why is the key Base-64?**                   | Base-64 is a compact text representation of binary data. JWT libraries expect a string, so we encode the random bytes into Base-64.                            |
| **What if I need a different key algorithm?** | This generator uses HMAC-SHA256 (256-bit key). If you need a different algorithm, you'd change the `KeyGenerator.getInstance("HmacSHA256")` line.              |
| **Should I regenerate the key every time?**   | No, keep the same secret as long as the application runs. Rotate it only when you need to invalidate all old tokens.                                           |
| **How do I rotate the key later?**            | Generate a new key with this tool, update `application.yaml`, and redeploy the app. Existing tokens will no longer be valid, so you may need to log users out. |

---

## Summary

1. **Build** the Maven project to get the libraries.
2. **Compile** and **run** `SecretGenerator` to obtain a random Base-64 key.
3. **Paste** that key into `application.yaml` under `app.jwt.secret`.
4. **Deploy** or start your Spring Boot application.

That's it! The secret key is now ready to secure your JWTs.
