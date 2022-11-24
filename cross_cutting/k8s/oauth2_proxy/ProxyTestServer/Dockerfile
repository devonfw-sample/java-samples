FROM adoptopenjdk/openjdk11:alpine-jre
RUN mkdir /opt/app
WORKDIR /opt/app
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]