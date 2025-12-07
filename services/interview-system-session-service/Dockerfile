# -------- BUILD STAGE ---------
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
COPY services ./services
COPY commons ./commons
RUN mvn dependency:go-offline
RUN mvn clean package -pl services/interview-system-session-service -am -DskipTests

# -------- RUNTIME STAGE ---------
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /build/services/interview-system-session-service/target/*.jar /app/session-service.jar
EXPOSE 9006
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java", "-Duser.timezone=Asia/Ho_Chi_Minh", "-jar", "session-service.jar"]
