FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV PORT 8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

