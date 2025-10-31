# Use a lightweight base image with Java 17
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled jar file from the build process
# The 'build/libs/app.jar' path comes from our build.gradle configuration
COPY build/libs/app.jar .

# Expose the port the application will run on (default for Spring Boot is 8080)
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
