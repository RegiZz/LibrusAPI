FROM openjdk:23-jdk
WORKDIR /app

# Install dependencies for Chrome and ChromeDriver
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    gnupg2 \
    ca-certificates \
    libxss1 \
    libappindicator1 \
    libindicator7 \
    fonts-liberation \
    libnss3 \
    lsb-release \
    xdg-utils \
    libgbm-dev \
    libgconf-2-4 \
    libx11-xcb1 \
    xvfb

# Install Google Chrome (latest stable version)
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && dpkg -i google-chrome-stable_current_amd64.deb; apt-get -fy install

# Install ChromeDriver (version that matches the installed Chrome)
RUN CHROME_VERSION=$(google-chrome --version | grep -oP '\d+\.\d+\.\d+') \
    && CHROMEDRIVER_VERSION=$(curl -sS https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION) \
    && wget https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip \
    && unzip chromedriver_linux64.zip -d /usr/local/bin/ \
    && chmod +x /usr/local/bin/chromedriver


COPY . /app


RUN ./gradlew build # or ./mvnw package if using Maven


CMD ["java", "-jar", "build/libs/LibrusAPI-0.0.1-alpha.jar"]
