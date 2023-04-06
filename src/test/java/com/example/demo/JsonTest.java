package com.example.demo;

import com.example.demo.inner.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class JsonTest {

    ObjectMapper objectMapper = new ObjectMapper();
    Controller.Dummy dummy = new Controller.Dummy("bname");
    @Test
    void name() throws JsonProcessingException {
        String string = objectMapper.writeValueAsString(dummy);
        Controller.Dummy dummy1 = objectMapper.readValue(string, Controller.Dummy.class);
    }
}
