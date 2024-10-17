# Use OpenJDK 17 Alpine base image for a minimal Java environment
FROM openjdk:17-alpine

# Install Chromium, ChromeDriver, and other dependencies
RUN apk add --no-cache \
    chromium \
    chromium-chromedriver \
    curl \
    bash

# Set environment variables for Chrome and ChromeDriver
ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_DRIVER=/usr/bin/chromedriver

# Expose port 8080 for your API
EXPOSE 8080

# Create an app directory and set it as the working directory
WORKDIR /app

# Copy your project files into the container
COPY . /app

# Set the entrypoint to run the API using gradle
CMD ["./gradlew", "bootRun"]
