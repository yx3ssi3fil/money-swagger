package com.kakaopay.moneyswagger.dto;

import com.kakaopay.moneyswagger.entity.member.Member;
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

        public Member toEntity() {
            return Member.builder()
                    .name(this.name)
                    .build();
        }
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

        public static Response from(Member member) {
            return Response.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .build();
        }
    }
}
