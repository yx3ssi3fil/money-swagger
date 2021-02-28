package com.kakaopay.moneyswagger.repository;

import com.kakaopay.moneyswagger.entity.account.MoneyPortion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyPortionRepository extends JpaRepository<MoneyPortion, Long> {
}
