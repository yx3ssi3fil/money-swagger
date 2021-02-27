package com.kakaopay.moneyswagger.dto;

import com.kakaopay.moneyswagger.entity.account.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateAccountDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private Long memberId;

        @Builder
        public Request(Long memberId) {
            this.memberId = memberId;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long accountId;
        private Long memberId;
        private String memberName;
        private Integer balance; //잔

        @Builder
        public Response(Long accountId, Long memberId, String memberName, Integer balance) {
            this.accountId = accountId;
            this.memberId = memberId;
            this.memberName = memberName;
            this.balance = balance;
        }

        public static Response from(Account account) {
            return Response.builder()
                    .accountId(account.getId())
                    .memberId(account.getMember().getId())
                    .memberName(account.getMember().getName())
                    .balance(account.getBalance().intValue())
                    .build();

        }
    }
}
