package hdbank.com.notificationservice.service;

import hdbank.com.notificationservice.model.Message;

public interface EmailService {
    public void sendEmail(Message message);
}
