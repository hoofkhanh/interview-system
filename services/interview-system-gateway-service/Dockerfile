# -------- BUILD STAGE ---------
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
COPY services ./services
COPY commons ./commons
RUN mvn dependency:go-offline
RUN mvn clean package -pl services/interview-system-gateway-service -am -DskipTests

# -------- RUNTIME STAGE ---------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/services/interview-system-gateway-service/target/*.jar /app/gateway-service.jar
EXPOSE 9004
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]