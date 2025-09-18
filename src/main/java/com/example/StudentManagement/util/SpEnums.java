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
        ADD("ADD"), UPDATE("UPDATE"), DELETE("DELETE"), GET("GET"), GET_PIC("GET_PIC");
        private final String code;
        ActionType(String c) { this.code = c; }
        public String code() { return code; }
    }
}
