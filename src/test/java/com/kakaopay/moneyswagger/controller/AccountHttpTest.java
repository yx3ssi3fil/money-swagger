package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateAccountDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class AccountHttpTest {
    private WebTestClient webTestClient;

    public AccountHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public CreateAccountDto.Response createAccount(Long memberId) {
        CreateAccountDto.Request requestBody = new CreateAccountDto.Request(memberId);

        return webTestClient.post().uri(AccountController.URL_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateAccountDto.Response.class)
                .returnResult()
                .getResponseBody();
    }
}
