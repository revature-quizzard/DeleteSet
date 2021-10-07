package com.revature.delete_set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.revature.delete_set.stubs.TestLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeleteSetHandlerTest {

    static TestLogger testLogger;

    DeleteSetHandler sut;
    Context mockContext;
    UserRepo mockUserRepo;
    SetRepo mockSetRepo;


    @BeforeAll
    static void beforeAll() { testLogger = new TestLogger(); }

    @BeforeEach
    void setUp() {
        mockUserRepo = mock(UserRepo.class);
        mockSetRepo = mock(SetRepo.class);
        sut = new DeleteSetHandler(mockSetRepo, mockUserRepo);

        mockContext = mock(Context.class);
        when(mockContext.getLogger()).thenReturn(testLogger);

        User user = User.builder()
                .id("valid")
                .username("valid")
                .build();

        when(mockSetRepo.deleteSetById(anyString())).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        mockContext = null;
        mockSetRepo = null;
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