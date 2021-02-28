package com.kakaopay.moneyswagger.chatroom.model;

import com.kakaopay.moneyswagger.util.BaseTimeEntity;
import com.kakaopay.moneyswagger.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseTimeEntity {
    @Id
    @Column(name = "chat_room_id")
    private String id;

    @OneToMany(mappedBy = "chatroom")
    private List<Member> members = new ArrayList<>();

    @Builder
    public ChatRoom(String id, List<Member> members) {
        this.id = id;
        this.members = members;
    }
}
