package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.*;
import com.kakaopay.moneyswagger.entity.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private MoneySwaggingHttpTest moneySwaggingHttpTest;

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
        moneySwaggingHttpTest = new MoneySwaggingHttpTest(webTestClient);
        makeTestData();
    }

    @DisplayName("[과제 1번] 뿌리기 API")
    @Test
    void createMoneySwagging() {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = giver.getId();

        //when
        CreateMoneySwaggingDto.Response responseBody = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, userId);

        //then
        assert responseBody != null;
        assertThat(responseBody.getMoneySwaggingId()).isNotNull();
        assertThat(responseBody.getToken().length()).isEqualTo(3);
        assertThat(responseBody.getCreatedTime()).isBefore(LocalDateTime.now());
    }

    @DisplayName("[과제 2번] 받기 API - 받기 성공(채팅방 참여자 중 뿌리지 않은 사람)")
    @Test
    void acceptByMember() {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = member1.getId();
        String token = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId()).getToken();
        MoneyAcceptanceDto.Request requestBody = new MoneyAcceptanceDto.Request(token);

        //when
        MoneyAcceptanceDto.Response responseBody = moneySwaggingHttpTest.acceptMoney(chatRoomId, userId, requestBody);

        //then
        assertThat(responseBody.getReceiveAmount()).isGreaterThan(0);
    }

    @DisplayName("[과제 2번] 받기 API - 받기 실패(이미 받기 인원이 다 찼음))")
    @Test
    void acceptByMemberWhenItIsFull() {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        String token = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId()).getToken();
        MoneyAcceptanceDto.Request requestBody = new MoneyAcceptanceDto.Request(token);
        moneySwaggingHttpTest.acceptMoney(chatRoomId, member1.getId(), requestBody);
        moneySwaggingHttpTest.acceptMoney(chatRoomId, member2.getId(), requestBody);
        moneySwaggingHttpTest.acceptMoney(chatRoomId, member3.getId(), requestBody);

        //when
        webTestClient.post().uri(MoneySwaggingController.URL_ACCEPT_MONEY)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(member4.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("[과제 2번] 받기 API - 받기 실패(채팅방 참여자 중 뿌린 사람)")
    @Test
    void acceptByGiver() {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = giver.getId();
        String token = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId()).getToken();
        MoneyAcceptanceDto.Request requestBody = new MoneyAcceptanceDto.Request(token);

        //when, then
        webTestClient.post().uri(MoneySwaggingController.URL_ACCEPT_MONEY)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("[과제 2번] 받기 API - 받기 실패(채팅방 참여자가 아닌 사람)")
    @Test
    void acceptByNotMemberOfChatRoom() {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        CreateMemberDto.Response other = memberHttpTest.createMember("other");
        Long userId = other.getId();
        String token = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId()).getToken();
        MoneyAcceptanceDto.Request requestBody = new MoneyAcceptanceDto.Request(token);

        //when, then
        webTestClient.post().uri(MoneySwaggingController.URL_ACCEPT_MONEY)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("[과제 3번] 조회 API - 뿌린 사람이 조회")
    @Test
    void retrieveMoneySwagging() {
        // given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = giver.getId();
        String token = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId()).getToken();

        //when
        RetrieveMoneySwaggingDto.Response responseBody = moneySwaggingHttpTest.retrieveByGiver(chatRoomId, token, userId);

        //then
        assertThat(responseBody.getMoneySwaggingAmount()).isEqualTo(amount);
        assertThat(responseBody.getCompletedAmount()).isEqualTo(0);
        assertThat(responseBody.getCompletedInfos()).isNullOrEmpty();
        assertThat(responseBody.getMoneySwaggingTime()).isBefore(LocalDateTime.now());
    }

    @DisplayName("[과제 3번] 조회 API - 뿌리지 않은 사람이 조회")
    @Test
    void retrieveMoneySwaggingByReceiver() {
        // given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = member1.getId();
        String token = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId()).getToken();

        //when, then
        webTestClient.get().uri(MoneySwaggingController.URL_RETRIEVE_MONEY_SWAGGING + "?token=" + token)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private void makeTestData() {
        makeMembers();
        makeAccounts();
        depositBigMoneyToTest();
        makeChatRoom();
    }

    private void makeMembers() {
        member1 = memberHttpTest.createMember("member1");
        member2 = memberHttpTest.createMember("member2");
        member3 = memberHttpTest.createMember("member3");
        member4 = memberHttpTest.createMember("member4");
        giver = memberHttpTest.createMember("giver");
    }

    private void makeAccounts() {
        account1 = accountHttpTest.createAccount(member1.getId());
        account2 = accountHttpTest.createAccount(member2.getId());
        account3 = accountHttpTest.createAccount(member3.getId());
        account4 = accountHttpTest.createAccount(member4.getId());
        giverAccount = accountHttpTest.createAccount(giver.getId());
    }

    private void depositBigMoneyToTest() {
        accountHttpTest.deposit(account1.getAccountId(), member1.getId(), 1_000_000_000);
        accountHttpTest.deposit(account2.getAccountId(), member2.getId(), 1_000_000_000);
        accountHttpTest.deposit(account3.getAccountId(), member3.getId(), 1_000_000_000);
        accountHttpTest.deposit(account4.getAccountId(), member4.getId(), 1_000_000_000);
        accountHttpTest.deposit(giverAccount.getAccountId(), giver.getId(), 1_000_000_000);
    }

    private void makeChatRoom() {
        memberIds = List.of(giver.getId(), member1.getId(), member2.getId(), member3.getId(), member4.getId());
        chatRoom = chatRoomHttpTest.createChatRoom(memberIds);
    }
}
