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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MoneySwaggingService {
    private static final Integer THREE_DIGITS = 3;

    private final MoneySwaggingRepository moneySwaggingRepository;
    private final MoneyPortionRepository moneyPortionRepository;

    public MoneySwagging createMoneySwagging(MoneySwagging moneySwagging) {
        String token = RandomStringUtils.randomAlphabetic(THREE_DIGITS);
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

        List<MoneyPortion> moneyPortions = IntStream.range(1, peopleCount)
                .mapToObj(num -> new MoneyPortion(moneySwagging, base))
                .collect(Collectors.toList());
        moneyPortions.add(new MoneyPortion(moneySwagging, base + bonus));

        return moneyPortionRepository.saveAll(moneyPortions);
    }
}
