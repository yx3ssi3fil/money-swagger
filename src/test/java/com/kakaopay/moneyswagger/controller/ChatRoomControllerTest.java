package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateChatRoomDto;
import com.kakaopay.moneyswagger.dto.CreateMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatRoomControllerTest extends AbstractControllerTest {
    private AccountHttpTest accountHttpTest;
    private MemberHttpTest memberHttpTest;

    @BeforeEach
    void setUp() {
        accountHttpTest = new AccountHttpTest(webTestClient);
        memberHttpTest = new MemberHttpTest(webTestClient);
    }

    @DisplayName("채팅방 생성 (핵심로직에 집중하기 위해, 채팅방 생성될 때 모든 인원이 같이 들어오는 것으로 가정)")
    @Test
    void createChatRoom() {
        //given
        CreateMemberDto.Response member1 = memberHttpTest.createMember("member1");
        CreateMemberDto.Response member2 = memberHttpTest.createMember("member2");
        CreateMemberDto.Response member3 = memberHttpTest.createMember("member3");
        CreateMemberDto.Response member4 = memberHttpTest.createMember("member4");
        List<Long> memberIds = List.of(member1.getId(), member2.getId(), member3.getId(), member4.getId());
        CreateChatRoomDto.Request requestBody = CreateChatRoomDto.Request.builder().members(memberIds).build();

        //when
        CreateChatRoomDto.Response responseBody = webTestClient.post().uri(ChatRoomController.URL_CREATE_CHAT_ROOM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateChatRoomDto.Response.class)
                .returnResult()
                .getResponseBody();

        //then
        assertThat(responseBody.getMemberInfos()).hasSize(4);
        responseBody.getMemberInfos().stream()
                .forEach(memberInfo -> {
                    assertThat(memberInfo.getChatRoomId()).isEqualTo(responseBody.getChatRoomId());
                });
    }
}
