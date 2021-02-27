package com.kakaopay.moneyswagger.entity.account;

import com.kakaopay.moneyswagger.entity.base.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class MoneySwagging extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "money_swagging_id")
    private Long id;

    private String token;
}
