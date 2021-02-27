package com.kakaopay.moneyswagger.dto;

import com.kakaopay.moneyswagger.entity.account.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TransferDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        private Long giverMemberId;

        @NotNull
        private String giverName;

        @NotNull
        private Long receiverAccountId;

        @NotNull
        private String receiverName;

        @Min(1)
        private Integer transferAmount; //계좌이체 금액

        @Builder
        public Request(Long giverMemberId, String giverName, Long receiverAccountId, String receiverName, Integer transferAmount) {
            this.giverMemberId = giverMemberId;
            this.giverName = giverName;
            this.receiverAccountId = receiverAccountId;
            this.receiverName = receiverName;
            this.transferAmount = transferAmount;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private String giverName;
        private Long giverMemberId;
        private Long giverAccountId;
        private Integer giverBalance; //계좌이체 후 잔액
        private String receiverName;
        private Long receiverAccountId;
        private Integer transferAmount; //계좌이체 금액

        @Builder
        public Response(String giverName, Long giverMemberId, Long giverAccountId, Integer giverBalance, String receiverName, Long receiverAccountId, Integer transferAmount) {
            this.giverName = giverName;
            this.giverMemberId = giverMemberId;
            this.giverAccountId = giverAccountId;
            this.giverBalance = giverBalance;
            this.receiverName = receiverName;
            this.receiverAccountId = receiverAccountId;
            this.transferAmount = transferAmount;
        }

        public static Response from(Account giverAfterTransfer, Account receiverAfterTransfer) {
            return Response.builder()
                    .giverAccountId(giverAfterTransfer.getId())
                    .giverBalance(giverAfterTransfer.getBalance().intValue())
                    .giverMemberId(giverAfterTransfer.getMember().getId())
                    .giverName(giverAfterTransfer.getMember().getName())
                    .receiverAccountId(receiverAfterTransfer.getId())
                    .receiverName(receiverAfterTransfer.getMember().getName())
                    .build();
        }
    }
}
