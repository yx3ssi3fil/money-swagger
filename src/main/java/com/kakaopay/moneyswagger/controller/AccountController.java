package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.dto.RetrieveAccountDto;
import com.kakaopay.moneyswagger.entity.account.Account;
import com.kakaopay.moneyswagger.entity.member.Member;
import com.kakaopay.moneyswagger.service.AccountService;
import com.kakaopay.moneyswagger.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController {
    public static final String URL_CREATE_ACCOUNT = "/accounts";
    public static final String URL_RETRIEVE_ACCOUNT = "/accounts/{accountId}";

    private final AccountService accountService;
    private final MemberService memberService;

    @PostMapping(URL_CREATE_ACCOUNT)
    public ResponseEntity<CreateAccountDto.Response> createAccount(@RequestBody CreateAccountDto.Request request) {
        Optional<Member> optionalMember = memberService.retrieveMemberById(request.getMemberId());
        if (optionalMember.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Member member = optionalMember.get();
        Account savedAccount = accountService.createAccount(member);
        CreateAccountDto.Response responseBody = CreateAccountDto.Response.from(savedAccount);
        return ResponseEntity
                .created(URI.create(URL_CREATE_ACCOUNT + "/" + savedAccount.getId()))
                .body(responseBody);
    }

    @GetMapping(URL_RETRIEVE_ACCOUNT)
    public ResponseEntity<RetrieveAccountDto.Response> retrieveById(@PathVariable Long accountId) {
        Optional<Account> optionalAccount = accountService.retrieveById(accountId);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Account account = optionalAccount.get();
        RetrieveAccountDto.Response responseBody = RetrieveAccountDto.Response.from(account);
        return ResponseEntity
                .ok()
                .body(responseBody);
    }
}
