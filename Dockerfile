FROM openjdk:17-alpine

# Install dependencies
RUN apk add --no-cache curl bash chromium chromium-chromedriver

# Set ChromeDriver and Chrome binary paths
ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROME_DRIVER=/usr/bin/chromedriver

# Copy your app's code to the container
COPY . /app
WORKDIR /app

CMD ["./gradlew", "bootRun"]
