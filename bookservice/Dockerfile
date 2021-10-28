FROM openjdk:11
COPY target/bookservice-0.0.1-SNAPSHOT.jar bookservice-0.0.1-SNAPSHOT.jar
EXPOSE 80
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","bookservice-0.0.1-SNAPSHOT.jar"]