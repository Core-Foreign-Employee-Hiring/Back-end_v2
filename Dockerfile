# Dockerfile

# jdk17 Image Start
FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=build/libs/backend-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} foreign_Backend.jar
ENTRYPOINT ["java","-Xms500m","-Xmx500m","-Duser.timezone=Asia/Seoul","-jar","foreign_Backend.jar"]
