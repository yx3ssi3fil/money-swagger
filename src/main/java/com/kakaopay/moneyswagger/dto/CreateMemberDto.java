package com.kakaopay.moneyswagger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateMemberDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String name;

        @Builder
        public Response(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
