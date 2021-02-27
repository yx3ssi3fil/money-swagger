package com.kakaopay.moneyswagger.repository;

import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneySwaggingRepository extends JpaRepository<MoneySwagging, Long> {
}
