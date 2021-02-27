package com.kakaopay.moneyswagger.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CreateChatRoomDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        private List<Long> memberIds = new ArrayList<>();

        @Builder
        public Request(List<Long> members) {
            this.memberIds = members;
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class MemberInfo {
        private Long memberId;
        private String memberName;

        @Builder
        public MemberInfo(Long memberId, String memberName) {
            this.memberId = memberId;
            this.memberName = memberName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private List<MemberInfo> memberInfos = new ArrayList<>();

        @Builder
        public Response(List<MemberInfo> memberInfos) {
            this.memberInfos = memberInfos;
        }
    }
}
