package com.kakaopay.moneyswagger.chatroom.dto;

import com.kakaopay.moneyswagger.member.model.Member;
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
        private String chatRoomId;

        @Builder
        public MemberInfo(Long memberId, String memberName, String chatRoomId) {
            this.memberId = memberId;
            this.memberName = memberName;
            this.chatRoomId = chatRoomId;
        }

        public static MemberInfo from(Member member) {
            return MemberInfo.builder()
                    .memberName(member.getName())
                    .memberId(member.getId())
                    .chatRoomId(member.getChatroom().getId())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private String chatRoomId;
        private List<MemberInfo> memberInfos = new ArrayList<>();

        @Builder
        public Response(String chatRoomId, List<MemberInfo> memberInfos) {
            this.chatRoomId = chatRoomId;
            this.memberInfos = memberInfos;
        }
    }
}
