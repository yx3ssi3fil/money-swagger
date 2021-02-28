package com.kakaopay.moneyswagger.moneyswagging.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoneySwaggingRepository extends JpaRepository<MoneySwagging, Long> {
    Optional<MoneySwagging> findByToken(String token);
}
