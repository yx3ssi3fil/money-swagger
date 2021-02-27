package com.kakaopay.moneyswagger.controller;


import com.kakaopay.moneyswagger.dto.CreateChatRoomDto;
import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    public static final String URL_CREATE_CHAT_ROOM = "/chat-rooms";

    private final ChatRoomService chatRoomService;

    @PostMapping(URL_CREATE_CHAT_ROOM)
    public ResponseEntity<CreateChatRoomDto.Response> createChatRoom(@RequestBody CreateChatRoomDto.Request request) {
        if (CollectionUtils.isEmpty(request.getMemberIds())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        ChatRoom chatRoom = chatRoomService.createChatRoom(request.getMemberIds());
        List<CreateChatRoomDto.MemberInfo> memberInfos = chatRoom.getMembers().stream()
                .map(CreateChatRoomDto.MemberInfo::from)
                .collect(Collectors.toList());
        CreateChatRoomDto.Response responseBody = CreateChatRoomDto.Response.builder()
                .chatRoomId(chatRoom.getId())
                .memberInfos(memberInfos)
                .build();

        return ResponseEntity
                .created(URI.create(URL_CREATE_CHAT_ROOM + "/" + chatRoom.getId()))
                .body(responseBody);
    }
}
