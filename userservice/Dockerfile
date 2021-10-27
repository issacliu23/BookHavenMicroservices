FROM openjdk:11
COPY target/userservice-0.0.1-SNAPSHOT.jar userservice-0.0.1-SNAPSHOT.jar
EXPOSE 80
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","userservice-0.0.1-SNAPSHOT.jar"]