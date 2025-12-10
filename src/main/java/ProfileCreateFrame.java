import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class ProfileCreateFrame extends JFrame {
    private final JFrame PARENT;

    public ProfileCreateFrame(JFrame PARENT, Pmt pmt, Settings settings) {
        this.PARENT = PARENT;

        // Frame settings
        this.setTitle("Neues Profil");
        this.setSize(350, 150);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        PARENT.setEnabled(false);

        // Instruction label
        JPanel panCenter = new JPanel(new GridBagLayout());
        JLabel lIns = new JLabel("Geben Sie dem neuen Profil einen Namen!", SwingConstants.CENTER);
        lIns.setFont(settings.getFont());
        panCenter.add(lIns, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.SOUTH;
        }});

        // Text field for new profile's name
        PlaceholderTextField tfName = new PlaceholderTextField("Name");
        tfName.setPreferredSize(new Dimension(200, 30));
        tfName.setFont(settings.getFont());
        panCenter.add(tfName, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.NONE;
            anchor = GridBagConstraints.CENTER;
        }});
        this.add(panCenter, BorderLayout.CENTER);

        // Confirm & Return buttons
        JPanel panButtons = new JPanel();
        panButtons.setLayout(new GridBagLayout());

        JButton bReturn = new JButton("X");
        bReturn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bReturn.addActionListener(_ -> {
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
        bConfirm.addActionListener(_ -> {
            pmt.createProfile(tfName.getText());
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

        this.add(panButtons, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
