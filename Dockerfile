FROM amazoncorretto:17
# FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} with-bee.jar
# COPY build/libs/*.jar my-project.jar
ENTRYPOINT ["java","-jar","/with-bee.jar"]

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime