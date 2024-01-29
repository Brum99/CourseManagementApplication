package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;

public class SettingsController {
    @FXML
    private ComboBox<String> fontSizeComboBox;

    @FXML
    private ColorPicker textColorPicker;

    @FXML
    private Button applyButton;

    private String initialFontSize;
    private String initialTextColor;

    private PreferencesChangedListener preferencesChangedListener;

    public void initialize() {
        // Initialize the font size and text color options in the ComboBox
        fontSizeComboBox.getItems().addAll("small-text", "medium-text", "large-text");

        // Set the initial values for font size and text color
        fontSizeComboBox.setValue(initialFontSize);
        textColorPicker.setValue(javafx.scene.paint.Color.web(initialTextColor));

        // Apply the selected preferences when the Apply button is clicked
        applyButton.setOnAction(event -> applyPreferences());
    }

    public void setInitialPreferences(String fontSize, String textColor) {
        initialFontSize = fontSize;
        initialTextColor = textColor;
    }

    public void setOnPreferencesChanged(PreferencesChangedListener listener) {
        preferencesChangedListener = listener;
    }

    private void applyPreferences() {
        String selectedFontSize = fontSizeComboBox.getValue();
        String selectedTextColor = textColorPicker.getValue().toString();

        if (preferencesChangedListener != null) {
            preferencesChangedListener.onPreferencesChanged(selectedFontSize, selectedTextColor);
        }
    }

    public interface PreferencesChangedListener {
        void onPreferencesChanged(String fontSize, String textColor);
    }
}