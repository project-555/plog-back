package com.plogcareers.backend.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.SortedMap;
import java.util.TreeMap;


@SpringBootTest
class DynamicLogContextTest {
    @Autowired
    private ObjectMapper mapper;

    private DynamicLogContext dynamicLogContext;

    @BeforeEach
    void setUp() {
        dynamicLogContext = new DynamicLogContext(mapper);
    }

    @Test
    @DisplayName("addField() should add a field to the context")
    void test() {
        // given
        dynamicLogContext.addField("key", "value");
        dynamicLogContext.addField("key2", "value2");

        // when
        String got = dynamicLogContext.toString();

        // then
        Assertions.assertEquals(",\"key\":\"value\",\"key2\":\"value2\"", got);
    }

    @Test
    @DisplayName("addFields() should add all fields to the context")
    void test_2() {
        // given
        SortedMap<String, Object> fields = new TreeMap<>();
        fields.put("key", "value");
        fields.put("key2", "value2");
        dynamicLogContext.addFields(fields);

        // when
        String got = dynamicLogContext.toString();

        // then
        Assertions.assertEquals(",\"key\":\"value\",\"key2\":\"value2\"", got);
    }

    @Test
    @DisplayName("toString() should return an empty string if the context is empty")
    void test_3() {
        // given + when
        String got = dynamicLogContext.toString();

        // then
        Assertions.assertEquals("", got);
    }

    @Test
    @DisplayName("clear() should clear the context")
    void test_4() {
        // given
        dynamicLogContext.addField("key", "value");
        dynamicLogContext.addField("key2", "value2");

        // when
        dynamicLogContext.clear();
        String got = dynamicLogContext.toString();

        // then
        Assertions.assertEquals("", got);
    }

    @Test
    @DisplayName("remove() should remove the field from the context")
    void test_5() {
        // given
        dynamicLogContext.addField("key", "value");
        dynamicLogContext.addField("key2", "value2");

        // when
        dynamicLogContext.remove("key");
        String got = dynamicLogContext.toString();

        // then
        Assertions.assertEquals(",\"key2\":\"value2\"", got);
    }
}