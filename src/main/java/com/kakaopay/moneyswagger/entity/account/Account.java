package com.kakaopay.moneyswagger.entity.account;

import com.kakaopay.moneyswagger.entity.member.Member;
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

    private BigDecimal balance = BigDecimal.ZERO; //잔액

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Account(Long id, BigDecimal balance, Member member) {
        this.id = id;
        this.balance = balance;
        this.member = member;
    }
}
