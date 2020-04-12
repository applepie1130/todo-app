FROM openjdk:8-jdk
COPY ./todo-app/build/libs /app/todo-app/build/libs
WORKDIR /app/todo-app/build/libs
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev",  "todo-app.jar"]
