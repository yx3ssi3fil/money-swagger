package com.kakaopay.moneyswagger.member;

import com.kakaopay.moneyswagger.member.dto.CreateMemberDto;
import com.kakaopay.moneyswagger.member.dto.RetrieveMemberDto;
import com.kakaopay.moneyswagger.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    public static final String URL_CREATE_MEMBER = "/members";
    public static final String URL_RETRIEVE_MEMBER = "/members/{id}";

    private final MemberService memberService;

    @PostMapping(URL_CREATE_MEMBER)
    public ResponseEntity<CreateMemberDto.Response> createMember(@RequestBody CreateMemberDto.Request request) {
        Member savedMember = memberService.createMember(request.toEntity());
        CreateMemberDto.Response responseBody = CreateMemberDto.Response.from(savedMember);

        return ResponseEntity
                .created(URI.create(URL_CREATE_MEMBER + "/" + responseBody.getId()))
                .body(responseBody);
    }

    @GetMapping(URL_RETRIEVE_MEMBER)
    public ResponseEntity<RetrieveMemberDto.Response> retrieveMemberById(@PathVariable Long id) {
        Optional<Member> optionalMember = memberService.retrieveMemberById(id);
        if (optionalMember.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Member memberById = optionalMember.get();
        RetrieveMemberDto.Response responseBody = RetrieveMemberDto.Response.from(memberById);
        return ResponseEntity
                .ok()
                .body(responseBody);
    }
}
