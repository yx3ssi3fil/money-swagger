package com.kakaopay.moneyswagger.dto;

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
        private Long memberName;
    }
}
