FROM gradle:9.1.0-jdk25-alpine AS build

WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./
COPY gradle.properties* ./

RUN chmod +x gradlew

COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies --no-daemon || true

COPY src ./src

RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown -R spring:spring /app

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

CMD ["-Djava.security.egd=file:/dev/./urandom", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0"]
