package com.kakaopay.moneyswagger.dto;

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
        private String token;
        private LocalDateTime createdTime;
        private List<Integer> dividedAmounts = new ArrayList<>();
    }
}
