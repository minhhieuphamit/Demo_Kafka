package hdbank.com.accountservice.repository;

import hdbank.com.accountservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByStatus(boolean status);
}
