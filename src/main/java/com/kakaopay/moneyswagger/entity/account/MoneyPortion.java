package com.kakaopay.moneyswagger.entity.account;

import com.kakaopay.moneyswagger.entity.base.BaseTimeEntity;
import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class MoneyPortion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "money_portion_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "money_swagging_id")
    private MoneySwagging moneySwagging;

    @OneToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member receiver;

    private Integer amount;

    @Builder
    public MoneyPortion(Long id, MoneySwagging moneySwagging, ChatRoom chatRoom, Member receiver, Integer amount) {
        this.id = id;
        this.moneySwagging = moneySwagging;
        this.chatRoom = chatRoom;
        this.receiver = receiver;
        this.amount = amount;
    }

    public MoneyPortion(MoneySwagging moneySwagging, Integer amount) {
        this.moneySwagging = moneySwagging;
        this.chatRoom = moneySwagging.getChatRoom();
        this.amount = amount;
    }

    public Boolean isReceived() {
        return receiver != null;
    }

    public void assignReceiver(Member receiver) {
        this.receiver = receiver;
    }
}
