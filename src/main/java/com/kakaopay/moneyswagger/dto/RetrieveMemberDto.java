package com.kakaopay.moneyswagger.dto;

import com.kakaopay.moneyswagger.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RetrieveMemberDto {
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

        public static Response from(Member member) {
            return Response.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .build();
        }
    }
}
