package com.kakaopay.moneyswagger.repository;

import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<String, ChatRoom> {
}
