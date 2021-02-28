package com.kakaopay.moneyswagger.entity.account;

import com.kakaopay.moneyswagger.entity.base.BaseTimeEntity;
import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
public class MoneySwagging extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "money_swagging_id")
    private Long id;

    private String token;

    private Integer amount;

    private Integer completedAmount;

    private Integer peopleCount;

    @OneToMany
    @JoinColumn(name = "moneySwagging")
    private List<MoneyPortion> moneyPortions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member; //given

    @Builder
    public MoneySwagging(Long id, String token, Integer amount, Integer completedAmount, Integer peopleCount,
                         List<MoneyPortion> moneyPortions, ChatRoom chatRoom, Member member) {
        this.id = id;
        this.token = token;
        this.amount = amount;
        this.completedAmount = completedAmount;
        this.peopleCount = peopleCount;
        this.moneyPortions = moneyPortions;
        this.chatRoom = chatRoom;
        this.member = member;
    }

    public void assignToken(String token) {
        this.token = token;
    }

    public void assignMoneyPortions(List<MoneyPortion> moneyPortions) {
        this.moneyPortions = moneyPortions;
    }

    public List<MoneyPortion> getCompletedMoneyPortions() {
        return moneyPortions.stream()
                .filter(MoneyPortion::isReceived)
                .collect(Collectors.toList());
    }
}
