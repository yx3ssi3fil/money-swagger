package com.kakaopay.moneyswagger.service;

import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import com.kakaopay.moneyswagger.repository.MoneySwaggingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MoneySwaggingService {
    private final MoneySwaggingRepository moneySwaggingRepository;

    public MoneySwagging createMoneySwagging(MoneySwagging moneySwagging) {
        String token = RandomStringUtils.randomAlphabetic(3);
        moneySwagging.assignToken(token);

        return moneySwaggingRepository.save(moneySwagging);
    }
}
