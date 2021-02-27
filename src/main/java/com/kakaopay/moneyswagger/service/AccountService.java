package com.kakaopay.moneyswagger.service;

import com.kakaopay.moneyswagger.entity.account.Account;
import com.kakaopay.moneyswagger.entity.member.Member;
import com.kakaopay.moneyswagger.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account createAccount(Member member) {
        Account account = Account.builder()
                .member(member)
                .balance(BigDecimal.ZERO)
                .build();

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Optional<Account> retrieveById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public Account deposit(Account account, Integer depositAmount) {
        account.deposit(depositAmount);
        return accountRepository.save(account);
    }
}
