import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProfileSelectionFrame extends JFrame {
    private JFrame parent;
    private final Pmt PMT;

    /**
     * Constructor of ProfileSelectionFrame class
     * Opens a frame for loading and configuring profiles
     * @param parent Parent component
     * @param PMT Control class
     * @param settings Settings object
     */
    public ProfileSelectionFrame(JFrame parent, Pmt PMT, Settings settings) {
        this.parent = parent;
        this.PMT = PMT;

        // Frame settings
        this.setTitle("Profile");
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        parent.setEnabled(false);

        // Get all files from saveData directory
        File folder = new File("data\\saveData");
        File[] files = folder.listFiles();
        String[] pathNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            pathNames[i] = files[i].getName();
        }

        // Create label element for each file
        JPanel panProfiles = new JPanel(new GridBagLayout()); // Panel for all profiles
        JList<String> listProfiles = new JList<>(pathNames);
        listProfiles.setFont(settings.getFont());
        JScrollPane spProfiles = new JScrollPane(listProfiles); // Put in ScrollPane
        panProfiles.add(spProfiles);

        // Wrap panProfiles so its content stays top-aligned inside the scroll pane
        JPanel panProfilesWrapper = new JPanel(new BorderLayout());
        panProfilesWrapper.add(panProfiles, BorderLayout.NORTH);

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
        bLoad.setPreferredSize(preferredSize);
        bLoad.addActionListener(_ -> {
            PMT.setDataPath("data/saveData/" + listProfiles.getSelectedValue());
            Log.info("loaded " + settings.getDataPath());
            this.dispose();
        });
        panButtons.add(bLoad, gbcButtons);
        gbcButtons.gridx++;

        JButton bEdit = new JButton("Umbenennen");
        bEdit.setPreferredSize(preferredSize);
        bEdit.addActionListener(_ -> {
            // TODO
        });
        panButtons.add(bEdit, gbcButtons);
        gbcButtons.gridx++;

        JButton bDel = new JButton("Löschen");
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
        bBack.setPreferredSize(preferredSize);
        bBack.addActionListener(_ -> {
            this.dispose();
        });
        panButtons.add(bBack, gbcButtons);
        gbcButtons.gridx++;

        // Add buttons to frame
        this.add(panButtons, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        parent.setEnabled(true);
        parent.requestFocus();
    }
}
