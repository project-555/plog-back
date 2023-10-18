package com.plogcareers.backend.common.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@RequiredArgsConstructor
@Getter
@Setter
@Component
public class ArgumentLogFormatter {
    private final ObjectMapper mapper;

    public String json(String[] argsKeys, Object[] argsValues) {
        HashMap<String, Object> map = new HashMap<>();

        for (int i = 0; i < argsKeys.length; i++) {
            map.put(argsKeys[i], argsValues[i]);
        }

        try {
            String result = mapper.writeValueAsString(map);
            if (result.equals(""))
                return "null";
            return result;
        } catch (JsonProcessingException e) {
            return "null";
        }
    }
}
