package com.kakaopay.moneyswagger.entity.member;

import com.kakaopay.moneyswagger.entity.account.Account;
import com.kakaopay.moneyswagger.entity.base.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Account> accounts = new ArrayList<>();

    @Builder
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
