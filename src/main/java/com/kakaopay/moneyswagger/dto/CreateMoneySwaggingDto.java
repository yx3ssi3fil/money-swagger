package com.kakaopay.moneyswagger.dto;

import com.kakaopay.moneyswagger.entity.account.MoneyPortion;
import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateMoneySwaggingDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private Integer amount;
        private Integer peopleCount;

        @Builder
        public Request(Integer amount, Integer peopleCount) {
            this.amount = amount;
            this.peopleCount = peopleCount;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long moneySwaggingId;
        private String token;
        private LocalDateTime createdTime;

        @Builder
        public Response(Long moneySwaggingId, String token, LocalDateTime createdTime) {
            this.moneySwaggingId = moneySwaggingId;
            this.token = token;
            this.createdTime = createdTime;
        }

        public static Response from(MoneySwagging moneySwagging) {
            return Response.builder()
                    .moneySwaggingId(moneySwagging.getId())
                    .token(moneySwagging.getToken())
                    .createdTime(moneySwagging.getCreatedDate())
                    .build();
        }
    }
}
