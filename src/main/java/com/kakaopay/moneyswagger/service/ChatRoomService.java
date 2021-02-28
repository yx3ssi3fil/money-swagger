package com.kakaopay.moneyswagger.service;

import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
import com.kakaopay.moneyswagger.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    public ChatRoom createChatRoom(List<Long> memberIds) {
        List<Member> members = memberIds.stream()
                .map(id -> memberService.retrieveMemberById(id))
                .map(Optional::get)
                .collect(Collectors.toList());


        String uuid = RandomStringUtils.randomAlphabetic(10);
        ChatRoom chatRoom = ChatRoom.builder()
                .id(uuid)
                .members(members)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        members.stream()
                .forEach(member -> member.joinChatRoom(savedChatRoom));

        return chatRoom;
    }

    public Optional<ChatRoom> retrieveById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

}
