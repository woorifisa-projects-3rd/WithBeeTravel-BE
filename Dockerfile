# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the local gradle build JAR file to the container
# Gradle 빌드 후 생성되는 JAR 파일을 복사합니다.
COPY ./build/libs/withbeetravel-*.jar /app/withbeetravel.jar

# Run the application
CMD ["java", "-jar", "withbeetravel.jar"]