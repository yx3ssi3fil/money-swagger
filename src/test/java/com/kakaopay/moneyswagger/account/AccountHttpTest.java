package com.kakaopay.moneyswagger.account;

import com.kakaopay.moneyswagger.account.AccountController;
import com.kakaopay.moneyswagger.account.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.account.dto.DepositDto;
import com.kakaopay.moneyswagger.account.dto.RetrieveAccountDto;
import com.kakaopay.moneyswagger.account.dto.TransferDto;
import com.kakaopay.moneyswagger.member.dto.CreateMemberDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class AccountHttpTest {
    private WebTestClient webTestClient;

    public AccountHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public CreateAccountDto.Response createAccount(Long memberId) {
        CreateAccountDto.Request requestBody = new CreateAccountDto.Request(memberId);

        return webTestClient.post().uri(AccountController.URL_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateAccountDto.Response.class)
                .returnResult()
                .getResponseBody();
    }

    public RetrieveAccountDto.Response retrieveAccount(Long accountId) {
        return webTestClient.get().uri(AccountController.URL_RETRIEVE_ACCOUNT, accountId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RetrieveAccountDto.Response.class)
                .returnResult()
                .getResponseBody();
    }

    public DepositDto.Response deposit(Long accountId, Long memberId, Integer depositAmount) {
        DepositDto.Request requestBody = DepositDto.Request.builder()
                .memberId(memberId)
                .depositAmount(depositAmount)
                .build();

        return webTestClient.put().uri(AccountController.URL_DEPOSIT, accountId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DepositDto.Response.class)
                .returnResult()
                .getResponseBody();
    }

    public TransferDto.Response transfer(Integer transferAmount,
                                         CreateMemberDto.Response giver, CreateAccountDto.Response giverAccount,
                                         CreateMemberDto.Response receiver, CreateAccountDto.Response receiverAccount) {

        TransferDto.Request requestBody = TransferDto.Request.builder()
                .transferAmount(transferAmount)
                .giverName(giver.getName())
                .giverMemberId(giver.getId())
                .receiverAccountId(receiverAccount.getAccountId())
                .receiverName(receiverAccount.getMemberName())
                .build();

        return webTestClient.put().uri(AccountController.URL_TRANSFER, giverAccount.getAccountId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransferDto.Response.class)
                .returnResult()
                .getResponseBody();
    }
}
