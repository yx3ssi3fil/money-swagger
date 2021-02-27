package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateMemberDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class MemberHttpTest {
    private WebTestClient webTestClient;

    public MemberHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public CreateMemberDto.Response createMember(String name) {
        CreateMemberDto.Request requestBody = new CreateMemberDto.Request("name");

        return webTestClient
                .post().uri(MemberController.URL_CREATE_MEMBER)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateMemberDto.Response.class)
                .returnResult()
                .getResponseBody();
    }
}
