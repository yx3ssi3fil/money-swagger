package com.kakaopay.moneyswagger.account;

import com.kakaopay.moneyswagger.account.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.account.dto.DepositDto;
import com.kakaopay.moneyswagger.account.dto.RetrieveAccountDto;
import com.kakaopay.moneyswagger.account.dto.TransferDto;
import com.kakaopay.moneyswagger.account.model.Account;
import com.kakaopay.moneyswagger.account.model.TransferRole;
import com.kakaopay.moneyswagger.member.MemberService;
import com.kakaopay.moneyswagger.member.model.Member;
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

    private static final String SLASH = "/";

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
                .created(URI.create(URL_CREATE_ACCOUNT + SLASH + savedAccount.getId()))
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
        if (!account.isOwner(request.getMemberId())) {
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
    public ResponseEntity<TransferDto.Response> transfer(@PathVariable Long accountId,
                                                         @RequestBody @Valid TransferDto.Request request) {
        Optional<Account> optionalGiverAccount = accountService.getGiverAccount(accountId, request.getGiverMemberId());
        if (optionalGiverAccount.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Account giverAccount = optionalGiverAccount.get();
        Optional<Account> receiverAccount = accountService.getReceiverAccount(request.getReceiverAccountId());
        if (receiverAccount.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Map<TransferRole, Account> accountsAfterTransfer = accountService.transfer(giverAccount, receiverAccount.get(), request.getTransferAmount());
        TransferDto.Response responseBody = TransferDto.Response.from(accountsAfterTransfer);
        return ResponseEntity
                .ok()
                .body(responseBody);
    }
}
