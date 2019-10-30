FROM openjdk:8-jre-alpine
COPY target/girlpower-1.0-jar-with-dependencies.jar /girlpower-1.0.jar
CMD ["/usr/bin/java", "-cp", "/girlpower-1.0.jar", "service.core.Quoter"]
