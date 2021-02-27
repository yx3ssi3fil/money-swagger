package com.kakaopay.moneyswagger.repository;

import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
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
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        Member member3 = new Member("member3");
        Member member4 = new Member("member4");
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