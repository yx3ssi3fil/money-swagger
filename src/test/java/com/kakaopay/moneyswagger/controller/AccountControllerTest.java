package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateAccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountControllerTest extends AbstractControllerTest {
    private MemberHttpTest memberHttpTest;
    private Long memberId;

    @BeforeEach
    void setUp() {
        memberHttpTest = new MemberHttpTest(webTestClient);
        memberId = memberHttpTest.createMember("name").getId();
    }

    @DisplayName("계좌 생성")
    @Test
    void createAccount() {
        //given
        CreateAccountDto.Request requestBody = new CreateAccountDto.Request(memberId);

        //when
        CreateAccountDto.Response responseBody = webTestClient.post().uri(AccountController.URL_CREATE_ACCOUNTS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateAccountDto.Response.class)
                .returnResult()
                .getResponseBody();

        //then
        assertThat(responseBody.getAccountId()).isNotNull().isNotNegative();
        assertThat(responseBody.getMemberId()).isEqualTo(memberId);
        assertThat(responseBody.getMemberName()).isEqualTo("name");
        assertThat(responseBody.getBalance()).isEqualTo(0);
    }
}
