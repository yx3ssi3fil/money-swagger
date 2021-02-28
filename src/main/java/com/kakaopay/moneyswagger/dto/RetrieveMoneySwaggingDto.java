package com.kakaopay.moneyswagger.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class RetrieveMoneySwaggingDto {
    @Getter
    @NoArgsConstructor
    public static class Response {
        private LocalDateTime moneySwaggingTime;
        private Integer moneySwaggingAmount;
        private Integer completedAmount;
        private List<CompletedInfo> completedInfos;

        @Builder
        public Response(LocalDateTime moneySwaggingTime, Integer moneySwaggingAmount, Integer completedAmount, List<CompletedInfo> completedInfos) {
            this.moneySwaggingTime = moneySwaggingTime;
            this.moneySwaggingAmount = moneySwaggingAmount;
            this.completedAmount = completedAmount;
            this.completedInfos = completedInfos;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CompletedInfo {
        private String receiverName;
        private Integer receivedAmount;

        @Builder
        public CompletedInfo(String receiverName, Integer receivedAmount) {
            this.receiverName = receiverName;
            this.receivedAmount = receivedAmount;
        }
    }
}
