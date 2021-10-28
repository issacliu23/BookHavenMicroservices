FROM openjdk:11
COPY target/paymentservice-0.0.1-SNAPSHOT.jar paymentservice-0.0.1-SNAPSHOT.jar
EXPOSE 80
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","paymentservice-0.0.1-SNAPSHOT.jar"]