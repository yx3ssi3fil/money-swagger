package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateAccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountControllerTest extends AbstractControllerTest {
    private AccountHttpTest accountHttpTest;
    private MemberHttpTest memberHttpTest;
    private Long memberId;

    @BeforeEach
    void setUp() {
        accountHttpTest = new AccountHttpTest(webTestClient);
        memberHttpTest = new MemberHttpTest(webTestClient);
        memberId = memberHttpTest.createMember("name").getId();
    }

    @DisplayName("계좌 생성")
    @Test
    void createAccount() {
        //when
        CreateAccountDto.Response responseBody = accountHttpTest.createAccount(memberId);

        //then
        assertThat(responseBody.getAccountId()).isNotNull().isNotNegative();
        assertThat(responseBody.getMemberId()).isEqualTo(memberId);
        assertThat(responseBody.getMemberName()).isEqualTo("name");
        assertThat(responseBody.getBalance()).isEqualTo(0);
    }
}
