package com.example.worker;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.sql.*;

public class OrderWorker {

    public static void main(String[] args) {
        // Read environment variables (set in Dockerfile/Compose)
        String mqUrl = System.getenv().getOrDefault("MQ_URL", "tcp://localhost:61616");
        String mqQueue = System.getenv().getOrDefault("MQ_QUEUE", "orders");
        String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
        String dbUser = System.getenv().getOrDefault("DB_USER", "app");
        String dbPass = System.getenv().getOrDefault("DB_PASS", "app_pass");
        String dbName = System.getenv().getOrDefault("DB_NAME", "orders");

        String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true";

        System.out.println("Worker starting... connecting to MQ: " + mqUrl + " and DB: " + jdbcUrl);

        try {
            // Connect to ActiveMQ
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(mqUrl);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(mqQueue);

            MessageConsumer consumer = session.createConsumer(destination);

            // Listen for messages
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        String orderId = ((TextMessage) message).getText();
                        System.out.println("Received order ID: " + orderId);

                        // Update DB status
                        try (Connection dbConn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {
                            PreparedStatement stmt = dbConn.prepareStatement(
                                "UPDATE orders SET status=? WHERE order_id=?"
                            );
                            stmt.setString(1, "PROCESSED");
                            stmt.setString(2, orderId);
                            int rows = stmt.executeUpdate();
                            System.out.println("Updated " + rows + " row(s) for order " + orderId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("Worker is now listening for messages...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
