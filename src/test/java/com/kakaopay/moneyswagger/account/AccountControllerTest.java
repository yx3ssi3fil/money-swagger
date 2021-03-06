package com.kakaopay.moneyswagger.account;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.account.dto.CreateAccountDto;
import com.kakaopay.moneyswagger.account.dto.DepositDto;
import com.kakaopay.moneyswagger.account.dto.RetrieveAccountDto;
import com.kakaopay.moneyswagger.account.dto.TransferDto;
import com.kakaopay.moneyswagger.member.MemberHttpTest;
import com.kakaopay.moneyswagger.member.dto.CreateMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountControllerTest extends AbstractControllerTest {
    private AccountHttpTest accountHttpTest;
    private MemberHttpTest memberHttpTest;
    private Long memberId;

    @BeforeEach
    void setUp() {
        accountHttpTest = new AccountHttpTest(webTestClient);
        memberHttpTest = new MemberHttpTest(webTestClient);
        memberId = memberHttpTest.createMember("name").getId();
    }

    @DisplayName("계좌 생성")
    @Test
    void createAccount() {
        //when
        CreateAccountDto.Response responseBody = accountHttpTest.createAccount(memberId);

        //then
        assertThat(responseBody.getAccountId()).isNotNull().isNotNegative();
        assertThat(responseBody.getMemberId()).isEqualTo(memberId);
        assertThat(responseBody.getMemberName()).isEqualTo("name");
        assertThat(responseBody.getBalance()).isEqualTo(0);
    }

    @DisplayName("계좌 조회(by Id)")
    @Test
    void retrieveAccount() {
        //given
        Long accountId = accountHttpTest.createAccount(memberId).getAccountId();

        //when
        RetrieveAccountDto.Response responseBody = accountHttpTest.retrieveAccount(accountId);

        //then
        assertThat(responseBody.getAccountId()).isEqualTo(accountId);
        assertThat(responseBody.getBalance()).isEqualTo(0);
        assertThat(responseBody.getMemberId()).isEqualTo(memberId);
        assertThat(responseBody.getMemberName()).isEqualTo("name");
    }

    @DisplayName("계좌 조회(by Id) - Invalid accountId")
    @Test
    void retrieveByIdWhenInvalidAccountId() {
        //given
        Long accountId = 12345555L;

        //when, then
        webTestClient.get().uri(AccountController.URL_RETRIEVE_ACCOUNT, accountId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("입금")
    @Test
    void deposit() {
        //given
        Long accountId = accountHttpTest.createAccount(memberId).getAccountId();
        Integer depositAmount = 2000;

        //when
        DepositDto.Response responseBody = accountHttpTest.deposit(accountId, memberId, depositAmount);

        //then
        assertThat(responseBody.getAccountId()).isEqualTo(accountId);
        assertThat(responseBody.getBalance()).isEqualTo(depositAmount);
    }

    @DisplayName("입금 - invalid depositAmount")
    @Test
    void depositWhenInvalidAmount() {
        //given
        Long accountId = accountHttpTest.createAccount(memberId).getAccountId();
        Integer depositAmount = 0;
        DepositDto.Request requestBody = DepositDto.Request.builder()
                .memberId(memberId)
                .depositAmount(depositAmount)
                .build();

        //when
        webTestClient.put().uri(AccountController.URL_DEPOSIT, accountId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(DepositDto.Response.class);
    }

    @DisplayName("계좌이체(giver -> receiver)")
    @Test
    void withdraw() {
        //given
        CreateMemberDto.Response giver = memberHttpTest.createMember("giver");
        CreateAccountDto.Response giverAccount = accountHttpTest.createAccount(giver.getId());
        accountHttpTest.deposit(giverAccount.getAccountId(), giver.getId(), 100000);
        CreateMemberDto.Response receiver = memberHttpTest.createMember("receiver");
        CreateAccountDto.Response receiverAccount = accountHttpTest.createAccount(receiver.getId());
        Integer transferAmount = 20000;

        //when
         TransferDto.Response responseBody = accountHttpTest.transfer(transferAmount, giver, giverAccount, receiver, receiverAccount);

        //then
        assertThat(responseBody.getGiverAccountId()).isEqualTo(giverAccount.getAccountId());
        assertThat(responseBody.getGiverBalance()).isEqualTo(100000 - transferAmount);
        assertThat(responseBody.getReceiverAccountId()).isEqualTo(receiverAccount.getAccountId());
        assertThat(responseBody.getReceiverName()).isEqualTo(receiverAccount.getMemberName());
        RetrieveAccountDto.Response receiverAccountAfterTransfer = accountHttpTest.retrieveAccount(receiverAccount.getAccountId());
        assertThat(receiverAccountAfterTransfer.getBalance()).isEqualTo(transferAmount);
    }
}
