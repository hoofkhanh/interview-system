# -------- BUILD STAGE ---------
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
COPY services ./services
RUN mvn dependency:go-offline
RUN mvn clean package -pl services/interview-system-auth-service -am -DskipTests

# -------- RUNTIME STAGE ---------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/services/interview-system-auth-service/target/*.jar /app/auth-service.jar
EXPOSE 9003
ENTRYPOINT ["java", "-jar", "auth-service.jar"]