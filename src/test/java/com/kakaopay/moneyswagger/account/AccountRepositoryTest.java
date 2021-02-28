package com.kakaopay.moneyswagger.account;

import com.kakaopay.moneyswagger.account.model.Account;
import com.kakaopay.moneyswagger.account.model.AccountRepository;
import com.kakaopay.moneyswagger.member.model.Member;
import com.kakaopay.moneyswagger.member.model.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createAccount() {
        //given
        Member member = Member.builder()
                .name("name")
                .build();
        Member savedMember = memberRepository.save(member);

        //when
        Account account = Account.builder().member(savedMember).build();
        Account savedAccount = accountRepository.save(account);

        //then
        Member memberFromAccount = savedAccount.getMember();
        assertThat(memberFromAccount.getId()).isEqualTo(savedMember.getId());
        assertThat(memberFromAccount.getName()).isEqualTo(savedMember.getName());
    }
}