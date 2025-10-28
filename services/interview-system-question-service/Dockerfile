# -------- BUILD STAGE ---------
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
COPY services ./services
COPY commons ./commons
RUN mvn dependency:go-offline
RUN mvn clean package -pl services/interview-system-question-service -am -DskipTests

# -------- RUNTIME STAGE ---------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/services/interview-system-question-service/target/*.jar /app/question-service.jar
EXPOSE 9005
ENTRYPOINT ["java", "-jar", "question-service.jar"]