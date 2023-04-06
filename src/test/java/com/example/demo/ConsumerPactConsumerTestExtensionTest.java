package com.example.demo;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import com.example.demo.inner.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactDirectory("build/pact") //@PactBroker(url="http://localhost:8090")
// https://github.com/pact-foundation/pact-jvm/tree/master/consumer/junit5
public class ConsumerPactConsumerTestExtensionTest {

    public static final String TEST_PROVIDER = "test_provider";

    @BeforeEach
    void setUp() {
        System.setProperty("pact_do_not_track", "true");
    }

    @Pact(provider = TEST_PROVIDER, consumer = "test_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) throws JsonProcessingException {
        Map<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Type", "application/json");

        Controller.Dummy body = new Controller.Dummy("bname");
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(body);

        return builder
                .given("test state")
                .uponReceiving("ExampleJavaConsumerPactTest test interaction")
                .path("/name")
                .method("GET")
                .matchHeader("Authorization", "Bearer xxxxxx")
                .willRespondWith()
                .headers(responseHeader)
                .status(200)
                .body(newJsonBody(object -> {
                            object.stringType("name", "aname");
                        }).build()
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact", pactVersion = PactSpecVersion.V3)
    void demoTest(MockServer mockServer) {
        RestTemplate template = new RestTemplateBuilder()
                .defaultHeader("Authorization", "Bearer xxxxxx")
                .build();
        ResponseEntity<Controller.Dummy> forEntity = template.getForEntity(mockServer.getUrl() + "/name", Controller.Dummy.class);


        assertThat(forEntity.getStatusCode().value()).isEqualTo(200);
    }
}
