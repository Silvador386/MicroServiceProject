package com.Silvador386;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        // send out an email notification
      log.info("Received a Notification for Order - {}", orderPlacedEvent.getOrderNumber());
    }

}

