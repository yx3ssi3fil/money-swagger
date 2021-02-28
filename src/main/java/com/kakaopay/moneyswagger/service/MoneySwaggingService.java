package com.kakaopay.moneyswagger.service;

import com.kakaopay.moneyswagger.entity.account.MoneyPortion;
import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import com.kakaopay.moneyswagger.repository.MoneyPortionRepository;
import com.kakaopay.moneyswagger.repository.MoneySwaggingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MoneySwaggingService {
    private final MoneySwaggingRepository moneySwaggingRepository;
    private final MoneyPortionRepository moneyPortionRepository;

    public MoneySwagging createMoneySwagging(MoneySwagging moneySwagging) {
        String token = RandomStringUtils.randomAlphabetic(3);
        moneySwagging.assignToken(token);
        List<MoneyPortion> allMoneyPortion = createAllMoneyPortion(moneySwagging);
        moneySwagging.assignMoneyPortions(allMoneyPortion);

        return moneySwaggingRepository.save(moneySwagging);
    }

    private List<MoneyPortion> createAllMoneyPortion(MoneySwagging moneySwagging) {
        Integer amount = moneySwagging.getAmount();
        Integer peopleCount = moneySwagging.getPeopleCount();

        int base = amount / peopleCount;
        int bonus = amount % peopleCount;

        List<MoneyPortion> moneyPortions = new ArrayList<>();
        for (int i = 0; i < peopleCount - 1; i++) {
            MoneyPortion moneyPortion = MoneyPortion.builder()
                    .moneySwagging(moneySwagging)
                    .chatRoom(moneySwagging.getChatRoom())
                    .amount(base)
                    .build();
            moneyPortions.add(moneyPortion);
        }

        MoneyPortion moneyPortion = MoneyPortion.builder()
                .moneySwagging(moneySwagging)
                .chatRoom(moneySwagging.getChatRoom())
                .amount(base + bonus)
                .build();
        moneyPortions.add(moneyPortion);

        return moneyPortionRepository.saveAll(moneyPortions);
    }
}
