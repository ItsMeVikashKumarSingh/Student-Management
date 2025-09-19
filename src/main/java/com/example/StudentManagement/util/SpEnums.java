package com.example.StudentManagement.util;

public final class SpEnums {
    private SpEnums() {}

    public enum EntityType {
        COURSE("COURSE"), STUDENT("STUDENT"), TEACHER("TEACHER"), DOCUMENT("DOCUMENT");
        private final String code;
        EntityType(String c) { this.code = c; }
        public String code() { return code; }
    }

    public enum ActionType {
        ADD("ADD"), UPDATE("UPDATE"), DELETE("DELETE"), GET("GET"), GET_PIC("GET_PIC"),
        LOGIN("LOGIN"), GET_BY_ID("GET_BY_ID"), GET_BY_COURSE("GET_BY_COURSE"),
        GET_COURSE_NAME("GET_COURSE_NAME");  // ✅ NEW ACTION ADDED

        private final String code;
        ActionType(String c) { this.code = c; }
        public String code() { return code; }
    }
}
