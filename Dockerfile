FROM eclipse-temurin:17-jdk

ARG JAR_FILE=target/guestbook-service-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} guestbook-service.jar

ENTRYPOINT ["java", "-jar", "/guestbook-service.jar"]