package com.kakaopay.moneyswagger.moneyswagging.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MoneyAcceptanceDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        private String token;

        public Request(String token) {
            this.token = token;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Integer receiveAmount;

        @Builder
        public Response(Integer receiveAmount) {
            this.receiveAmount = receiveAmount;
        }
    }
}
