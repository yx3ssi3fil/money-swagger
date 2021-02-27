package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateMemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberControllerTest extends AbstractControllerTest {
    @DisplayName("회원가입")
    @Test
    void signUp() {
        //given
        CreateMemberDto.Request requestBody = new CreateMemberDto.Request("name");

        //when
        CreateMemberDto.Response responseBody = webTestClient
                .post().uri(MemberController.URL_MEMBERS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateMemberDto.Response.class)
                .returnResult()
                .getResponseBody();

        //then
        assertThat(responseBody.getId()).isNotNull().isNotNegative();
        assertThat(responseBody.getName()).isEqualTo("name");
    }
}
