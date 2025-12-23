import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class EditListeningSessionsFrame extends JFrame {
    private final JFrame PARENT;

    /**
     * Constructor for EditListeningSessionFrame.
     * In this frame user can edit the date and time of sessions or delete them.
     * @param settings Settings object.
     * @param pmt PMT control class object.
     * @param parent Parent component.
     * @param album Album object containing session data.
     */
    public EditListeningSessionsFrame(Settings settings, Pmt pmt, JFrame parent, Album album) {
        this.PARENT = parent;

        // Frame settings
        this.setTitle(album.getAlbumName() + " Sessions");
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        PARENT.setEnabled(false);

        // Sessions panel with all sessions
        JPanel panSessions = new JPanel(new GridBagLayout());

        panSessions.add(SessionPanel.initializeHeaderPanel(settings), new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx =  1.0;
            insets = new Insets(2, 2, 2, 2);
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }});

        // Add all sessions to frame
        LinkedList<SessionPanel> llSessions = new LinkedList<>();
        for (int i = 0; i < album.getSessions().size(); i++) {
            int finalI = i;
            llSessions.addLast(new SessionPanel(album.getSessions().get(i), i, settings));
            llSessions.getLast().getDeleteButton().addActionListener(_ -> {
                album.removeSession(finalI);
                pmt.editAlbum(album, album);

                // Remove from GUI
                for (int j = finalI; j < llSessions.size() - 1; j++) {
                    llSessions.set(finalI, llSessions.get(finalI + 1));
                }
                llSessions.getLast().setVisible(false);
                llSessions.removeLast();
                Log.info("Removed Session " + finalI + " from GUI");
            });

            panSessions.add(llSessions.getLast(), new GridBagConstraints() {{
                gridx = 0;
                gridy = finalI + 1;
                weightx =  1.0;
                insets = new Insets(2, 2, 2, 2);
                fill = GridBagConstraints.HORIZONTAL;
                anchor = GridBagConstraints.CENTER;
            }});

            // Contrast row
            if (settings.getRowContrast()) this.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        }

        // Wrap panProfiles so its content stays top-aligned inside the scroll pane
        JPanel panSessionsWrapper = new JPanel(new BorderLayout());
        panSessionsWrapper.add(panSessions, BorderLayout.NORTH);

        // Add panel to scroll pane (use wrapper as viewport)
        JScrollPane spSessions = new JScrollPane(panSessionsWrapper);
        spSessions.getVerticalScrollBar().setUnitIncrement(16);

        this.add(spSessions, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.BOTH;
            anchor = GridBagConstraints.CENTER;
        }});

        // Add confirm and discard buttons
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
            // Save changes if made
            LinkedList<Session> llSessionsEdit = new  LinkedList<>();
            for (int i = 0; i <  llSessions.size(); i++) {
                llSessionsEdit.addLast(new Session(album.getSessions().get(i).getListenedTracks(), new DateTime(llSessions.get(i).getDateTextField().getText(), llSessions.get(i).getTimeTextField().getText())));
            }
            album.setSessions(llSessionsEdit);
            pmt.editAlbum(album, album);
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

        this.add(panButtons, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            weighty = 1.0;
            fill = GridBagConstraints.CENTER;
            anchor = GridBagConstraints.SOUTH;
        }});

        this.setVisible(true);
    }

    /**
     * Override dispose method to focus on parent frame when disposing.
     */
    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
