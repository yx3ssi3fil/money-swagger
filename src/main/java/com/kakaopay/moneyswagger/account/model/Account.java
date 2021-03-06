package com.kakaopay.moneyswagger.account.model;

import com.kakaopay.moneyswagger.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id; //게좌번호 역할 포함

    private BigDecimal balance; //잔액

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Account(Long id, BigDecimal balance, Member member) {
        this.id = id;
        this.balance = balance;
        this.member = member;
    }

    public void deposit(Integer depositAmount) {
        this.balance = this.balance.add(BigDecimal.valueOf(depositAmount));
    }

    public void withdraw(Integer withdrawAmount) {
        this.balance = this.balance.subtract(BigDecimal.valueOf(withdrawAmount));
    }

    public Boolean isOwner(Long memberId) {
        return this.member.getId().equals(memberId);
    }

    public Boolean isOwnerName(String name) {
        return this.member.getName().equals(name);
    }
}
