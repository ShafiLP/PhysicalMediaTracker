import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class GeneralSettingsFrame extends JFrame {
    private final Settings settings;

    public GeneralSettingsFrame(Gui gui, Settings settings) {
        this.settings = settings;

        // JFrame settings
        this.setTitle("Einstellungen");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(gui);
        this.setSize(400, 150);
        this.setLayout(new BorderLayout());

        // Settings panel containing all settings
        JPanel panSettings = new JPanel(new GridLayout(2, 2));

        // Auto-save settings
        JCheckBox cbAutosave = new JCheckBox("Automatisch speichern", settings.getAutoSave());
        cbAutosave.setFont(settings.getFont());
        panSettings.add(cbAutosave);

        // Resolution for uploaded album covers
        JPanel panResolution = new JPanel(new GridLayout(2, 1) {{
            setFont(settings.getFont());
        }});
        panResolution.add(new JLabel("Cover-Auflösung"));
        JTextField tfCoverResolution = new JTextField(String.valueOf(settings.getCoverResolution()));
        tfCoverResolution.setFont(settings.getFont());
        panResolution.add(tfCoverResolution);
        panSettings.add(panResolution);

        // Apply and cancel buttons
        JPanel panButtons = new JPanel();
        panButtons.setLayout(new GridBagLayout());
        JButton bReturn = new JButton("X");
        bReturn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bReturn.addActionListener(e -> {
            this.dispose();
        });
        panButtons.add(bReturn, new GridBagConstraints() {{
            gridy = 0;
            gridx = 0;
            weightx = 0.1;
            insets = new Insets(0, 0, 5, 5);
            anchor = GridBagConstraints.EAST;
            fill = GridBagConstraints.NONE;
        }});

        JButton bConfirm = new JButton("✓");
        bConfirm.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bConfirm.addActionListener(e -> {
            // Save settings
            settings.setAutoSave(cbAutosave.isSelected());
            try {
                settings.setCoverResolution(Integer.parseInt(tfCoverResolution.getText()));
            }  catch (NumberFormatException ex) {
                Log.error(ex.getMessage());
                // Keep current settings
            }
            Settings.writeSettings(settings); // * Save settings to JSON
            this.dispose();
        });
        panButtons.add(bConfirm, new GridBagConstraints() {{
            gridy = 0;
            gridx = 1;
            weightx = 0.1;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.WEST;
            fill = GridBagConstraints.NONE;
        }});

        // Make JFrame visible
        this.add(panSettings, BorderLayout.CENTER);
        this.add(panButtons, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    /**
     * Changes the accent colour of all FlatLaf elements
     * @param pColor new accent colour
     */
    private void applyAccentColor(Color pColor) {
        String hex = String.format("#%02x%02x%02x%02x",  pColor.getRed(), pColor.getGreen(), pColor.getBlue(),  pColor.getAlpha());
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", hex));
        if (!settings.isDarkmode()) {
            FlatLightLaf.setup();
            FlatLaf.updateUI();
        } else {
            FlatDarkLaf.setup();
            FlatDarkLaf.updateUI();
        }

    }
}
