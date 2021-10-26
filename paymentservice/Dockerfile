FROM openjdk:11
COPY target/paymentservice-0.0.1-SNAPSHOT.jar paymentservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","paymentservice-0.0.1-SNAPSHOT.jar"]