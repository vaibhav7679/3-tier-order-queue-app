FROM eclipse-temurin:11-jre
WORKDIR /app
COPY target/orders-worker.jar /app/app.jar
ENV DB_HOST=db DB_USER=app DB_PASS=app_pass DB_NAME=orders \
    MQ_URL=tcp://activemq:61616 MQ_QUEUE=orders
ENTRYPOINT ["java","-jar","/app/app.jar"]
