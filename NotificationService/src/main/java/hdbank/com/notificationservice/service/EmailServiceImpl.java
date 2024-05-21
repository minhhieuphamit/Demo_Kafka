package hdbank.com.notificationservice.service;

import hdbank.com.notificationservice.model.Message;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("cskh@hdbank.com.vn")
    //@Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendEmail(Message message) {
        try {
            logger.info("Start sending email");

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("name", message.getToName());
            context.setVariable("content", message.getContent());
            String html = templateEngine.process("welcome-email", context);

            helper.setTo(message.getTo());
            helper.setText(html, true);
            helper.setSubject(message.getSubject());
            helper.setFrom(from);
            javaMailSender.send(mimeMessage);

            logger.info("Email sent successfully");
        } catch (Exception e) {
            logger.error("Error when sending email: " + e.getMessage());
        }
    }
}
