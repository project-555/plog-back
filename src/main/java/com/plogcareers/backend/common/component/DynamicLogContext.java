package com.plogcareers.backend.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.SortedMap;
import java.util.TreeMap;


@Getter
@Component
public class DynamicLogContext {
    private final ObjectMapper mapper;

    private final SortedMap<String, Object> context;
    private boolean fixedValueExists = true;

    public DynamicLogContext(ObjectMapper mapper) {
        this.context = new TreeMap<>();
        this.mapper = mapper;
    }

    public void setFixedValueExists(boolean fixedValueExists) {
        this.fixedValueExists = fixedValueExists;
    }

    public void addField(String key, Object value) {
        context.put(key, value);
    }

    public void addFields(SortedMap<String, Object> fields) {
        context.putAll(fields);
    }

    public void clear() {
        context.clear();
    }

    public void remove(String key) {
        context.remove(key);
    }

    public String toString() {
        if (context.isEmpty()) {
            return "";
        }

        try {
            String fieldJSON = mapper.writeValueAsString(context);
            String result = fieldJSON.substring(1, fieldJSON.length() - 1); // remove curly braces

            if (fixedValueExists) {
                return "," + result;
            }
            return result;
        } catch (Exception e) {
            return "";
        }
    }

}
