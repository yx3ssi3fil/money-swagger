package com.kakaopay.moneyswagger.account.dto;

import com.kakaopay.moneyswagger.account.model.Account;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

public class DepositDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        private Long memberId;
        @Min(value = 1, message = "depositAmount should be greater than 0")
        private Integer depositAmount;

        @Builder
        public Request(Long memberId, Integer depositAmount) {
            this.memberId = memberId;
            this.depositAmount = depositAmount;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long accountId;
        private Long memberId;
        private String memberName;
        private Integer balance;

        @Builder
        public Response(Long accountId, Long memberId, String memberName, Integer balance) {
            this.accountId = accountId;
            this.memberId = memberId;
            this.memberName = memberName;
            this.balance = balance;
        }

        public static DepositDto.Response from(Account account) {
            return DepositDto.Response.builder()
                    .accountId(account.getId())
                    .balance(account.getBalance().intValue())
                    .memberId(account.getMember().getId())
                    .memberName(account.getMember().getName())
                    .build();
        }
    }
}
