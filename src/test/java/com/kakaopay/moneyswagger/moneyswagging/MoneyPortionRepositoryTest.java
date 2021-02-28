package com.kakaopay.moneyswagger.moneyswagging;

import com.kakaopay.moneyswagger.chatroom.model.ChatRoomRepository;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneyPortion;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwagging;
import com.kakaopay.moneyswagger.chatroom.model.ChatRoom;
import com.kakaopay.moneyswagger.member.model.Member;
import com.kakaopay.moneyswagger.member.model.MemberRepository;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneyPortionRepository;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwaggingRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class MoneyPortionRepositoryTest {
    @Autowired
    private MoneySwaggingRepository moneySwaggingRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MoneyPortionRepository moneyPortionRepository;

    @Test
    void createMoneyPortion() {
        //given
        Member member1 = memberRepository.save(new Member("member1"));
        Member member2 = memberRepository.save(new Member("member2"));
        Member receiver = memberRepository.save(new Member("receiver"));
        Member giver = memberRepository.save(new Member("giver"));
        List<Member> members = List.of(member1, member2, receiver, giver);
        String uuid = RandomStringUtils.randomAlphabetic(10);
        ChatRoom chatRoom = ChatRoom.builder().id(uuid).members(members).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        String token = RandomStringUtils.randomAlphabetic(3);
        MoneySwagging moneySwagging = MoneySwagging.builder()
                .chatRoom(savedChatRoom)
                .member(giver)
                .token(token)
                .build();
        MoneySwagging savedMoneySwagging = moneySwaggingRepository.save(moneySwagging);

        //when
        MoneyPortion moneyPortion = MoneyPortion.builder()
                .chatRoom(savedChatRoom)
                .moneySwagging(savedMoneySwagging)
                .receiver(receiver)
                .build();
        MoneyPortion savedMoneyPortion = moneyPortionRepository.save(moneyPortion);

        //then
        assertThat(savedMoneyPortion.getId()).isNotNull();
        assertThat(savedMoneyPortion.getChatRoom()).isEqualTo(savedChatRoom);
        assertThat(savedMoneyPortion.getMoneySwagging()).isEqualTo(savedMoneySwagging);
        assertThat(savedMoneyPortion.getReceiver()).isEqualTo(receiver);
    }
}
