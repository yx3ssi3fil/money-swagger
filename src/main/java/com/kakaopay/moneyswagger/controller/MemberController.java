package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateMemberDto;
import com.kakaopay.moneyswagger.entity.member.Member;
import com.kakaopay.moneyswagger.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    public static final String URL_MEMBERS = "/members";

    private final MemberService memberService;

    @PostMapping(URL_MEMBERS)
    public ResponseEntity<CreateMemberDto.Response> createMember(@RequestBody CreateMemberDto.Request request) {
        Member savedMember = memberService.createMember(request.toEntity());
        CreateMemberDto.Response responseBody = CreateMemberDto.Response.from(savedMember);

        return ResponseEntity
                .created(URI.create(URL_MEMBERS + "/" + responseBody.getId()))
                .body(responseBody);
    }
}
