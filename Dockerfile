FROM openjdk:19-jdk AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw --projects :desafio-api --also-make clean package

FROM openjdk:19-jdk
VOLUME /tmp

COPY --from=build /app/desafio-api/target/*exec.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

