package com.numble.team3.account.infra;

import com.numble.team3.account.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByEmail(String email);
}
