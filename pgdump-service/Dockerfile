FROM ppod/turing-pgdump:0.0.1
ARG JAR_FILE
RUN mkdir -p /opt/turing/dump && mkdir -p /mnt/turing/dump/volume
COPY ${JAR_FILE} /opt/turing/dump/app.jar
WORKDIR /opt/turing/dump
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","app.jar"]