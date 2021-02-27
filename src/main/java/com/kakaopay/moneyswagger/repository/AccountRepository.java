package com.kakaopay.moneyswagger.repository;

import com.kakaopay.moneyswagger.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
