package com.kakaopay.moneyswagger.chatroom;

import com.kakaopay.moneyswagger.chatroom.model.ChatRoom;
import com.kakaopay.moneyswagger.chatroom.model.ChatRoomRepository;
import com.kakaopay.moneyswagger.member.model.Member;
import com.kakaopay.moneyswagger.member.model.MemberRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatRoomRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void createChatRoom() {
        //given
        Member member1 = memberRepository.save(new Member("member1"));
        Member member2 = memberRepository.save(new Member("member2"));
        Member member3 = memberRepository.save(new Member("member3"));
        Member member4 = memberRepository.save(new Member("member4"));
        List<Member> members = List.of(member1, member2, member3, member4);
        String uuid = RandomStringUtils.randomAlphabetic(10);
        ChatRoom chatRoom = ChatRoom.builder().id(uuid).members(members).build();

        //when
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //then
        assertThat(savedChatRoom.getMembers()).hasSize(4);
        assertThat(savedChatRoom.getId()).isEqualTo(uuid);
    }
}