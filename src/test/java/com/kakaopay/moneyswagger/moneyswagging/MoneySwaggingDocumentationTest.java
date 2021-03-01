package com.kakaopay.moneyswagger.moneyswagging;

import com.kakaopay.moneyswagger.AbstractDocumentationTest;
import com.kakaopay.moneyswagger.account.AccountHttpTest;
import com.kakaopay.moneyswagger.account.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.chatroom.ChatRoomHttpTest;
import com.kakaopay.moneyswagger.chatroom.dto.CreateChatRoomDto;
import com.kakaopay.moneyswagger.member.MemberHttpTest;
import com.kakaopay.moneyswagger.member.dto.CreateMemberDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.MoneyAcceptanceDto;
import com.kakaopay.moneyswagger.moneyswagging.model.Header;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoneySwaggingDocumentationTest extends AbstractDocumentationTest {
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

    @DisplayName("[API 문서화 - 1] 머니뿌리기 API")
    @Test
    @Order(1)
    void createMoneySwagging() throws Exception {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = giver.getId();
        CreateMoneySwaggingDto.Request requestBody = CreateMoneySwaggingDto.Request.builder()
                .amount(amount)
                .peopleCount(peopleCount)
                .build();

        //when, then
        mockMvc.perform(post(MoneySwaggingController.URL_CREATE_MONEY_SWAGGING)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("money-swagging/create",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("멤버 Id"),
                                headerWithName("X-ROOM-ID").description("채팅방 id")
                        ),
                        requestFields(
                                fieldWithPath("amount").description("뿌릴 금액"),
                                fieldWithPath("peopleCount").description("뿌릴 인원")
                        ),
                        responseFields(
                                fieldWithPath("moneySwaggingId").type(JsonFieldType.NUMBER).description("머니뿌리기 id"),
                                fieldWithPath("token").type(JsonFieldType.STRING).description("토큰(세 자리)"),
                                fieldWithPath("createdTime").type(JsonFieldType.STRING).description("뿌리기 요청 시각")
                        )
                ));
    }

    @DisplayName("[API 문서화 - 2] 받기 API")
    @Test
    @Order(2)
    void acceptByMember() throws Exception {
        //given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = member1.getId();
        String token = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId()).getToken();
        MoneyAcceptanceDto.Request requestBody = new MoneyAcceptanceDto.Request(token, chatRoomId);

        //when
        mockMvc.perform(put(MoneySwaggingController.URL_ACCEPT_MONEY)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("money-swagging/accept-money",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("멤버 Id"),
                                headerWithName("X-ROOM-ID").description("채팅방 id")
                        ),
                        requestFields(
                                fieldWithPath("token").description("뿌리기 토큰"),
                                fieldWithPath("chatRoomId").description("채팅방 id")
                        ),
                        responseFields(
                                fieldWithPath("receiveAmount").type(JsonFieldType.NUMBER).description("받기 금액(단위: 원(won))")
                        )
                ));
    }

    @DisplayName("[API 문서화 - 3] 조회 API")
    @Test
    @Order(3)
    void retrieveMoneySwagging() throws Exception {
        // given
        Integer amount = 1_000_000;
        Integer peopleCount = 3;
        String chatRoomId = chatRoom.getChatRoomId();
        Long userId = giver.getId();
        CreateMoneySwaggingDto.Response moneySwagging = moneySwaggingHttpTest.create(amount, peopleCount, chatRoomId, giver.getId());
        String token = moneySwagging.getToken();

        MoneyAcceptanceDto.Request requestBodyToAccept = new MoneyAcceptanceDto.Request(token, chatRoomId);
        moneySwaggingHttpTest.acceptMoney(chatRoomId, member1.getId(), requestBodyToAccept, HttpStatus.OK);
        moneySwaggingHttpTest.acceptMoney(chatRoomId, member2.getId(), requestBodyToAccept, HttpStatus.OK);
        moneySwaggingHttpTest.acceptMoney(chatRoomId, member3.getId(), requestBodyToAccept, HttpStatus.OK);

        //when, then
        mockMvc.perform(get(MoneySwaggingController.URL_RETRIEVE_MONEY_SWAGGING)
                .param("token", token)
                .header(Header.CHAT_ROOM_ID.getKey(), chatRoomId)
                .header(Header.USER_ID.getKey(), String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("money-swagging/retrieve",
                        requestHeaders(
                                headerWithName("X-USER-ID").description("멤버 Id"),
                                headerWithName("X-ROOM-ID").description("채팅방 id")
                        ),
                        requestParameters(
                                parameterWithName("token").description("뿌리기 토큰")
                        ),
                        responseFields(
                                fieldWithPath("moneySwaggingTime").type(JsonFieldType.STRING).description("뿌리기 요청한 시각"),
                                fieldWithPath("moneySwaggingAmount").type(JsonFieldType.NUMBER).description("뿌리기 금액"),
                                fieldWithPath("completedAmount").type(JsonFieldType.NUMBER).description("받기 완료된 금액"),
                                fieldWithPath("completedInfos").type(JsonFieldType.ARRAY).description("받기 완료된 뿌리기 금액"),
                                fieldWithPath("completedInfos[].receiverName").type(JsonFieldType.STRING).description("받은 사람 이름"),
                                fieldWithPath("completedInfos[].receivedAmount").type(JsonFieldType.NUMBER).description("받은 금액")
                        )
                ));
    }

    private void makeTestData() {
        makeMembers();
        makeAccounts();
        depositBigMoneyToTest();
        makeChatRoom();
    }

    private void makeMembers() {
        member1 = memberHttpTest.createMember("Ryon");
        member2 = memberHttpTest.createMember("Con");
        member3 = memberHttpTest.createMember("Apeach");
        member4 = memberHttpTest.createMember("Jay-Z");
        giver = memberHttpTest.createMember("Muzi");
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
