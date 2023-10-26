FROM openjdk:8-jdk-alpine
WORKDIR app
ARG ARTIFACT_NAME=core-service-impl/target/core-service-impl-0.0.1-SNAPSHOT.jar
COPY ${ARTIFACT_NAME} /app/core-service-impl-0.0.1-SNAPSHOT.jar
EXPOSE 6057
#ENTRYPOINT "java -jar -Djdk.io.File.enableADS=true -Dserver.profile=dev -Ddb.server.host=postgresql.ch8wfucynpvq.us-east-1.rds.amazonaws.com -Ddb.server.port=5431 -Ddb.schema=cmdb -Ddb.user=postgres -Ddb.password='P0\$tGr3$&s3qua1\$n3t!k5' /app/core-service-impl-0.0.1-SNAPSHOT.jar"
#ENTRYPOINT ["java","-jar","-Djdk.io.File.enableADS=true","-Dserver.profile=dev","-Ddb.server.host=postgresql.ch8wfucynpvq.us-east-1.rds.amazonaws.com","-Ddb.server.port=5431","-Ddb.schema=cmdb","-Ddb.user=postgres","-Ddb.password=\'P0$tGr3$&s3qua1$n3t!k5\'","/app/core-service-impl-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT  sh -c "java -jar -Djdk.io.File.enableADS=true -Dserver.profile=dev -Ddb.server.host=postgresql.ch8wfucynpvq.us-east-1.rds.amazonaws.com -Ddb.server.port=5431 -Ddb.schema=cmdb -Ddb.user=postgres -Ddb.password='Synect!ks2023' /app/core-service-impl-0.0.1-SNAPSHOT.jar"

