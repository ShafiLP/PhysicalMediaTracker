import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import com.formdev.flatlaf.FlatClientProperties;

public class AlbumCreateFrame extends JFrame implements CoverSearcher {
    private Settings settings;
    private JButton bCover;
    private Image[] albumCover = {null}; // Array so local object can be accessed in action listener
    private int[] latestIndex = {1}; // Must be array to be changable in ActionListener class
    private LinkedList<TrackEntry> llTracks = new LinkedList<>();
    private JLabel lDelete;
    private JPanel panTracks;
    private JCheckBox cbNulltrack;
    private Gui gui;

    /**
     * Opens a JFrame where a new Album object can be created
     * @param pPmt Object of control class
     */
    public AlbumCreateFrame(Gui gui, Pmt pPmt, Settings settings) {
        this.settings = settings;
        this.gui = gui;
        gui.setEnabled(false);

        // Frame settings
        this.setTitle("Neues Album");
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel where user inserts all information
        JPanel panMain = new JPanel();
        panMain.setLayout(new GridBagLayout());
        panMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Upper panel for album cover, name, artist & release
        JPanel panUpper = new JPanel();
        panUpper.setLayout(new GridBagLayout());

        // JButton where cover can be uploaded
        bCover = new JButton("Cover hinzufügen");
        bCover.setOpaque(true);
        bCover.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        bCover.setForeground(new Color(150, 150, 150));
        bCover.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        bCover.setPreferredSize(new Dimension(200, 200));
        bCover.setMinimumSize(new Dimension(200, 200));
        bCover.setMaximumSize(new Dimension(200, 200));
        bCover.addActionListener(e -> {
            albumCover[0] = iconFromUpload(bCover.getWidth(), bCover.getHeight());
            if(albumCover[0] != null) {
                bCover.setIcon(new ImageIcon(albumCover[0]));
                bCover.setText("");
            }
        });
        panUpper.add(bCover, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            weighty = 1.0;
            gridheight = 8;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
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
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Type of media
        JLabel lTypeOfMedia = new JLabel("Tonträger:");
        JCheckBox cbVinyl = new JCheckBox("Vinyl");
        JCheckBox cbCd = new JCheckBox("CD");
        JCheckBox cbCassette = new JCheckBox("Kassette");

        panUpper.add(lTypeOfMedia, new GridBagConstraints() {{
            gridx = 1;
            gridy = 3;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        panUpper.add(cbVinyl, new GridBagConstraints() {{
            gridx = 1;
            gridy = 4;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        panUpper.add(cbCd, new GridBagConstraints() {{
            gridx = 1;
            gridy = 5;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        panUpper.add(cbCassette, new GridBagConstraints() {{
            gridx = 1;
            gridy = 6;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Genre(s)
        PlaceholderTextField tfGenre = new PlaceholderTextField("Genres (Mit Komma trennen)");
        panUpper.add(tfGenre, new GridBagConstraints() {{
            gridx = 1;
            gridy = 7;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Where bought?
        PlaceholderTextField tfWhereBought = new PlaceholderTextField("Woher?");
        panUpper.add(tfWhereBought, new GridBagConstraints() {{
            gridx = 1;
            gridy = 8;
            weightx = 0.7;
            insets = new Insets(0, 5, 0, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Button to search for cover in web
        JButton bSearchForCover = new JButton("Nach Cover suchen");
        bSearchForCover.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bSearchForCover.addActionListener(e -> {
            new AlbumCoverSearcher(this, tfName.getText(), tfArtist.getText()).start();
        });
        panUpper.add(bSearchForCover, new GridBagConstraints() {{
            gridx = 0;
            gridy = 8;
            weightx = 0.3;
            weighty = 0;
            insets = new Insets(5, 5, 0, 5);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.NONE;
        }});

        // Lower panel for track list
        JPanel panLower = new JPanel();
        panLower.setLayout(new GridBagLayout());

        // Track List
        panTracks = new JPanel(new GridBagLayout());

        // CheckBox for Nulltracks
        cbNulltrack = new JCheckBox("Beinhaltet Nulltrack"); // If checked, index will start at 0
        cbNulltrack.addActionListener(_ -> {
            if(cbNulltrack.isSelected()) {
                for (TrackEntry llTrack : llTracks) {
                    llTrack.setIndex(llTrack.getIndex() - 1);
                }
            } else {
                for (TrackEntry llTrack : llTracks) {
                    llTrack.setIndex(llTrack.getIndex() + 1);
                }
            }
        });

        panTracks.add(cbNulltrack, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            gridwidth = 2;
            weightx = 1.0;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.WEST;
            fill = GridBagConstraints.NONE;
        }});

        // Head row with category names to make it look like a table
        JPanel headRow = new JPanel(new GridBagLayout());
        if (settings.getRowContrast()) headRow.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

        GridBagConstraints gbcHeadRow = new GridBagConstraints() {{
            gridx = 0;
            weightx = 0;
            insets = new Insets(4, 8, 4, 8);
        }};
        headRow.add(new JLabel("Nr.", SwingConstants.CENTER), gbcHeadRow); // Track number

        gbcHeadRow.gridx = 1;
        gbcHeadRow.fill = GridBagConstraints.HORIZONTAL;
        gbcHeadRow.weightx = 1.0;
        headRow.add(new JLabel("Trackname", SwingConstants.CENTER), gbcHeadRow); // Track name

        gbcHeadRow.gridx = 2;
        gbcHeadRow.weightx = 0;
        gbcHeadRow.fill = GridBagConstraints.NONE;
        lDelete = new JLabel("Löschen", SwingConstants.CENTER);
        headRow.add(lDelete, gbcHeadRow); // Delete button

        panTracks.add(headRow, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            gridwidth = 3;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Add five blank entries as placeholder
        for(int i = 0; i < 5; i++) {
            addTrackRow();
        }

        // Wrap panTracks so its content stays top-aligned inside the scroll pane
        JPanel panTracksWrapper = new JPanel(new BorderLayout());
        panTracksWrapper.add(panTracks, BorderLayout.NORTH);

        // Add track panel to JScrollPane (use wrapper as viewport)
        JScrollPane spTracks = new JScrollPane(panTracksWrapper);
        spTracks.getVerticalScrollBar().setUnitIncrement(16);

        // Add JScrollPane to main pane
        panLower.add(spTracks, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            gridwidth = 2;
            gridheight = 3;
            weightx = 1.0;
            weighty = 1.0;
            insets = new Insets(5, 0, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }});

        // Button to add tracks at the bottom of main panel
        JButton bAddTrack = new JButton("+");
        bAddTrack.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bAddTrack.addActionListener(e -> {
            addTrackRow();
        });
        panLower.add(bAddTrack, new GridBagConstraints() {{
            gridx = 0;
            gridy = 4;
            gridwidth = 2;
            weightx = 1;
            insets = new Insets(5, 0, 5, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.NONE;
        }});

        // Add panels to main panel
        panMain.add(panUpper, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        panMain.add(panLower, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            weighty = 1.0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.BOTH;
        }});

        // Add main panel to frame
        this.add(panMain, BorderLayout.CENTER);

        // Confirm & Return buttons
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
        bConfirm.addActionListener(_ -> {
            // Add new album to control class
            LinkedList<Track> llNewTracks = new LinkedList<>(); // Read tracks
            for (TrackEntry llTrack : llTracks) {
                llNewTracks.addLast(new Track(llTrack.getTextField().getText(), llTrack.getIndex()));
            }
            try {
                // Save album cover as image
                BufferedImage bImage = new BufferedImage(albumCover[0].getWidth(null), albumCover[0].getHeight(null), BufferedImage.TYPE_INT_ARGB); // Als PNG
                Graphics2D g2d = bImage.createGraphics();
                g2d.drawImage(albumCover[0], 0, 0, null);
                g2d.dispose();
                String path = "media\\albumCovers\\" + tfArtist.getText().toLowerCase().replace(" ", "") + "_" + tfName.getText().toLowerCase().replace(" ", "") + ".png";
                File outputFile = new File(path);
                ImageIO.write(bImage, "png", outputFile);

                // Save album in control class
                String[] genre = tfGenre.getText().replace(" ", "").split(",");
                Album newAlbum = new Album(llNewTracks, tfName.getText(), tfArtist.getText(), Integer.parseInt(tfRelease.getText()), path,
                tfWhereBought.getText(), cbNulltrack.isSelected(), cbVinyl.isSelected(), cbCd.isSelected(), cbCassette.isSelected(), genre);
                pPmt.addAlbum(newAlbum);
                this.dispose();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
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

    /**
     * Creates a new rock with index, text field and delete button and adds it to the tracks panel
     */
    private void addTrackRow() {
        llTracks.addLast(new TrackEntry(latestIndex[0], new JTextField()));

        JPanel newRow = new JPanel(new GridBagLayout());
        if (latestIndex[0] % 2 == 0 & settings.getRowContrast()) newRow.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

        GridBagConstraints gbcNewRow = new GridBagConstraints();
        gbcNewRow.insets = new Insets(4, 8, 4, 8);
        gbcNewRow.weightx = 0;

        // Track number
        gbcNewRow.gridx = 0;
        if (cbNulltrack.isSelected()) llTracks.getLast().setIndex(llTracks.getLast().getIndex() - 1);
        newRow.add(llTracks.getLast().getIndexLabel(), gbcNewRow);

        // Track name
        gbcNewRow.gridx = 1;
        gbcNewRow.fill = GridBagConstraints.HORIZONTAL;
        gbcNewRow.weightx = 1.0;
        newRow.add(llTracks.getLast().getTextField(), gbcNewRow);
        llTracks.getLast().getTextField().addKeyListener(new KeyListener() {
            final int idx = llTracks.getLast().getIndex();
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (llTracks.size() != idx) {
                        llTracks.get(idx).getTextField().requestFocus();
                    } else {
                        addTrackRow();
                        llTracks.get(idx).getTextField().requestFocus();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        // Delete button
        gbcNewRow.gridx = 2;
        gbcNewRow.weightx = 0;
        gbcNewRow.fill = GridBagConstraints.NONE;
        JButton bDelete = llTracks.getLast().getDeleteButton();
        bDelete.setPreferredSize(new Dimension(lDelete.getPreferredSize().width, bDelete.getPreferredSize().height));
        newRow.add(bDelete, gbcNewRow);

        final TrackEntry DELETE = llTracks.getLast();

        // Add action listener to delete button
        llTracks.getLast().getDeleteButton().addActionListener(e -> {
            final int DELETEIDX = DELETE.getIndex() - 1;

            // Change name of tracks to the one that are next and remove last track
            for (int i = DELETEIDX; i < llTracks.size() - 1; i++) {
                llTracks.get(i).getTextField().setText(llTracks.get(i + 1).getTextField().getText());
                llTracks.get(i).setIndex(i + 1);
            }
            llTracks.getLast().getIndexLabel().setVisible(false);
            llTracks.getLast().getTextField().setVisible(false);
            llTracks.getLast().getDeleteButton().setVisible(false);
            llTracks.removeLast();
            latestIndex[0]--;
        });

        // Add row to tracks panel
        GridBagConstraints gbcTrack = new GridBagConstraints();
        gbcTrack.gridx = 0;
        gbcTrack.gridy = latestIndex[0] + 2;
        gbcTrack.gridwidth = 3;
        gbcTrack.fill = GridBagConstraints.HORIZONTAL;

        panTracks.add(newRow, gbcTrack);
        this.revalidate();
        latestIndex[0]++;
    }

    public void setCoverFromUrl(String pUrl) {
        BufferedImage loadedImg;
        try {
            loadedImg = ImageIO.read(new URL(pUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        albumCover[0] = loadedImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        bCover.setIcon(new ImageIcon(albumCover[0]));
        bCover.setText("");
    }

    private Image iconFromUpload(int pWidth, int pHeight) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int result = chooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION) {
            File chosenFile = chooser.getSelectedFile();

            ImageIcon icon = new ImageIcon(chosenFile.getAbsolutePath());

            return icon.getImage().getScaledInstance(pWidth, pHeight, Image.SCALE_SMOOTH);
        } else {
            return null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        gui.setEnabled(true);
        gui.requestFocus();
    }
}
