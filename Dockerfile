FROM maven:4.0.0-rc-4-eclipse-temurin-25 AS build

WORKDIR /app

COPY JobApplication/pom.xml .
COPY JobApplication/src src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]