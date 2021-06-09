FROM openjdk:11-jre-slim-sid

WORKDIR /usr/src/app

COPY target/demo*.jar demo.jar

ENTRYPOINT ["java", "-jar", "demo.jar"]
