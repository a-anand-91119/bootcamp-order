FROM docker.io/gradle:8.14.3-jdk24 AS dependencies
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY libs.versions.toml .
COPY spotless.xml .

RUN ./gradlew --no-daemon dependencies

FROM dependencies AS builder
WORKDIR /app

COPY src src

RUN ./gradlew --no-daemon clean build -x test -x spotlessCheck

FROM docker.io/amazoncorretto:24-alpine AS runtime
WORKDIR /app

RUN addgroup -g 1001 -S appgroup && \
    adduser -S -D -H -u 1001 -s /sbin/nologin -G appgroup appuser

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=100 -Djava.security.manager=allow"

COPY --from=builder /app/build/libs/bootcamp-order-0.0.1-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

USER appuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
