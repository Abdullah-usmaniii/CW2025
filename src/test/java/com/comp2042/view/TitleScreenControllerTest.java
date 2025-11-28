package com.comp2042.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class TitleScreenControllerTest {

    private TitleScreenController controller;
    private VBox mockSettingsPanel;
    private Slider mockVolumeSlider;

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new TitleScreenController();

        // Inject FXML elements
        mockSettingsPanel = new VBox();
        mockSettingsPanel.setVisible(false);
        mockSettingsPanel.setManaged(false);

        mockVolumeSlider = new Slider();

        // Reflection to set private fields
        setPrivateField(controller, "settingsPanel", mockSettingsPanel);
        setPrivateField(controller, "volumeSlider", mockVolumeSlider);
    }

    // Helper method for reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testHandleSettingsTogglesVisibility() {
        // Initial state
        assertFalse(mockSettingsPanel.isVisible());
        assertFalse(mockSettingsPanel.isManaged());

        // First Click -> Show
        controller.handleSettings(new ActionEvent());
        assertTrue(mockSettingsPanel.isVisible(), "Settings should be visible after click");
        assertTrue(mockSettingsPanel.isManaged(), "Settings should be managed after click");

        // Second Click -> Hide
        controller.handleSettings(new ActionEvent());
        assertFalse(mockSettingsPanel.isVisible(), "Settings should be hidden after second click");
    }

    @Test
    void testInitializeSetsSliderValue() {
        // Pre-set SoundManager volume to a known value
        com.comp2042.Logic.SoundManager.getInstance().setVolume(0.3);

        // Run initialize
        controller.initialize();

        assertEquals(0.3, mockVolumeSlider.getValue(), 0.001, "Slider should pick up current global volume");
    }
}