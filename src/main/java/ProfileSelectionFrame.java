import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProfileSelectionFrame extends JFrame {
    private final JFrame PARENT;

    /**
     * Constructor of ProfileSelectionFrame class.
     * Opens a frame for loading and configuring profiles.
     * @param PARENT PARENT component.
     * @param settings Settings object.
     */
    public ProfileSelectionFrame(JFrame PARENT, Pmt pmt, Settings settings) {
        this.PARENT = PARENT;

        // Frame settings
        this.setTitle("Profile");
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        PARENT.setEnabled(false);

        // Get all files from saveData directory
        File folder = new File("data\\saveData");
        File[] files = folder.listFiles();
        if (files != null) {
            Log.error("No files in folder \"data\\saveData\".");
            return;
        }
        String[] pathNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            pathNames[i] = files[i].getName();
        }

        // Create label element for each file
        JPanel panProfiles = new JPanel(new GridBagLayout()); // Panel for all profiles
        JList<String> listProfiles = new JList<>(pathNames);
        listProfiles.setFont(settings.getFont());
        JScrollPane spProfiles = new JScrollPane(listProfiles); // Put in ScrollPane
        panProfiles.add(spProfiles, new GridBagConstraints() {{
            weightx = 1.0;
            weighty = 1.0;
            insets = new Insets(5, 5, 5, 5);
            fill = GridBagConstraints.BOTH;
            anchor = GridBagConstraints.CENTER;
        }});

        // Add panel to frame
        this.add(panProfiles, BorderLayout.CENTER);

        // Buttons
        JPanel panButtons = new  JPanel(new GridBagLayout());
        GridBagConstraints gbcButtons = new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.0;
            weighty = 0.0;
            fill =  GridBagConstraints.NONE;
            anchor = GridBagConstraints.CENTER;
        }};
        Dimension preferredSize = new Dimension(200, 20);

        JButton bLoad = new JButton("Laden");
        bLoad.setFont(settings.getFont());
        bLoad.setPreferredSize(preferredSize);
        bLoad.addActionListener(_ -> {
            pmt.setDataPath("data/saveData/" + listProfiles.getSelectedValue());
            Log.info("Loaded " + settings.getDataPath());
            this.dispose();
        });
        panButtons.add(bLoad, gbcButtons);
        gbcButtons.gridx++;

        JButton bEdit = new JButton("Umbenennen");
        bEdit.setFont(settings.getFont());
        bEdit.setPreferredSize(preferredSize);
        bEdit.addActionListener(_ -> {
            // TODO
        });
        panButtons.add(bEdit, gbcButtons);
        gbcButtons.gridx++;

        JButton bDel = new JButton("Löschen");
        bDel.setFont(settings.getFont());
        bDel.setPreferredSize(preferredSize);
        bDel.addActionListener(_ -> {
            try {
                Files.deleteIfExists(Paths.get("data/saveData/" + listProfiles.getSelectedValue()));
                Log.info("Deleted profile.");
            } catch (RuntimeException | IOException e) {
                Log.error(e.getMessage());
            }
            listProfiles.remove(listProfiles.getSelectedIndex());
        });
        panButtons.add(bDel, gbcButtons);
        gbcButtons.gridx++;

        JButton bBack = new JButton("Zurück");
        bBack.setFont(settings.getFont());
        bBack.setPreferredSize(preferredSize);
        bBack.addActionListener(_ -> {
            this.dispose();
        });
        panButtons.add(bBack, gbcButtons);
        gbcButtons.gridx++;

        // Add buttons to frame
        this.add(panButtons, BorderLayout.SOUTH);

        this.setVisible(true);
        Log.info("Opened Profile Selection Frame");
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
