package com.kakaopay.moneyswagger.member.model;

import com.kakaopay.moneyswagger.account.model.Account;
import com.kakaopay.moneyswagger.util.BaseTimeEntity;
import com.kakaopay.moneyswagger.chatroom.model.ChatRoom;
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

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatroom;

    @Builder
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String name) {
        this.name = name;
    }

    public void joinChatRoom(ChatRoom chatRoom) {
        this.chatroom = chatRoom;
    }

    public Account getMajorAccount() {
        return this.accounts.get(0);
    }
}
