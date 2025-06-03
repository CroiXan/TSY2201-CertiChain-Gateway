FROM openjdk:21-jdk

WORKDIR /app

COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar

COPY msp/ ./msp/

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]