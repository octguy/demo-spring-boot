# ============================
#         BUILD STAGE
# ============================
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

# --- Copy Gradle Wrapper first (maximize caching) ---
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# --- Pre-download dependencies for caching ---
RUN ./gradlew dependencies --no-daemon || true

# --- Copy source code ---
COPY src ./src

# --- Build JAR ---
RUN ./gradlew clean build -x test --no-daemon


# ============================
#        RUNTIME STAGE
# ============================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# --- Security: non-root user ---
RUN addgroup -S spring && adduser -S spring -G spring

# --- Copy final JAR ---
COPY --from=build /app/build/libs/*.jar app.jar

USER spring:spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
