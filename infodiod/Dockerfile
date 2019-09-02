FROM openjdk:8
ARG JAR_FILE
RUN mkdir -p /opt/turing/infodiod && mkdir -p /mnt/turing/dump/volume
COPY ${JAR_FILE} /opt/turing/infodiod/app.jar
WORKDIR /opt/turing/infodiod
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]