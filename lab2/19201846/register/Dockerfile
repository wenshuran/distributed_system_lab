FROM openjdk:8-jre-alpine
COPY target/register-1.0-jar-with-dependencies.jar /register-1.0.jar
CMD ["/usr/bin/java", "-cp", "/register-1.0.jar", "LRSServer"]
