package com.kakaopay.moneyswagger.repository;

import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MoneySwaggingRepositoryTest {
    @Autowired
    private MoneySwaggingRepository moneySwaggingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createMoneySwagging() {
        //given
        Member member1 = memberRepository.save(new Member("member1"));
        Member member2 = memberRepository.save(new Member("member2"));
        Member member3 = memberRepository.save(new Member("member3"));
        Member giver = memberRepository.save(new Member("giver"));
        List<Member> members = List.of(member1, member2, member3, giver);
        String uuid = RandomStringUtils.randomAlphabetic(10);
        ChatRoom chatRoom = ChatRoom.builder().id(uuid).members(members).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        String token = RandomStringUtils.randomAlphabetic(3);

        //when
        MoneySwagging moneySwagging = MoneySwagging.builder()
                .chatRoom(savedChatRoom)
                .member(giver)
                .token(token)
                .build();
        MoneySwagging savedMoneySwagging = moneySwaggingRepository.save(moneySwagging);

        //then
        assertThat(savedMoneySwagging.getId()).isNotNull();
        assertThat(savedMoneySwagging.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(savedMoneySwagging.getChatRoom().getId()).isEqualTo(savedChatRoom.getId());
        assertThat(savedMoneySwagging.getToken()).isEqualTo(token);
    }
}