package com.kakaopay.moneyswagger.account;

import com.kakaopay.moneyswagger.account.model.Account;
import com.kakaopay.moneyswagger.account.model.AccountRepository;
import com.kakaopay.moneyswagger.account.model.TransferRole;
import com.kakaopay.moneyswagger.member.MemberService;
import com.kakaopay.moneyswagger.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberService memberService;

    public Account createAccount(Member member) {
        Account account = Account.builder()
                .member(member)
                .balance(BigDecimal.ZERO)
                .build();

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Optional<Account> getReceiverAccount(Long accountId) {
        return retrieveById(accountId);
    }

    @Transactional(readOnly = true)
    public Optional<Account> getGiverAccount(Long accountId, Long giverMemberId) {
        Optional<Account> giverAccount = accountRepository.findById(accountId);

        if (giverAccount.isPresent()) {
            giverAccount = filterWhenNotOwner(giverAccount, giverMemberId);
        }

        return giverAccount;
    }

    public Optional<Account> retrieveById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public synchronized Account deposit(Account account, Integer depositAmount) {
        account.deposit(depositAmount);
        return accountRepository.save(account);
    }

    public synchronized Map<TransferRole, Account> transfer(Account giver, Account receiver, Integer transferAmount) {
        giver.withdraw(transferAmount);
        receiver.deposit(transferAmount);
        Account giverAfterTransfer = accountRepository.save(giver);
        Account receiverAfterTransfer = accountRepository.save(receiver);

        return Map.of(TransferRole.GIVER, giverAfterTransfer, TransferRole.RECEIVER, receiverAfterTransfer);
    }

    private Optional<Account> filterWhenNotOwner(Optional<Account> giverAccount, Long giverMemberId) {
        Account account = giverAccount.get();
        if (!account.isOwner(giverMemberId)) {
            return Optional.empty();
        }

        return giverAccount;
    }
}
