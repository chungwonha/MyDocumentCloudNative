FROM adoptopenjdk:11-jre-hotspot-bionic

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} searchservice.jar

ENTRYPOINT ["java", "-jar", "searchservice.jar"]