package com.kakaopay.moneyswagger.account.model;

import com.kakaopay.moneyswagger.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
