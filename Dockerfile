# ---- Build stage: compile the fat JAR with JDK 17 ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean bootJar --no-daemon

# ---- Run stage: slim JRE 17 image ----
FROM eclipse-temurin:17-jre
WORKDIR /app
# Let the JVM use most of the (small) container memory on free tiers
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"
COPY --from=build /app/build/libs/KUIT4-Server-Auth-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
