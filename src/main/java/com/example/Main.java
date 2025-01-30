package com.example;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void sendMessageToOpenWire(String host, int port, String queue, String message) {
        String brokerUrl = "tcp://" + host + ":" + port;

        try {
            // Create a connection factory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

            // Create a connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a destination (queue)
            Destination destination = session.createQueue(queue);

            // Create a message producer
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Send the message
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);

            System.out.println("Message sent to queue '" + queue + "': " + message);

            // Clean up
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Define connection parameters
        String host = "localhost";
        int port = 61616; // OpenWire port for ActiveMQ
        String queue = "local.report.session";

        // Create the message model
        Map<String, Object> filter = new HashMap<>();
        filter.put("request-id", "RYAN5");
        filter.put("client-id", "198");
        filter.put("division-id", "10469");
        filter.put("sender-id", "");
        filter.put("start-date", "2024-10-01 00:00:00");
        filter.put("end-date", "2024-10-04 23:59:59");
        filter.put("conversation-type", "");
        filter.put("report-format", 1);

        Map<String, Object> messageModel = new HashMap<>();
        messageModel.put("filter", filter);

        // Convert message model to JSON
        String message = new Gson().toJson(messageModel);

        // Send the message
        sendMessageToOpenWire(host, port, queue, message);
    }
}
