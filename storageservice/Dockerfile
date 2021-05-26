FROM adoptopenjdk:11-jre-hotspot-bionic

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} storage-service.jar

ENTRYPOINT ["java", "-jar", "storage-service.jar"]