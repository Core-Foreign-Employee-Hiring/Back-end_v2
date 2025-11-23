# Dockerfile

# jdk17 Image Start
FROM eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=build/libs/backend-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} foreign_Backend.jar
ENTRYPOINT ["java","-Xmx512m","-XX:+UseG1GC","-Xlog:gc*:file=/var/log/gc.log:time,uptime,level,tags","-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=/var/log","-Duser.timezone=Asia/Seoul","-jar","foreign_Backend.jar"]
