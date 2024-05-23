package hdbank.com.accountservice.service;

import hdbank.com.accountservice.model.Message;
import hdbank.com.accountservice.model.Statistic;
import hdbank.com.accountservice.repository.MessageRepository;
import hdbank.com.accountservice.repository.StatisticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class PoolingServiceImpl implements PoolingService {
    private Logger logger = LoggerFactory.getLogger(PoolingServiceImpl.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StatisticRepository statisticRepository;

    @Scheduled(fixedDelay = 1000)
    public void producer() {
        List<Message> messages = messageRepository.findByStatus(false);
        for (Message msg : messages) {
            CompletableFuture<SendResult<String, Object>> futureNotification = kafkaTemplate.send("notification", msg);
            futureNotification.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Error: " + ex.getMessage());
                } else {
                    System.out.println("Notification partition: " + result.getRecordMetadata().partition());
                    logger.info("TRUE");
                    msg.setStatus(true);
                    messageRepository.save(msg);
                }
            });
        }

        List<Statistic> statistics = statisticRepository.findByStatus(false);
        for (Statistic stat : statistics) {
            CompletableFuture<SendResult<String, Object>> futureStatistic = kafkaTemplate.send("statistic", stat);
            futureStatistic.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Error: " + ex.getMessage());
                } else {
                    System.out.println("Statistic partition: " + result.getRecordMetadata().partition());
                    logger.info("TRUE");
                    stat.setStatus(true);
                    statisticRepository.save(stat);
                }
            });
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void deleteLogTrue() {
        List<Message> messages = messageRepository.findByStatus(true);
        messageRepository.deleteAllInBatch(messages);

        List<Statistic> statistics = statisticRepository.findByStatus(true);
        statisticRepository.deleteAllInBatch(statistics);
    }
}
