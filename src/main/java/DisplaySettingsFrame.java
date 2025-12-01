import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class DisplaySettingsFrame extends JFrame {
    private Settings settings;

    public DisplaySettingsFrame(Gui gui, Settings settings) {
        this.settings = settings;

        // JFrame settings
        this.setTitle("Einstellungen");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 200);
        this.setLayout(new BorderLayout());

        // Settings panel containing all settings
        JPanel panSettings = new JPanel(new GridLayout(3, 2));

        // Darkmode settings
        JCheckBox cbDarkmode = new JCheckBox("Dunkler Modus", settings.isDarkmode());
        panSettings.add(cbDarkmode);

        // Accent colour settings
        JPanel panAccentColor = new JPanel(new GridLayout(2, 1));
        panAccentColor.add(new JLabel("Akzentfarbe"));
        JPanel panColors  = new JPanel(new GridBagLayout());
        JButton[] bColors = {
                new JButton() {{
                    setBackground(new Color(108, 108, 255));
                }},
                new JButton() {{
                    setBackground(new Color(255, 108, 108));
                }},
                new JButton() {{
                    setBackground(new Color(113, 255, 108));
                }},
                new JButton() {{
                    setBackground(new Color(255, 248, 108));
                }},
                new JButton() {{
                    setBackground(new Color(189, 108, 255));
                }}
        };
        for (int i = 0; i < bColors.length; i++) {
            bColors[i].putClientProperty("JButton.buttonType", "roundRect");
            bColors[i].setPreferredSize(new Dimension(20, 20));
            final int idx = i;
            bColors[i].addActionListener(e -> {
                applyAccentColor(bColors[idx].getBackground());
                System.out.println("Changed accent color."); // DEBUG
            });
            panColors.add(bColors[i], new GridBagConstraints() {{
                gridx = idx;
                gridy = 0;
                fill = GridBagConstraints.NONE;
                anchor = GridBagConstraints.WEST;
            }});
        }
        JPanel panColorsWrapped = new JPanel(new BorderLayout());
        panColorsWrapped.add(panColors, BorderLayout.WEST);
        panAccentColor.add(panColorsWrapped);
        panSettings.add(panAccentColor);

        // Font type settings
        JPanel panFontType = new JPanel(new GridLayout(2, 1));
        panFontType.add(new JLabel("Schriftart"));
        JComboBox<String> cbFontType = new JComboBox<>();
        cbFontType.addItem("Arial");
        cbFontType.addItem("Comic Sans MS");
        // TODO load all system fonts
        panFontType.add(cbFontType);
        panSettings.add(panFontType);

        // Font size settings
        JPanel panFontSize = new JPanel(new GridLayout(2, 1));
        panFontSize.add(new JLabel("Schriftgröße"));
        JTextField tfFontSize = new JTextField();
        // TODO replace with bar
        panFontSize.add(tfFontSize);
        panSettings.add(panFontSize);

        // AlbumComponent size settings
        JPanel panAlbumSize = new JPanel(new GridLayout(2, 1));
        panAlbumSize.add(new JLabel("Größe der Albumkomponenten"));
        JTextField tfAlbumSize = new JTextField();
        // TODO replace with bar
        panAlbumSize.add(tfAlbumSize);
        panSettings.add(panAlbumSize);

        // Row contrast settings
        JCheckBox cbRowContrast = new JCheckBox("Kontrast zwischen Reihen");
        panSettings.add(cbRowContrast);

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
            // * Apply settings
            // Darkmode
            if (cbDarkmode.isSelected()) {
                FlatDarkLaf.setup();
                FlatLaf.updateUI();
                gui.applyDarkmode();
            }
            settings.setDarkmode(cbDarkmode.isSelected());
            Settings.writeSettings(settings);
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
