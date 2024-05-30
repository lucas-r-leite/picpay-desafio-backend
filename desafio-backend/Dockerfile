FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY target/desafio-backend-0.0.1-SNAPSHOT.jar app.jar
ARG db
ARG user
ARG password
ENV SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3306/${db}?createDatabaseIfNotExist=true
ENV SPRING_DATASOURCE_USERNAME=${user}
ENV SPRING_DATASOURCE_PASSWORD=${password}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]