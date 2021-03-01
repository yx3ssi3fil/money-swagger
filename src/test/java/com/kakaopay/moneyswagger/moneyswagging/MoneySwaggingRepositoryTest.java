package com.kakaopay.moneyswagger.moneyswagging;

import com.kakaopay.moneyswagger.chatroom.model.ChatRoom;
import com.kakaopay.moneyswagger.chatroom.model.ChatRoomRepository;
import com.kakaopay.moneyswagger.member.model.Member;
import com.kakaopay.moneyswagger.member.model.MemberRepository;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwagging;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwaggingRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MoneySwaggingRepositoryTest {
    @Autowired
    private MoneySwaggingRepository moneySwaggingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void createMoneySwagging() {
        //given
        String token = RandomStringUtils.randomAlphabetic(3);
        Member giver = makeTestGiver();
        List<Member> members = makeTestMembers(giver);
        ChatRoom chatRoom = makeTestChatRoom(members);
        MoneySwagging moneySwagging = makeTestMoneySwagging(token, chatRoom, giver);

        //when
        MoneySwagging savedMoneySwagging = moneySwaggingRepository.save(moneySwagging);

        //then
        assertThat(savedMoneySwagging.getId()).isNotNull();
        assertThat(savedMoneySwagging.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(savedMoneySwagging.getChatRoom().getId()).isEqualTo(chatRoom.getId());
        assertThat(savedMoneySwagging.getToken()).isEqualTo(token);
    }

    @Test
    void findByMemberAndToken() {
        //given
        String token = RandomStringUtils.randomAlphabetic(3);
        Member giver = makeTestGiver();
        List<Member> members = makeTestMembers(giver);
        ChatRoom chatRoom = makeTestChatRoom(members);
        MoneySwagging savedMoneySwagging = moneySwaggingRepository.save(makeTestMoneySwagging(token, chatRoom, giver));

        //when
        Optional<MoneySwagging> moneySwagging = moneySwaggingRepository.findByMemberAndToken(giver, token);

        //then
        assertThat(moneySwagging).isPresent();
        assertThat(moneySwagging.get().getId()).isEqualTo(savedMoneySwagging.getId());
    }

    @Test
    void findByChatRoomAndToken() {
        //given
        String token = RandomStringUtils.randomAlphabetic(3);
        Member giver = makeTestGiver();
        List<Member> members = makeTestMembers(giver);
        ChatRoom chatRoom = makeTestChatRoom(members);
        MoneySwagging savedMoneySwagging = moneySwaggingRepository.save(makeTestMoneySwagging(token, chatRoom, giver));

        //when
        Optional<MoneySwagging> moneySwagging = moneySwaggingRepository.findByChatRoomAndToken(chatRoom, token);

        //then
        assertThat(moneySwagging).isPresent();
        assertThat(moneySwagging.get().getId()).isEqualTo(savedMoneySwagging.getId());
    }

    private MoneySwagging makeTestMoneySwagging(String token, ChatRoom chatRoom, Member giver) {
        return MoneySwagging.builder()
                .chatRoom(chatRoom)
                .member(giver)
                .token(token)
                .build();
    }

    private ChatRoom makeTestChatRoom(List<Member> members) {
        String uuid = RandomStringUtils.randomAlphabetic(10);
        ChatRoom chatRoom = ChatRoom.builder().id(uuid).members(members).build();

        return chatRoomRepository.save(chatRoom);
    }

    private List<Member> makeTestMembers(Member giver) {
        Member member1 = memberRepository.save(new Member("member1"));
        Member member2 = memberRepository.save(new Member("member2"));
        Member member3 = memberRepository.save(new Member("member3"));

        return List.of(member1, member2, member3, giver);
    }

    private Member makeTestGiver() {
        return memberRepository.save(new Member("giver"));
    }
}