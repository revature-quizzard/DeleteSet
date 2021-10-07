package com.revature.delete_set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.revature.delete_set.stubs.TestLogger;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeleteSetHandlerTest {

    DeleteSetHandler sut;
    Context mockContext;
    UserRepo mockUserRepo;
    SetRepo mockSetRepo;
    static TestLogger testLogger;

    @BeforeAll
    static void beforeAll() {
        testLogger = new TestLogger();
    }

    @AfterAll
    static void afterAll() {
        testLogger.close();
    }

    @BeforeEach
    void setUp() {
        mockContext = mock(Context.class);
        mockUserRepo = mock(UserRepo.class);
        mockSetRepo = mock(SetRepo.class);

        User user = User.builder()
                .id("valid")
                .username("valid")
                .build();

        sut = new DeleteSetHandler(mockSetRepo, mockUserRepo);

        when(mockContext.getLogger()).thenReturn(testLogger);
        when(mockSetRepo.deleteSetById(anyString())).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        mockContext = null;
        mockUserRepo = null;
    }

    @Test
    void handleRequest() {
        // Arrange
        APIGatewayProxyRequestEvent mockRequest = new APIGatewayProxyRequestEvent();
        mockRequest.withPath("/sets/cards");
        mockRequest.withHttpMethod("DELETE");
        mockRequest.withPathParameters(Collections.singletonMap("id", "lmao"));

        // Act
        APIGatewayProxyResponseEvent responseEvent = sut.handleRequest(mockRequest, mockContext);

        // Assert
        assertEquals(200, responseEvent.getStatusCode());
    }
}