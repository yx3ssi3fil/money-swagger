package com.kakaopay.moneyswagger.entity.chat;

import com.kakaopay.moneyswagger.entity.base.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseTimeEntity {
    @Id
    @Column(name = "chat_room_id")
    private String id;
}
