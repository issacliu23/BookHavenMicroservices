FROM openjdk:11
COPY target/userservice-0.0.1-SNAPSHOT.jar userservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/userservice-0.0.1-SNAPSHOT.jar"]