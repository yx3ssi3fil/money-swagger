package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.dto.CreateChatRoomDto;
import com.kakaopay.moneyswagger.dto.CreateMemberDto;
import com.kakaopay.moneyswagger.dto.MoneySwaggingDto;
import com.kakaopay.moneyswagger.entity.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneySwaggingControllerTest extends AbstractControllerTest {
    private AccountHttpTest accountHttpTest;
    private MemberHttpTest memberHttpTest;
    private ChatRoomHttpTest chatRoomHttpTest;


    private static CreateMemberDto.Response member1;
    private static CreateMemberDto.Response member2;
    private static CreateMemberDto.Response member3;
    private static CreateMemberDto.Response member4;
    private static CreateMemberDto.Response giver;
    private static List<Long> memberIds = new ArrayList<>();

    private static CreateAccountDto.Response account1;
    private static CreateAccountDto.Response account2;
    private static CreateAccountDto.Response account3;
    private static CreateAccountDto.Response account4;
    private static CreateAccountDto.Response giverAccount;
    private static CreateChatRoomDto.Response chatRoom;

    @BeforeEach
    void setUp() {
        accountHttpTest = new AccountHttpTest(webTestClient);
        memberHttpTest = new MemberHttpTest(webTestClient);
        chatRoomHttpTest = new ChatRoomHttpTest(webTestClient);

        member1 = memberHttpTest.createMember("member1");
        member2 = memberHttpTest.createMember("member2");
        member3 = memberHttpTest.createMember("member3");
        member4 = memberHttpTest.createMember("member4");
        giver = memberHttpTest.createMember("giver");
        account1 = accountHttpTest.createAccount(member1.getId());
        account2 = accountHttpTest.createAccount(member2.getId());
        account3 = accountHttpTest.createAccount(member3.getId());
        account4 = accountHttpTest.createAccount(member4.getId());
        giverAccount = accountHttpTest.createAccount(giver.getId());
        accountHttpTest.deposit(account1.getAccountId(), member1.getId(), 1_000_000_000);
        accountHttpTest.deposit(account2.getAccountId(), member2.getId(), 1_000_000_000);
        accountHttpTest.deposit(account3.getAccountId(), member3.getId(), 1_000_000_000);
        accountHttpTest.deposit(account4.getAccountId(), member4.getId(), 1_000_000_000);
        accountHttpTest.deposit(giverAccount.getAccountId(), giver.getId(), 1_000_000_000);

        memberIds = List.of(giver.getId(), member1.getId(), member2.getId(), member3.getId(), member4.getId());
        chatRoom = chatRoomHttpTest.createChatRoom(memberIds);
    }

    @Test
    void createMoneySwagging() {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = giver.getId();

        MoneySwaggingDto.Request requestBody = MoneySwaggingDto.Request.builder()
                .amount(amount)
                .peopleCount(peopleCount)
                .build();

        //when
        MoneySwaggingDto.Response responseBody = webTestClient
                .post().uri(MoneySwaggingController.URL_CREATE_MONEY_SWAGGING)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MoneySwaggingDto.Response.class)
                .returnResult()
                .getResponseBody();

        //then
        assert responseBody != null;
        assertThat(responseBody.getToken().length()).isEqualTo(3);
        assertThat(responseBody.getCreatedTime()).isBefore(LocalDateTime.now());
        assertThat(responseBody.getDividedAmounts()).hasSize(peopleCount);
        int sum = responseBody.getDividedAmounts().stream()
                .mapToInt(Integer::intValue)
                .sum();
        assertThat(sum).isEqualTo(amount);
    }
}
