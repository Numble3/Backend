package com.numble.team3.account.infra;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.RoleType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaAccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  @Query("SELECT a FROM Account a WHERE a.roleType = :roleType AND a.deleted = :deleted")
  Page<Account> findAllWithAdmin(@Param("roleType") RoleType roleType, @Param("deleted") boolean deleted, Pageable pageable);
}
