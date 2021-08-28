FROM openjdk:11

COPY target/demo*.jar demo.jar

ENTRYPOINT ["java", "-jar", "demo.jar"]
