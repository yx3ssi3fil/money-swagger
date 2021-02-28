package com.kakaopay.moneyswagger.member;

import com.kakaopay.moneyswagger.member.dto.CreateMemberDto;
import com.kakaopay.moneyswagger.member.dto.RetrieveMemberDto;
import com.kakaopay.moneyswagger.member.MemberController;
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

    public RetrieveMemberDto.Response retrieveMember(Long memberId) {
        return webTestClient
                .get().uri(MemberController.URL_CREATE_MEMBER + "/{id}", memberId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RetrieveMemberDto.Response.class)
                .returnResult()
                .getResponseBody();
    }
}
