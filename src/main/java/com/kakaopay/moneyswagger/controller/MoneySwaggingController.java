package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.entity.Header;
import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
import com.kakaopay.moneyswagger.service.ChatRoomService;
import com.kakaopay.moneyswagger.service.MemberService;
import com.kakaopay.moneyswagger.service.MoneySwaggingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MoneySwaggingController {
    public static final String URL_CREATE_MONEY_SWAGGING = "/money-swaggings";

    private final MoneySwaggingService moneySwaggingService;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    @PostMapping(URL_CREATE_MONEY_SWAGGING)
    public ResponseEntity<CreateMoneySwaggingDto.Response> create(HttpServletRequest httpServletRequest,
                                                                  @RequestBody @Valid CreateMoneySwaggingDto.Request request) {
        String chatRoomId = httpServletRequest.getHeader(Header.CHAT_ROOM_ID.getKey());
        String memberId = httpServletRequest.getHeader(Header.USER_ID.getKey());

        if (Strings.isBlank(chatRoomId) || Strings.isBlank(memberId)) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Long giverId;
        try {
            giverId = Long.valueOf(memberId);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Optional<Member> optionalGiver = memberService.retrieveMemberById(giverId);
        Optional<ChatRoom> optionalChatRoom = chatRoomService.retrieveById(chatRoomId);

        if (optionalGiver.isEmpty() || optionalChatRoom.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneySwagging moneySwagging = MoneySwagging.builder()
                .member(optionalGiver.get())
                .chatRoom(optionalChatRoom.get())
                .build();
        MoneySwagging savedMoneySwagging = moneySwaggingService.createMoneySwagging(moneySwagging);
        return null;
    }
}
