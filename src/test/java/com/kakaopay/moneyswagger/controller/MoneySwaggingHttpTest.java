package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.dto.MoneyAcceptanceDto;
import com.kakaopay.moneyswagger.dto.RetrieveMoneySwaggingDto;
import com.kakaopay.moneyswagger.entity.Header;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class MoneySwaggingHttpTest {
    private WebTestClient webTestClient;

    public MoneySwaggingHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public CreateMoneySwaggingDto.Response create(Integer amount, Integer peopleCount, String chatRoomId, Long userId) {
        CreateMoneySwaggingDto.Request requestBody = CreateMoneySwaggingDto.Request.builder()
                .amount(amount)
                .peopleCount(peopleCount)
                .build();

        return webTestClient
                .post().uri(MoneySwaggingController.URL_CREATE_MONEY_SWAGGING)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateMoneySwaggingDto.Response.class)
                .returnResult()
                .getResponseBody();
    }

    public RetrieveMoneySwaggingDto.Response retrieveByGiver(String chatRoomId, String token, Long userId, Long moneySwaggingId) {
        return webTestClient.get().uri(MoneySwaggingController.URL_RETRIEVE_MONEY_SWAGGING + "?token=" + token, moneySwaggingId)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RetrieveMoneySwaggingDto.Response.class)
                .returnResult()
                .getResponseBody();
    }

    public <T> MoneyAcceptanceDto.Response acceptMoney(String chatRoomId, Long userId, MoneyAcceptanceDto.Request requestBody, HttpStatus T) {
        return webTestClient.post().uri(MoneySwaggingController.URL_ACCEPT_MONEY)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isEqualTo(T)
                .expectBody(MoneyAcceptanceDto.Response.class)
                .returnResult()
                .getResponseBody();
    }
}
