FROM openjdk:8-jre-alpine
COPY target/dodgydrivers-1.0-jar-with-dependencies.jar /dodgydrivers-1.0.jar
CMD ["/usr/bin/java", "-cp", "/dodgydrivers-1.0.jar", "service.core.Quoter"]
