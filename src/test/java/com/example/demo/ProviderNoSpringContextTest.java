package com.example.demo;

import au.com.dius.pact.core.model.annotations.PactDirectory;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.example.demo.inner.Controller;
import com.example.demo.inner.MyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Provider("test_provider")
@PactFolder("build/pact") //@PactBroker(url="http://localhost")
public class ProviderNoSpringContextTest {

    private final MyService myServiceMock = mock(MyService.class);
    private final Controller controller = new Controller(myServiceMock);

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    @SuppressWarnings("JUnitMalformedDeclaration")
    void before(PactVerificationContext context) {
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(controller);
        context.setTarget(testTarget);
    }

    @State({"test state"}) // Method will be run before testing interactions that require "default"
    public void toDefaultState() {
        when(myServiceMock.calcName()).thenReturn("bname");
    }


}
