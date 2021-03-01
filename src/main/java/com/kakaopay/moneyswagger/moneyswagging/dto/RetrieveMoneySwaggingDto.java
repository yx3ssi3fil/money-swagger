package com.kakaopay.moneyswagger.moneyswagging.dto;

import com.kakaopay.moneyswagger.moneyswagging.model.MoneyPortion;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwagging;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RetrieveMoneySwaggingDto {
    @Getter
    @NoArgsConstructor
    public static class Response {
        private LocalDateTime moneySwaggingTime;
        private Integer moneySwaggingAmount;
        private Integer completedAmount;
        private List<CompletedInfo> completedInfos = Collections.EMPTY_LIST;

        @Builder
        public Response(LocalDateTime moneySwaggingTime, Integer moneySwaggingAmount, Integer completedAmount, List<CompletedInfo> completedInfos) {
            this.moneySwaggingTime = moneySwaggingTime;
            this.moneySwaggingAmount = moneySwaggingAmount;
            this.completedAmount = completedAmount;
            this.completedInfos = completedInfos;
        }

        public static Response from(MoneySwagging moneySwagging) {
            List<MoneyPortion> completedMoneyPortions = moneySwagging.getCompletedMoneyPortions();
            int completedAmount = completedMoneyPortions.stream()
                    .mapToInt(MoneyPortion::getAmount)
                    .sum();

            List<CompletedInfo> completedInfos = completedMoneyPortions.stream()
                    .map(moneyPortion -> new CompletedInfo(moneyPortion.getReceiver().getName(), moneyPortion.getAmount()))
                    .collect(Collectors.toList());

            return Response.builder()
                    .moneySwaggingAmount(moneySwagging.getAmount())
                    .moneySwaggingTime(moneySwagging.getCreatedDate())
                    .completedAmount(completedAmount)
                    .completedInfos(completedInfos)
                    .build();
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
