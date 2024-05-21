package hdbank.com.accountservice.controller;

import hdbank.com.accountservice.model.Account;
import hdbank.com.accountservice.model.Message;
import hdbank.com.accountservice.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/new")
    public Account create(@RequestBody Account account) {
        Statistic statistic = new Statistic("Account " + account.getName() + " is created ", new Date());

        Message message = new Message();
        message.setTo(account.getEmail());
        message.setToName(account.getName());
        message.setSubject("Welcome to HDBank");
        message.setContent("You have successfully created an account at HDBank");

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("notification", message);
        for (int i = 0; i < 1000; i++) {
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                } else {
                    System.out.println(result.getRecordMetadata().partition());
                }
            });
        }
        kafkaTemplate.send("statistic", statistic);

        return account;
    }
}
