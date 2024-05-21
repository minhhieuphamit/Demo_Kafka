package hdbank.com.notificationservice.service;

import hdbank.com.notificationservice.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailService emailService;

    @Override
    @KafkaListener(id = "notificationGroup", topics = "notification")
    public void listenMessage(Message message) {
        logger.info("Received message: " + message.getTo());
        emailService.sendEmail(message);
    }
}
