package com.kakaopay.moneyswagger.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RetrieveAccountDto {
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
    }
}
