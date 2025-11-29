package com.comp2042.view;

import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InstructionsControllerTest {

    private InstructionsController instructionsController;
    private StubTitleScreenController stubTitleController;

    // Stub for TitleScreenController
    static class StubTitleScreenController extends TitleScreenController {
        boolean closeInstructionsCalled = false;

        @Override
        public void closeInstructions() {
            closeInstructionsCalled = true;
        }
    }

    @BeforeEach
    void setUp() {
        instructionsController = new InstructionsController();
        stubTitleController = new StubTitleScreenController();
        instructionsController.setTitleScreenController(stubTitleController);
    }

    @Test
    void testHandleBack() {
        instructionsController.handleBack(new ActionEvent());
        assertTrue(stubTitleController.closeInstructionsCalled, "Back button should call closeInstructions");
    }

    @Test
    void testHandleBackSafety() {
        // Ensure no crash if controller is null
        InstructionsController orphanController = new InstructionsController();
        assertDoesNotThrow(() -> orphanController.handleBack(new ActionEvent()));
    }
}