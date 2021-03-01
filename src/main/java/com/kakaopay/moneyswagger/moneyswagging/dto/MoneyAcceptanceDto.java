package com.kakaopay.moneyswagger.moneyswagging.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MoneyAcceptanceDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        private String token;
        private String chatRoomId;

        @Builder
        public Request(String token, String chatRoomId) {
            this.token = token;
            this.chatRoomId = chatRoomId;
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
