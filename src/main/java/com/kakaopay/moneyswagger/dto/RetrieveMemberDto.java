package com.kakaopay.moneyswagger.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class RetrieveMemberDto {
    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String name;
    }
}
