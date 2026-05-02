# `deployment` notes

Below is a line-by-line explanation of every shell command in use.

---

## 1. General Docker introspection

| Command                    | Purpose                                                                                                        |
| -------------------------- | -------------------------------------------------------------------------------------------------------------- |
| `docker ps --all`          | List **all** containers, including stopped ones. Shows container ID, image, command, status, ports, and names. |
| `docker images`            | List all locally stored images, with repository, tag, image ID, size, and creation time.                       |
| `docker help build`        | Display the help text for the `docker build` command.                                                          |
| `docker help run`          | Show the help documentation for `docker run`.                                                                  |
| `docker help container`    | Short help on container-related sub-commands (e.g., `ls`, `stop`, `rm`).                                       |
| `docker help container ls` | Help for the `docker container ls` command (list containers).                                                  |
| `docker help logs`         | Help for the `docker logs` command (view container logs).                                                      |
| `docker help exec`         | Help for `docker exec` (run a command in a running container).                                                 |
| `docker help stop`         | Help for stopping containers.                                                                                  |
| `docker help rm`           | Help for removing containers.                                                                                  |

---

## 2. Inspecting the JDK image (`eclipse-temurin:17-jdk-noble`)

```bash
docker pull eclipse-temurin:17-jdk-noble
```

_Download the latest Eclipse Temurin JDK 17 image tagged `noble` from Docker Hub._

```bash
docker images
```

_Verify that the image is now present locally._

```bash
docker image inspect --format '{{.RepoTags}} {{.RepoDigests}}' eclipse-temurin:17-jdk-noble
```

_Print the image's tags and digests in a concise, one-line format._

```bash
docker image inspect eclipse-temurin:17-jdk-noble
```

_Output the full JSON inspection report for the image, showing layers, environment variables, labels, etc._

```bash
docker image inspect eclipse-temurin:17-jdk-noble | grep license
```

_Filter the inspection output for any license information._

```bash
docker image inspect --format '{{json .Config.Labels}}' eclipse-temurin:17-jdk-noble | jq
```

_Show the image's configuration labels as pretty-printed JSON with `jq`._

**`jq` is a lightweight, powerful command-line JSON processor.
Think of it as **`sed` or `awk` for JSON**: it can read JSON data from a file or standard input, transform it, filter it, and output the result-all in a single command line.**

```bash
docker run --rm --entrypoint=dpkg eclipse-temurin:17-jdk-noble -l
```

_Run the image in a temporary container, replacing the default entrypoint with `dpkg -l` to list all installed Debian packages._

---

## 3. Inspecting the JRE image (`eclipse-temurin:17-jre-alpine-3.23`)

```bash
docker pull eclipse-temurin:17-jre-alpine-3.23
```

_Pull the Alpine-based JRE 17 image._

```bash
docker images
```

_Check that the image was successfully downloaded._

```bash
docker image inspect --format '{{.RepoTags}} {{.RepoDigests}}' eclipse-temurin:17-jre-alpine-3.23
```

_Show tags and digests for quick reference._

```bash
docker image inspect eclipse-temurin:17-jre-alpine-3.23
```

_Display the full inspection JSON for the Alpine JRE image._

---

## 4. Building the application image (`care-api-img:latest`)

```bash
docker build --tag care-api-img:latest .
```

_Build a Docker image from the current directory's `Dockerfile`, tagging it as `care-api-img:latest`._

```bash
docker run --interactive --tty --rm --memory=512m --cpus=1.0 --publish 8080:8080 --name care-api-latest-int-cntr --pull=never care-api-img:latest java -jar app.jar
```

_Run the freshly built image in an interactive terminal, removing the container on exit, limiting memory to 512 MiB, one CPU, and exposing port 8080. The command starts the Spring Boot application directly from the JAR._

---

## 5. Running the container locally for development

```bash
mkdir data
```

_Create a `data` directory in the current folder - used later for mounting as a volume._

```bash
docker run --user $(id -u):$(id -g) \
           --volume $(pwd)/data:/app/data \
           --memory=512m --cpus=1.0 \
           --detach \
           --publish 8080:8080 \
           --name care-api-latest-cntr \
           --pull=never \
           care-api-img:latest
```

_Start the application in detached mode:_

- _Run as the current host user (`$(id -u):$(id -g)`) to avoid permission issues on mounted files._
- _Mount the local `data` folder into the container at `/app/data`._
- _Limit resources to 512 MiB RAM and one CPU._
- _Expose container port 8080 to host port 8080._
- _Name the container `care-api-latest-cntr`._
- _Skip pulling a new image (`--pull=never`) because we just built it._

```bash
docker container ls --all --size
```

_List all containers, including their disk usage._

```bash
docker logs --follow care-api-latest-cntr
```

_Stream the container's logs to the terminal._

```bash
docker exec --interactive --tty --privileged care-api-latest-cntr sh
```

_Open an interactive shell inside the running container with elevated privileges._

---

## 6. Cleaning up after testing

### 6.1 Stop and remove the container

```bash
docker stop care-api-latest-cntr
```

_Gracefully stop the container._

```bash
docker rm care-api-latest-cntr
```

_Delete the stopped container and free its resources._

```bash
docker container ls --all
```

_Confirm that the container no longer appears in the list._

### 6.2 Remove the image

```bash
docker image rm care-api-img:latest
```

_Delete the `care-api-img:latest` image from the local registry._

```bash
docker images --all
```

_Show all images to verify the removal._

---

## 7. Building and running a development-profile image

```bash
docker build --tag care-api-img:dev .
```

_Build a new image tagged `care-api-img:dev` from the same Dockerfile._

```bash
docker run --user $(id -u):$(id -g) \
           --volume $(pwd)/data:/app/data \
           --memory=512m --cpus=1.0 \
           --detach \
           --publish 8080:8080 \
           --env SPRING_PROFILES_ACTIVE=dev \
           --name care-api-dev \
           --pull=never \
           care-api-img:dev
```

_Run the dev image with:_

- _Same user, volume, memory, CPU limits, and port mapping as before._
- _Set the Spring Boot active profile to `dev` via an environment variable._
- _Name the container `care-api-dev`._

---

## 8. Building and running a production-profile image

```bash
docker build --tag care-api:prod .
```

_Build an image tagged `care-api:prod` (note the different tag)._

```bash
docker run --user $(id -u):$(id -g) \
           --volume $(pwd)/data:/app/data \
           --memory=512m --cpus=1.0 \
           --detach \
           --publish 8080:8080 \
           --env SPRING_PROFILES_ACTIVE=prod \
           --name care-api-prod \
           --pull=never \
           care-api-img:prod
```

_Run the prod image:_

- _Use the same resource constraints and volume mapping._
- _Activate the `prod` Spring profile._
- _Name the container `care-api-prod`._

---

## 9. Summary

- **Inspecting images** (`docker image inspect`) lets you see tags, digests, labels, and the full configuration.
- **Running** (`docker run`) can be tailored with user IDs, volumes, resource limits, port mappings, and environment variables.
- **Stopping/Removing** (`docker stop`/`docker rm`) cleans up containers; `docker image rm` removes images.
- **Building** (`docker build`) can be repeated with different tags for development and production.

These comments should help anyone read this notes and understand exactly what each Docker command is doing.

**Please note: in my case the execution environments, ('./care/src/main/resources/application\*.yaml') are defined almost identically, but everything can be customized.**
