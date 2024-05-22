package hdbank.com.accountservice.repository;

import hdbank.com.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
