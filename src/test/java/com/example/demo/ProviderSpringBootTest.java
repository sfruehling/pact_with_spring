package com.example.demo;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.apache.hc.core5.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("test_provider")
@PactFolder("build/pact")
public class ProviderSpringBootTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void before(PactVerificationContext context) {
        System.setProperty("pact_do_not_track", "true");
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context, HttpRequest httpRequest) {
        replaceAuthHeader(httpRequest);
        context.verifyInteraction();
    }

    private void replaceAuthHeader(HttpRequest request) {
        if (request.containsHeader("Authorization")) {
            String header = "Bearer xxxxx";
            request.removeHeaders("Authorization");
            request.addHeader("Authorization", header);
        }
    }


    @State("test state")
    void name() {
        // nothing to do/mock here
    }
}
