package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateChatRoomDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

public class ChatRoomHttpTest {
    private WebTestClient webTestClient;

    public ChatRoomHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public CreateChatRoomDto.Response createChatRoom(List<Long> memberIds) {
        CreateChatRoomDto.Request requestBody = CreateChatRoomDto.Request.builder()
                .members(memberIds)
                .build();

        return webTestClient.post().uri(ChatRoomController.URL_CREATE_CHAT_ROOM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateChatRoomDto.Response.class)
                .returnResult()
                .getResponseBody();
    }
}
