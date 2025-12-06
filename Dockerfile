FROM eclipse-temurin:21-jdk

COPY target/workoutproject-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app/app.jar"]