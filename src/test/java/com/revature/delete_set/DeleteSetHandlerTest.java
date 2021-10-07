package com.revature.delete_set;

import com.amazonaws.services.lambda.runtime.Context;
import com.revature.delete_set.stubs.TestLogger;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
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

        sut = new DeleteSetHandler(mockUserRepo, mockSetRepo);

        when(mockContext.getLogger()).thenReturn(testLogger);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        mockContext = null;
        mockUserRepo = null;
    }

    @Test
    void handleRequest() {
    }
}