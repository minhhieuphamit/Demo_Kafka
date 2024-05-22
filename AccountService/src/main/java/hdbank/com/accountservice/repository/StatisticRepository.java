package hdbank.com.accountservice.repository;

import hdbank.com.accountservice.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Integer> {
    List<Statistic> findByStatus(boolean status);

}
