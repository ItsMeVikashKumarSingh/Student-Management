package com.example.StudentManagement.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SpPayloadUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private SpPayloadUtil() {}

    public static String save(Object payload) {
        if (payload == null) return null;
        try { return MAPPER.writeValueAsString(payload); }
        catch (JsonProcessingException e) { throw new RuntimeException("Failed to build JSON payload", e); }
    }

    public static String saveId(int id) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id);
        return save(m);
    }

    public static String saveRoll(int roll) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("roll", roll);
        return save(m);
    }
}
