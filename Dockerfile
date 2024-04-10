FROM openjdk:21-slim
EXPOSE 8282
WORKDIR /app

ARG JAR_FILE
COPY ${JAR_FILE} /app/user.jar
CMD ["java", "-jar", "user.jar"]
