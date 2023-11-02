FROM openjdk:8-jdk-alpine
WORKDIR app
ARG ARTIFACT_NAME=core-service-impl/target/core-service-impl-0.0.1-SNAPSHOT.jar
COPY ${ARTIFACT_NAME} /app/core-service-impl-0.0.1-SNAPSHOT.jar
EXPOSE 6057
ENTRYPOINT sh -c "java -jar -Djdk.io.File.enableADS=true -Dserver.profile=dev /app/core-service-impl-0.0.1-SNAPSHOT.jar"
