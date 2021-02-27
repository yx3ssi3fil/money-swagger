package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.dto.RetrieveAccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

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

    @DisplayName("계좌 조회(by Id)")
    @Test
    void retrieveAccount() {
        //given
        Long accountId = accountHttpTest.createAccount(memberId).getAccountId();

        //when
        RetrieveAccountDto.Response responseBody = webTestClient.get().uri(AccountController.URL_RETRIEVE_ACCOUNT, accountId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RetrieveAccountDto.Response.class)
                .returnResult()
                .getResponseBody();

        //then
        assertThat(responseBody.getAccountId()).isEqualTo(accountId);
        assertThat(responseBody.getBalance()).isEqualTo(0);
        assertThat(responseBody.getMemberId()).isEqualTo(memberId);
        assertThat(responseBody.getMemberName()).isEqualTo("name");
    }

    @DisplayName("계좌 조회(by Id) - Invalid accountId")
    @Test
    void retrieveByIdWhenInvalidAccountId() {
        Long accountId = 12345555L;

        //when
        webTestClient.get().uri(AccountController.URL_RETRIEVE_ACCOUNT, accountId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
