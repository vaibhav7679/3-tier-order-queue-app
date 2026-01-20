FROM tomcat:9-jdk11
COPY target/orders-backend.war /usr/local/tomcat/webapps/ROOT.war
ENV DB_HOST=db DB_USER=app DB_PASS=app_pass DB_NAME=orders \
    MQ_URL=tcp://activemq:61616 MQ_QUEUE=orders
