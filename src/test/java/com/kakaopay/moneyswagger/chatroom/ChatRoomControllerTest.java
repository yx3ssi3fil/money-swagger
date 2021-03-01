package com.kakaopay.moneyswagger.chatroom;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.account.AccountHttpTest;
import com.kakaopay.moneyswagger.chatroom.dto.CreateChatRoomDto;
import com.kakaopay.moneyswagger.member.MemberHttpTest;
import com.kakaopay.moneyswagger.member.dto.CreateMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatRoomControllerTest extends AbstractControllerTest {
    private AccountHttpTest accountHttpTest;
    private MemberHttpTest memberHttpTest;
    private ChatRoomHttpTest chatRoomHttpTest;

    @BeforeEach
    void setUp() {
        accountHttpTest = new AccountHttpTest(webTestClient);
        memberHttpTest = new MemberHttpTest(webTestClient);
        chatRoomHttpTest = new ChatRoomHttpTest(webTestClient);
    }

    @DisplayName("채팅방 생성 (채팅방 생성될 때 모든 인원이 같이 들어오는 것으로 가정)")
    @Test
    void createChatRoom() {
        //given
        CreateMemberDto.Response member1 = memberHttpTest.createMember("member1");
        CreateMemberDto.Response member2 = memberHttpTest.createMember("member2");
        CreateMemberDto.Response member3 = memberHttpTest.createMember("member3");
        CreateMemberDto.Response member4 = memberHttpTest.createMember("member4");
        List<Long> memberIds = List.of(member1.getId(), member2.getId(), member3.getId(), member4.getId());

        //when
        CreateChatRoomDto.Response responseBody = chatRoomHttpTest.createChatRoom(memberIds);

        //then
        assertThat(responseBody.getMemberInfos()).hasSize(4);
        responseBody.getMemberInfos().stream()
                .forEach(memberInfo -> {
                    assertThat(memberInfo.getChatRoomId()).isEqualTo(responseBody.getChatRoomId());
                });
    }
}
