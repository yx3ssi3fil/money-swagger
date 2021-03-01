package com.kakaopay.moneyswagger.moneyswagging.model;

import com.kakaopay.moneyswagger.chatroom.model.ChatRoom;
import com.kakaopay.moneyswagger.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoneySwaggingRepository extends JpaRepository<MoneySwagging, Long> {
    Optional<MoneySwagging> findByMemberAndToken(Member member, String token);
    Optional<MoneySwagging> findByChatRoomAndToken(ChatRoom chatRoom, String token);
}
