package com.kakaopay.moneyswagger.entity;

public enum Header {
    USER_ID("X-USER-ID"),
    CHAT_ROOM_ID("X-ROOM-ID");

    private String key;

    Header(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
