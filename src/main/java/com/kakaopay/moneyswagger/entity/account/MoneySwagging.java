package com.kakaopay.moneyswagger.entity.account;

import com.kakaopay.moneyswagger.entity.base.BaseTimeEntity;
import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class MoneySwagging extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "money_swagging_id")
    private Long id;

    private String token;

    @OneToOne(mappedBy = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne(mappedBy = "member_id")
    private Member member;

    @Builder
    public MoneySwagging(Long id, String token, ChatRoom chatRoom, Member member) {
        this.id = id;
        this.token = token;
        this.chatRoom = chatRoom;
        this.member = member;
    }
}
