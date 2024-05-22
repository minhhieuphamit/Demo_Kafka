package hdbank.com.accountservice.controller;

import hdbank.com.accountservice.model.Account;
import hdbank.com.accountservice.model.Message;
import hdbank.com.accountservice.model.Statistic;
import hdbank.com.accountservice.repository.AccountRepository;
import hdbank.com.accountservice.repository.MessageRepository;
import hdbank.com.accountservice.repository.StatisticRepository;
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

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    StatisticRepository statisticRepository;

    @PostMapping("/new")
    public Account create(@RequestBody Account account) {
        Statistic statistic = new Statistic("Account " + account.getName() + " is created ", new Date());
        statistic.setStatus(false);

        Message message = new Message();
        message.setTo(account.getEmail());
        message.setToName(account.getName());
        message.setSubject("Welcome to HDBank");
        message.setContent("You have successfully created an account at HDBank");
        message.setStatus(false);

        accountRepository.save(account);
        messageRepository.save(message);
        statisticRepository.save(statistic);

        /*CompletableFuture<SendResult<String, Object>> futureNotification = kafkaTemplate.send("notification", message);
        CompletableFuture<SendResult<String, Object>> futureStatistic = kafkaTemplate.send("statistic", statistic);
        for (int i = 0; i < 100; i++) {

            futureNotification.whenComplete((result, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                } else {
                    System.out.println("Notification partition: " + result.getRecordMetadata().partition());
                }
            });

            futureStatistic.whenComplete((result, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                } else {
                    System.out.println("Statistic partition: " + result.getRecordMetadata().partition());
                }
            });
        }*/
//        kafkaTemplate.send("notification", message);
//        kafkaTemplate.send("statistic", statistic);
        return account;
    }
}
