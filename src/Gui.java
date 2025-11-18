import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.LinkedList;

import com.formdev.flatlaf.FlatLightLaf;

public class Gui extends JFrame {
    private final Pmt PMT;

    public Gui(Pmt pPmt) {
        PMT = pPmt;

        // Setup FlatLaf
        FlatLightLaf.setup();
        com.formdev.flatlaf.FlatLightLaf.updateUI();

        this.setTitle("Physical Media Tracker");
        this.setSize(500, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // JButton for adding new album
        JButton bAddAlbum = new JButton("Album hinzufügen");
        bAddAlbum.addActionListener(_ -> {
            openAddAlbumFrame();
        });
        this.add(bAddAlbum, BorderLayout.SOUTH);

        // JScrollPane for scrollable album display
        JScrollPane scrollPane = new JScrollPane();

        // JPanel with albums
        JPanel panAlbums = new JPanel(new GridLayout(3, 5));

        // DEBUG
        panAlbums.add(new AlbumComponent("media\\albumCovers\\test1.jpg", "DUNKEL", "Die Ärzte"));
        panAlbums.add(new AlbumComponent("media\\albumCovers\\test2.jpg", "Three Cheers for Sweet Revenge", "My Chemical Romance"));
        panAlbums.add(new AlbumComponent("media\\albumCovers\\test3.jpg", "Does This Look Infected?", "Sum 41"));
        panAlbums.add(new AlbumComponent("media\\albumCovers\\test4.jpg", "Appeal To Reason", "Rise Against"));
        panAlbums.add(new AlbumComponent("media\\albumCovers\\test5.jpg", "TEKKNO", "Electric Callboy"));
        panAlbums.add(new AlbumComponent("media\\albumCovers\\test6.jpg", "Angelika Express", "Angelika Express"));
        panAlbums.add(new AlbumComponent("media\\albumCovers\\test7.jpg", "Phoenix", "zebrahead"));

        for(Component c : panAlbums.getComponents()) {
            c.setPreferredSize(new Dimension(150, 160));
        }

        scrollPane.setViewportView(panAlbums);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /**
     * Opens the frame where the user can add a new album to the list
     */
    private void openAddAlbumFrame() {
        JFrame fNewAlbum = new JFrame();
        fNewAlbum.setTitle("Neues Album");
        fNewAlbum.setSize(500, 500);
        fNewAlbum.setLocationRelativeTo(null);
        fNewAlbum.setLayout(new BorderLayout());

        // Main panel where user inserts all information
        JPanel panMain = new JPanel();
        panMain.setLayout(new GridBagLayout());
        panMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Upper panel for album cover, name, artist & release
        JPanel panUpper = new JPanel();
        panUpper.setLayout(new GridBagLayout());

        // JLabel where cover can be uploaded
        JLabel lCover = new JLabel("Cover hinzufügen", SwingConstants.CENTER);
        lCover.setOpaque(true);
        lCover.setBackground(new Color(200, 200, 200));
        lCover.setForeground(new Color(150, 150, 150));
        lCover.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        // TODO: Add MouseListener
        panUpper.add(lCover, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.3;
            weighty = 1;
            gridheight = 3;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }});

        // Album name
        PlaceholderTextField tfName = new PlaceholderTextField("Name");
        panUpper.add(tfName, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Album artist
        PlaceholderTextField tfArtist = new PlaceholderTextField("Künstler");
        panUpper.add(tfArtist, new GridBagConstraints() {{
            gridx = 1;
            gridy = 1;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Release Year
        PlaceholderTextField tfRelease = new PlaceholderTextField("Erscheinungsjahr");
        panUpper.add(tfRelease, new GridBagConstraints() {{
            gridx = 1;
            gridy = 2;
            weightx = 0.7;
            insets = new Insets(0, 5, 0, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Lower panel for track list
        JPanel panLower = new JPanel();
        panLower.setLayout(new GridBagLayout());

        // Track List
        JPanel panTracks = new JPanel();
        panTracks.setLayout(new GridBagLayout());
        int[] latestIndex = {1}; // Must be array to be changable in ActionListener class

        JCheckBox cbNulltrack = new JCheckBox("Beinhaltet Nulltrack"); // If checked, index will start at 0
        cbNulltrack.addActionListener(_ -> {
            // TODO
        });
        panLower.add(cbNulltrack, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            gridwidth = 2;
            weightx = 1;
            anchor = GridBagConstraints.WEST;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        LinkedList<Track> llTracks = new LinkedList<>();
        llTracks.add(new Track("", null, 1));

        GridBagConstraints gbcIndex = new GridBagConstraints();
        gbcIndex.gridx = 0;
        gbcIndex.gridy = 1;
        gbcIndex.weightx = 0.1;
        gbcIndex.insets = new Insets(0, 0, 5, 0);
        gbcIndex.anchor = GridBagConstraints.CENTER;
        gbcIndex.fill = GridBagConstraints.NONE;

        GridBagConstraints gbcTextField = new GridBagConstraints();
        gbcTextField.gridx = 1;
        gbcTextField.gridy = 1;
        gbcTextField.weightx = 0.9;
        gbcTextField.insets = new Insets(0, 0, 5, 5);
        gbcTextField.anchor = GridBagConstraints.CENTER;
        gbcTextField.fill = GridBagConstraints.HORIZONTAL;

        panTracks.add(new JLabel(llTracks.get(latestIndex[0]-1).getTrackNumber() + ""), gbcIndex);
        gbcIndex.gridy++;

        panTracks.add(new JTextField(), gbcTextField);
        gbcTextField.gridy++;

        // Add track panel to JScrollPane
        JScrollPane spTracks = new JScrollPane(panTracks);

        // Add JScrollPane to main pane
        panLower.add(spTracks, new GridBagConstraints() {{
            gridx = 0;
            gridy = 3;
            gridwidth = 2;
            gridheight = 3;
            weightx = 1.0;
            weighty = 1.0;
            insets = new Insets(5, 0, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }});

        // Button to add tracks at the bottom of main panel
        JButton bAddTrack = new JButton("Track hinzufügen");
        bAddTrack.addActionListener(_ -> {
            latestIndex[0]++;

            panTracks.add(new JLabel(latestIndex[0] + ""), gbcIndex);
            gbcIndex.gridy++;

            panTracks.add(new JTextField(), gbcTextField);
            gbcTextField.gridy++;

            fNewAlbum.revalidate();
        });
        panLower.add(bAddTrack, new GridBagConstraints() {{
            gridx = 0;
            gridy = 6;
            gridwidth = 2;
            weightx = 1;
            insets = new Insets(5, 0, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        // Add panels to main panel
        panMain.add(panUpper, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 0.4;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }});
        panMain.add(panLower, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            weighty = 0.6;
            gridheight = 2;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }});

        // Add main panel to frame
        fNewAlbum.add(panMain, BorderLayout.CENTER);

        // Confirm & Return buttons
        JPanel panButtons = new JPanel();
        panButtons.setLayout(new GridBagLayout());

        JButton bReturn = new JButton("Abbrechen");
        bReturn.addActionListener(_ -> {
            fNewAlbum.dispose();
        });
        panButtons.add(bReturn, new GridBagConstraints() {{
            gridy = 0;
            gridx = 0;
            weightx = 0.1;
            anchor = GridBagConstraints.EAST;
            fill = GridBagConstraints.NONE;
        }});

        JButton bConfirm = new JButton("Bestätigen");
        bConfirm.addActionListener(_ -> {
            // TODO
        });
        panButtons.add(bConfirm, new GridBagConstraints() {{
            gridy = 0;
            gridx = 1;
            weightx = 0.1;
            anchor = GridBagConstraints.WEST;
            fill = GridBagConstraints.NONE;
        }});

        fNewAlbum.add(panButtons, BorderLayout.SOUTH);

        fNewAlbum.setVisible(true);
    }
}

class AlbumComponent extends JPanel {
    private String imgPath;
    private String name;
    private String artist;

    public AlbumComponent() {
        // Empty constructor
    }

    public AlbumComponent(String pImgPath, String pName, String pArtist) {
        imgPath = pImgPath;
        name = pName;
        artist = pArtist;

        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        // Cover
        ImageIcon icon = new ImageIcon(imgPath);
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel lImg = new JLabel(new ImageIcon(img));
        this.add(lImg, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.VERTICAL;
        }});

        // Name
        JLabel lName = new JLabel(name);
        lName.setBackground(new Color(200, 200, 200));
        lName.setOpaque(true);
        lName.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lName, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Artist
        JLabel lArtist = new JLabel(artist);
        lArtist.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lArtist, new GridBagConstraints() {{
            gridx = 0;
            gridy = 2;
            weightx = 1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});
    }
}

