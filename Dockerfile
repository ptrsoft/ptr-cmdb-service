FROM openjdk:8-jdk-alpine
WORKDIR app
ARG ARTIFACT_NAME=core-service-impl/target/core-service-impl-0.0.1-SNAPSHOT.jar
COPY ${ARTIFACT_NAME} /app/core-service-impl-0.0.1-SNAPSHOT.jar
EXPOSE 6057
ENTRYPOINT ["java","-jar","/app/core-service-impl-0.0.1-SNAPSHOT.jar"]
