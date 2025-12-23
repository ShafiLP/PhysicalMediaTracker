import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class SessionPanel extends JPanel {
    private int idx;
    private final JLabel idxLabel;
    private final PlaceholderTextField date;
    private final PlaceholderTextField time;
    private final JButton delete;

    public SessionPanel(Session session, int idx, Settings settings) {
        // Index label
        this.idx = idx;
        idxLabel = new JLabel(String.valueOf(idx + 1), SwingConstants.CENTER);
        idxLabel.setFont(settings.getFont());

        // Date text field
        date = new PlaceholderTextField("DD.MM.YYYY");
        date.setText(session.getDate());
        date.setFont(settings.getFont());

        // Time text field
        time = new PlaceholderTextField("HH:MM");
        time.setText(session.getTime());
        time.setFont(settings.getFont());

        // Delete button
        ImageIcon icon = new ImageIcon("media\\icons\\delete.png");
        Image img = icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        delete = new JButton(new ImageIcon(img));
        delete.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        delete.setPreferredSize(new Dimension(20,20));

        // Add components to panel
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.1;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }};
        this.add(idxLabel, gbc);
        gbc.gridx++;
        gbc.weightx = 0.4;
        this.add(date, gbc);
        gbc.gridx++;
        gbc.weightx = 0.4;
        this.add(time, gbc);
        gbc.gridx++;
        gbc.weightx = 0.1;
        this.add(delete, gbc);

        // Contrast row
        if (settings.getRowContrast() & idx % 2 == 1) this.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
    }

    public static JPanel initializeHeaderPanel(Settings settings) {
        JPanel panHead = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weighty = 0.1;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }};

        // Head row
        panHead.add(new JLabel("Nr.", SwingConstants.CENTER) {{
            setPreferredSize(new Dimension(20, getPreferredSize().height));
        }}, gbc);
        gbc.gridx++;
        gbc.weightx = 0.4;
        panHead.add(new JLabel("Datum", SwingConstants.CENTER), gbc);
        gbc.gridx++;
        gbc.weightx = 0.4;
        panHead.add(new JLabel("Zeit", SwingConstants.CENTER), gbc);
        gbc.gridx++;
        gbc.weightx = 0.1;
        JLabel lDel = new JLabel("Löschen", SwingConstants.CENTER);
        panHead.add(lDel, gbc);

        if (settings.getRowContrast()) panHead.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        return panHead;
    }

    public void setIndex(int idx) {
        this.idx = idx;
        idxLabel.setText(String.valueOf(idx));
    }

    public PlaceholderTextField getDateTextField() {
        return date;
    }

    public PlaceholderTextField getTimeTextField() {
        return time;
    }

    public JButton getDeleteButton() {
        return delete;
    }
}
