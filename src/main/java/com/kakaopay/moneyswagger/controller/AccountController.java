package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.dto.DepositDto;
import com.kakaopay.moneyswagger.dto.RetrieveAccountDto;
import com.kakaopay.moneyswagger.dto.TransferDto;
import com.kakaopay.moneyswagger.entity.TransferRole;
import com.kakaopay.moneyswagger.entity.account.Account;
import com.kakaopay.moneyswagger.entity.member.Member;
import com.kakaopay.moneyswagger.service.AccountService;
import com.kakaopay.moneyswagger.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController {
    public static final String URL_CREATE_ACCOUNT = "/accounts";
    public static final String URL_RETRIEVE_ACCOUNT = "/accounts/{accountId}";
    public static final String URL_DEPOSIT = "/accounts/{accountId}/deposit";
    public static final String URL_TRANSFER = "/accounts/{accountId}/transfer";

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

    @PutMapping(URL_DEPOSIT)
    public ResponseEntity<DepositDto.Response> deposit(@PathVariable Long accountId, @RequestBody @Valid DepositDto.Request request) {
        Optional<Account> optionalAccount = accountService.retrieveById(accountId);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Account account = optionalAccount.get();
        if (!account.getMember().getId().equals(request.getMemberId())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Account accountAfterDeposit = accountService.deposit(account, request.getDepositAmount());
        DepositDto.Response responseBody = DepositDto.Response.from(accountAfterDeposit);
        return ResponseEntity
                .ok(responseBody);
    }

    @PutMapping(URL_TRANSFER)
    public ResponseEntity<TransferDto.Response> transfer(@PathVariable Long accountId, @RequestBody @Valid TransferDto.Request request) {
        Optional<Account> optionalGiverAccount = accountService.retrieveById(accountId);
        if (optionalGiverAccount.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Account giverAccount = optionalGiverAccount.get();
        if (!giverAccount.getMember().getId().equals(request.getGiverMemberId())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Optional<Account> optionalReceiverAccount = accountService.retrieveById(request.getReceiverAccountId());
        if (optionalReceiverAccount.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Account receiverAccount = optionalReceiverAccount.get();
        if (!receiverAccount.getMember().getName().equals(request.getReceiverName())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        if (giverAccount.getBalance().intValue() < request.getTransferAmount()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Map<TransferRole, Account> accountsAfterTransfer = accountService.transfer(giverAccount, receiverAccount, request.getTransferAmount());
        Account giverAfterTransfer = accountsAfterTransfer.get(TransferRole.GIVER);
        Account receiverAfterTransfer = accountsAfterTransfer.get(TransferRole.RECEIVER);
        TransferDto.Response responseBody = TransferDto.Response.from(giverAfterTransfer, receiverAfterTransfer);
        return ResponseEntity
                .ok()
                .body(responseBody);
    }
}
