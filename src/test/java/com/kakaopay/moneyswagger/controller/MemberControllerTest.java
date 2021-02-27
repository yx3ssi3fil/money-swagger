package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateMemberDto;
import com.kakaopay.moneyswagger.dto.RetrieveMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberControllerTest extends AbstractControllerTest {
    private MemberHttpTest memberHttpTest;

    @BeforeEach
    public void setUp() {
        this.memberHttpTest = new MemberHttpTest(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    void signUp() {
        //given
        String name = "name";

        //when
        CreateMemberDto.Response responseBody = memberHttpTest.createMember(name);

        //then
        assertThat(responseBody.getId()).isNotNull().isNotNegative();
        assertThat(responseBody.getName()).isEqualTo("name");
    }

    @DisplayName("회원조회 (by id)")
    @Test
    void retrieveById() {
        //given
        Long memberId = memberHttpTest.createMember("name").getId();

        //when
        RetrieveMemberDto.Response responseBody = webTestClient
                .get().uri(MemberController.URL_MEMBERS + "/" + memberId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RetrieveMemberDto.Response.class)
                .returnResult()
                .getResponseBody();

        //then
        assertThat(responseBody.getId()).isNotNull().isNotNegative();
        assertThat(responseBody.getName()).isEqualTo("name");
    }
}
