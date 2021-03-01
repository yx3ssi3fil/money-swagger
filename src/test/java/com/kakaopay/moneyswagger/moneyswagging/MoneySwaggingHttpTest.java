package com.kakaopay.moneyswagger.moneyswagging;

import com.kakaopay.moneyswagger.moneyswagging.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.MoneyAcceptanceDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.RetrieveMoneySwaggingDto;
import com.kakaopay.moneyswagger.moneyswagging.model.Header;
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

    public <T> RetrieveMoneySwaggingDto.Response retrieveByGiver(String chatRoomId, String token, Long userId, Long moneySwaggingId, HttpStatus T) {
        return webTestClient.get().uri(MoneySwaggingController.URL_RETRIEVE_MONEY_SWAGGING + "?token=" + token)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(T)
                .expectBody(RetrieveMoneySwaggingDto.Response.class)
                .returnResult()
                .getResponseBody();
    }

    public <T> MoneyAcceptanceDto.Response acceptMoney(String chatRoomId, Long userId, MoneyAcceptanceDto.Request requestBody, HttpStatus T) {
        return webTestClient.put().uri(MoneySwaggingController.URL_ACCEPT_MONEY)
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
