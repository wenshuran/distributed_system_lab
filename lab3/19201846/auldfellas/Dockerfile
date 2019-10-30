FROM openjdk:8-jre-alpine
COPY target/auldfellas-1.0-jar-with-dependencies.jar /auldfellas-1.0.jar
CMD ["/usr/bin/java", "-cp", "/auldfellas-1.0.jar", "service.core.Quoter"]
