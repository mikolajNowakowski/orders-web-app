FROM openjdk:20
EXPOSE 8080
WORKDIR /web
ADD persistence/src/main/resources resources
ADD api/target/api.jar api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]