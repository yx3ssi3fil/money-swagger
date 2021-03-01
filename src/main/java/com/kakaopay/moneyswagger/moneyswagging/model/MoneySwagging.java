package com.kakaopay.moneyswagger.moneyswagging.model;

import com.kakaopay.moneyswagger.account.model.Account;
import com.kakaopay.moneyswagger.chatroom.model.ChatRoom;
import com.kakaopay.moneyswagger.member.model.Member;
import com.kakaopay.moneyswagger.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
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

    public Boolean isGiver(Long memberId) {
        return this.member.getId().equals(memberId);
    }

    public Boolean isChatRoomMember(Long memberId) {
        return this.chatRoom.getMembers()
                .stream()
                .anyMatch(member -> memberId == member.getId());
    }

    public Boolean isExpiredToAcceptMoney() {
        LocalDateTime nowInKst = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime createdTimeInKst = this.getCreatedDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime tenMinutesAgo = nowInKst.minusMinutes(10);

        return createdTimeInKst.isBefore(tenMinutesAgo);
    }

    public Boolean isExpiredToRetrieve() {
        LocalDateTime nowInKst = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime createdTimeInKst = this.getCreatedDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime sevenDaysAgo = nowInKst.minusDays(7);

        return createdTimeInKst.isBefore(sevenDaysAgo);
    }

    public List<MoneyPortion> getAvailableMoneyPortions(Long userId) {
        if (isAgainAcceptance(this.moneyPortions, userId)) {
            return Collections.emptyList();
        }

        return this.moneyPortions.stream()
                .filter(moneyPortion -> !moneyPortion.isReceived())
                .collect(Collectors.toList());
    }

    public Account getGiverAccount() {
        return this.member.getMajorAccount();
    }

    private Boolean isAgainAcceptance(List<MoneyPortion> moneyPortions, Long userId) {
        return moneyPortions.stream()
                .filter(MoneyPortion::isReceived)
                .anyMatch(moneyPortion -> moneyPortion.getReceiver().getId().equals(userId));
    }
}
