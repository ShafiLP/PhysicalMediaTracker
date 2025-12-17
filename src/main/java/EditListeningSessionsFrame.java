import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class EditListeningSessionsFrame extends JFrame {
    private final JFrame PARENT;

    public EditListeningSessionsFrame(Settings settings, Pmt pmt, JFrame parent, Album album) {
        this.PARENT = parent;

        this.setTitle(album.getAlbumName() + " Sessions");
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PARENT.setEnabled(false);

        JPanel panSessions = new JPanel(new GridBagLayout());
        GridBagConstraints gbcIdx = new  GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.3;
            weighty = 1.0;
            fill = GridBagConstraints.NONE;
            anchor = GridBagConstraints.CENTER;
        }};
        GridBagConstraints gbcDate = new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }};
        GridBagConstraints gbcTime = new GridBagConstraints() {{
            gridx = 2;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }};
        GridBagConstraints gbcDel = new GridBagConstraints() {{
            gridx = 3;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }};

        // Head row
        JPanel panHeadRow  = new JPanel(new GridBagLayout());
        panHeadRow.add(new JLabel("Nr.", SwingConstants.CENTER) {{
            setPreferredSize(new Dimension(20, getPreferredSize().height));
        }}, gbcIdx);
        panHeadRow.add(new JLabel("Datum", SwingConstants.CENTER), gbcDate);
        panHeadRow.add(new JLabel("Zeit", SwingConstants.CENTER), gbcTime);
        JLabel lDel = new JLabel("Löschen", SwingConstants.CENTER);
        panHeadRow.add(lDel, gbcDel);
        if (settings.getRowContrast()) panHeadRow.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        panSessions.add(panHeadRow, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }});

        // Add all sessions to frame
        LinkedList<PlaceholderTextField> llDate = new LinkedList<>();
        LinkedList<PlaceholderTextField> llTime = new LinkedList<>();
        LinkedList<JButton> llDelete = new LinkedList<>();
        for (int i = 0; i < album.getSessions().size(); i++) {
            int finalI = i;
            JPanel panRow = new JPanel(new GridBagLayout());

            // Index
            panRow.add(new JLabel(String.valueOf(finalI + 1), SwingConstants.CENTER) {{
                if ((finalI + 1) % 2 == 0 & settings.getRowContrast()) setOpaque(false);
                setPreferredSize(new Dimension(20, getPreferredSize().height));
            }}, gbcIdx);

            // Date text field
            llDate.addLast(new PlaceholderTextField("DD.MM.YYYY") {{
                setText(album.getSessions().get(finalI).getDate());
            }});
            panRow.add(llDate.getLast(), gbcDate);

            // Time text field
            llTime.addLast(new PlaceholderTextField("HH:MM") {{
                setText(album.getSessions().get(finalI).getTime());
            }});
            panRow.add(llTime.getLast(), gbcTime);

            // Delete button
            ImageIcon icon = new ImageIcon("media\\icons\\delete.png");
            Image img = icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            llDelete.addLast(new JButton(new ImageIcon(img)));
            llDelete.getLast().putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
            llDelete.getLast().addActionListener(_ -> {
                album.removeSession(finalI);
                pmt.editAlbum(album, album);
            });
            panRow.add(llDelete.getLast(), gbcDel);

            // Contrast row
            if (settings.getRowContrast()) panRow.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

            panSessions.add(panRow, new GridBagConstraints() {{
                gridx = 0;
                gridy = finalI + 1;
                weightx = 1.0;
                weighty = 1.0;
                fill = GridBagConstraints.HORIZONTAL;
                anchor = GridBagConstraints.CENTER;
            }});
        }

        // Wrap panProfiles so its content stays top-aligned inside the scroll pane
        JPanel panSessionsWrapper = new JPanel(new BorderLayout());
        panSessionsWrapper.add(panSessions, BorderLayout.NORTH);

        // Add panel to scroll pane (use wrapper as viewport)
        JScrollPane spSessions = new JScrollPane(panSessionsWrapper);
        spSessions.getVerticalScrollBar().setUnitIncrement(16);

        this.add(spSessions, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
