# ==============================
# 0 - ARGS
# ==============================
ARG JAVA_VERSION=17

# ==============================
# 1 - BUILD STAGE
# ==============================
FROM eclipse-temurin:${JAVA_VERSION}-jdk-noble AS build

# 1.0 Keep root permissions for apt, but later change user and add non-root user.
USER root

RUN apt-get update && apt-get upgrade -y \ 
    && apt-get clean \ 
    && rm -rf /var/lib/apt/lists/* \ 
    && addgroup --system appuser \ 
    && adduser --system --disabled-login --home /home/appuser appuser



# 1.1 Switch to non-root user.
WORKDIR /app
RUN chown --recursive appuser:appuser /app /home/appuser
USER appuser

# 1.2 Copy files with ownership setting in one step.
COPY --chown=appuser:appuser gradlew .
COPY --chown=appuser:appuser gradle/ gradle/
COPY --chown=appuser:appuser build.gradle settings.gradle ./

# 1.3 Pre-fetch deps.
RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies

# 1.4 Copy source.
COPY --chown=appuser:appuser src/ src/

# 1.5 Build fat jar archive.
RUN ./gradlew --no-daemon bootJar -x test

# ==============================
# 2 - RUNTIME STAGE
# ==============================
FROM eclipse-temurin:${JAVA_VERSION}-jre-alpine-3.23 AS runtime
WORKDIR /app

# 2.0 Create non-root user and install curl for health check.
RUN addgroup -S appuser \ 
    && adduser -S -D -G appuser appuser \ 
    && apk add --no-cache curl

# 2.1 Copy jar archive.
COPY --from=build --chown=appuser:appuser /app/build/libs/app.jar app.jar

# 2.2 Create a volume.
RUN mkdir data && chown --recursive appuser:appuser .
VOLUME ["/app/data"]

# 2.3 Expose port.
EXPOSE 8080

# 2.4 Health-check on "/check" API endpoint.
HEALTHCHECK --start-period=30s --interval=15s --timeout=5s --retries=3 \ 
    CMD curl -fsS http://localhost:8080/check || exit 1

# 2.5 Switch to non-root user at work.
USER appuser

# 2.6 Entrypoint.
ENTRYPOINT ["java","-jar","app.jar"]