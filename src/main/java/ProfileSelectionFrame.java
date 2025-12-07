import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class ProfileSelectionFrame extends JFrame {
    public ProfileSelectionFrame(Settings settings) {
        // Frame settings
        this.setTitle("Profile");
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Get all files from saveData directory
        File folder = new File("data\\saveData");
        File[] files = folder.listFiles();

        // Popup Menu, opens when clicking on AlbumComponent
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuLoad = new JMenuItem("Öffnen");
        JMenuItem menuEdit = new JMenuItem("Umbenennen");
        JMenuItem menuDelete = new JMenuItem("Löschen");

        menuLoad.addActionListener(_ -> {
            // TODO
        });
        menuEdit.addActionListener(_ -> {
            // TODO
        });
        menuDelete.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    "Wirklich löschen?",
                    "Profil löschen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (n == 0) {
                // TODO delete
            } else {
                System.out.println("Deletion cancelled"); // DEBUG
            }
        });

        popupMenu.add(menuLoad);
        popupMenu.add(menuEdit);
        popupMenu.add(menuDelete);

        // Create label element for each file
        JPanel panProfiles = new JPanel(new GridBagLayout()); // Panel for all profiles
        for (int i = 0; i < files.length; i++) {
            JLabel lProfile =  new JLabel(files[i].getName());
            lProfile.setFont(settings.getFont());
            lProfile.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
            lProfile.addMouseListener(new MouseAdapter() {
                // Show menu when clicked
                @Override
                public void mouseClicked(MouseEvent e) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }

                // Change background colour when hovering
                @Override
                public void mouseEntered(MouseEvent e) {
                    lProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    lProfile.setOpaque(true);
                    lProfile.repaint();
                }

                // Change background color to normal when hover exit
                @Override
                public void mouseExited(MouseEvent e) {
                    lProfile.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    lProfile.setOpaque(false);
                    lProfile.repaint();
                }
            });
            int finalI = i;
            panProfiles.add(lProfile, new GridBagConstraints() {{
                gridx = 0;
                gridy = finalI;
                weightx = 1.0;
                weighty = 1.0;
                fill = GridBagConstraints.HORIZONTAL;
                anchor = GridBagConstraints.CENTER;
            }});
        }

        // Wrap panProfiles so its content stays top-aligned inside the scroll pane
        JPanel panProfilesWrapper = new JPanel(new BorderLayout());
        panProfilesWrapper.add(panProfiles, BorderLayout.NORTH);

        // Add panel to scroll pane (use wrapper as viewport)
        JScrollPane spProfiles = new JScrollPane(panProfilesWrapper);
        spProfiles.getVerticalScrollBar().setUnitIncrement(16);

        // Add scroll pane to frame
        this.add(spProfiles, BorderLayout.CENTER);

        this.setVisible(true);
    }
}
