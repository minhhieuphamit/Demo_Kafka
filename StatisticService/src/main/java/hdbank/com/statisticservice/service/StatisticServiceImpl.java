package hdbank.com.statisticservice.service;

import hdbank.com.statisticservice.entity.Statistic;
import hdbank.com.statisticservice.repository.StatisticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StatisticRepository statisticRepository;

    @KafkaListener(id = "statisticGroup", topics = "statistic")
    public void listen(Statistic statistic) {
        logger.info("Received statistic: " + statistic.getMessage());
//        statisticRepository.save(statistic);
        throw new RuntimeException();
    }

    @KafkaListener(id = "dltGroup", topics = "statistic.DLT")
    public void dltListen(Statistic statistic) {
        logger.info("Received from DLT: " + statistic.getMessage());
    }
}
