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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import com.formdev.flatlaf.FlatClientProperties;

public class AlbumEditFrame extends JFrame implements IWebCoverSearcher, IWebTracklistSearcher {
    private final Settings SETTINGS;
    private final JFrame PARENT;
    private LinkedList<TrackEntry> llTracks = new LinkedList<>();
    private Image[] albumCover = {null};
    private JButton bCover;
    private JCheckBox cbNulltrack;
    private JLabel lDelete;
    private int[] latestIndex = {1}; // Must be an array to be changeable in ActionListener class
    private JPanel panTracks;

    /**
     * Opens a JFrame where all the album's information can be edited
     * @param PARENT Parent frame of this frame
     * @param album Album object to edit
     * @param pmt Object of control class
     * @param SETTINGS Settings object containing settings data
     */
    public AlbumEditFrame(JFrame PARENT, Pmt pmt, Album album, Settings SETTINGS) {
        this.SETTINGS = SETTINGS;
        this.PARENT = PARENT;
        PARENT.setEnabled(false);

        this.setTitle(album.getAlbumName());
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

        // JButton where cover can be changed
        bCover = new JButton();
        bCover.setOpaque(true);
        bCover.setBackground(SETTINGS.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        bCover.setForeground(new Color(150, 150, 150));
        bCover.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        bCover.setPreferredSize(new Dimension(200, 200));
        bCover.setMinimumSize(new Dimension(200, 200));
        bCover.setMaximumSize(new Dimension(200, 200));
        bCover.addActionListener(_ -> {
            albumCover[0] = iconFromUpload(bCover.getWidth(), bCover.getHeight());
            if(albumCover[0] != null) {
                bCover.setIcon(new ImageIcon(albumCover[0]));
                bCover.setText("");
            }
        });
        ImageIcon icon = new ImageIcon(album.getCoverPath());
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        bCover.setIcon(new ImageIcon(scaledImage));
        panUpper.add(bCover, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            weighty = 0;
            gridheight = 9;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        // Album name
        PlaceholderTextField tfName = new PlaceholderTextField("Name");
        tfName.setText(album.getAlbumName());
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
        tfArtist.setText(album.getAlbumArtist());
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
        tfRelease.setText(album.getReleaseYear() + "");
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
        JCheckBox cbVinyl = new JCheckBox("Vinyl", album.isOnVinyl());
        JCheckBox cbCd = new JCheckBox("CD", album.isOnCd());
        JCheckBox cbCassette = new JCheckBox("Kassette", album.isOnCassette());

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
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Genre(s)
        PlaceholderTextField tfGenre = new PlaceholderTextField("Genres (Mit Komma trennen)");
        if (album.getGenres() != null) tfGenre.setText(String.join(", ", album.getGenres()));
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
        tfWhereBought.setText(album.getWhereBought());
        panUpper.add(tfWhereBought, new GridBagConstraints() {{
            gridx = 1;
            gridy = 7;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Edit sessions
        JButton bSessions = new  JButton("Sessions bearbeiten");
        bSessions.addActionListener(_ -> {
            new EditListeningSessionsFrame(SETTINGS, pmt, this, album);
        });
        panUpper.add(bSessions, new GridBagConstraints() {{
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
            new WebCoverSearcher(this, tfName.getText(), tfArtist.getText()).start();
        });
        panUpper.add(bSearchForCover, new GridBagConstraints() {{
            gridx = 0;
            gridy = 9;
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
        panTracks = new JPanel();
        panTracks.setLayout(new GridBagLayout());

        // CheckBox for Nulltracks
        cbNulltrack = new JCheckBox("Beinhaltet Nulltrack", album.containsNulltrack()); // If checked, index will start at 0
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
            gridwidth = 3;
            weightx = 1.0;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Head row with category names to make it look like a table
        JPanel headRow = new JPanel(new GridBagLayout());
        if (SETTINGS.getRowContrast()) headRow.setBackground(SETTINGS.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

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

        // Add all tracks from album
        if (album.containsNulltrack()) latestIndex[0] = 0;
        for(int i = 0; i < album.getTrackList().size(); i++) {
            llTracks.addLast(new TrackEntry(latestIndex[0], new JTextField(album.getTrackList().get(i).getTrackName())));

            JPanel newRow = new JPanel(new GridBagLayout());
            if (latestIndex[0] % 2 == 0 & SETTINGS.getRowContrast()) newRow.setBackground(SETTINGS.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

            GridBagConstraints gbcNewRow = new GridBagConstraints();
            gbcNewRow.insets = new Insets(4, 8, 4, 8);
            gbcNewRow.weightx = 0;

            // Track number
            gbcNewRow.gridx = 0;
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
            llTracks.getLast().getDeleteButton().addActionListener(_ -> {
                final int DELETEIDX = DELETE.getIndex() - 1;

                // Change name of tracks to the one that are next and remove last track 
                for (int j = DELETEIDX; j < llTracks.size() - 1; j++) {
                    llTracks.get(j).getTextField().setText(llTracks.get(j + 1).getTextField().getText());
                    llTracks.get(j).setIndex(j + 1);
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
            gbcTrack.gridy = i + 2;
            gbcTrack.gridwidth = 3;
            gbcTrack.fill = GridBagConstraints.HORIZONTAL;

            panTracks.add(newRow, gbcTrack);
            latestIndex[0]++;
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

        // Button to search for track list
        JButton bSearchTracks = new JButton("Tracklist suchen");
        bSearchTracks.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bSearchTracks.addActionListener(_ -> {
            new WebTracklistSearcher(this, tfName.getText(), tfArtist.getText()).start();
        });
        panLower.add(bSearchTracks, new GridBagConstraints() {{
            gridx = 0;
            gridy = 4;
            anchor = GridBagConstraints.WEST;
            fill = GridBagConstraints.NONE;
        }});

        // Button to add tracks at the bottom of main panel
        JButton bAddTrack = new JButton("+");
        bAddTrack.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bAddTrack.addActionListener(_ -> {
            addTrackRow();
        });
        panLower.add(bAddTrack, new GridBagConstraints() {{
            gridx = 1;
            gridy = 4;
            anchor  = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
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
            // Add new album to control class
            LinkedList<Track> llNewTracks = new LinkedList<>(); // Read tracks
            for (TrackEntry llTrack : llTracks) {
                llNewTracks.addLast(new Track(llTrack.getTextField().getText(), llTrack.getIndex()));
            }
            try {
                // Save album cover as image and remove old one from files if cover was changed
                String path;
                if (albumCover[0] != null) { 
                    // Delete old cover
                    Files.delete(Paths.get(album.getCoverPath()));

                    // Save new cover
                    BufferedImage bImage = new BufferedImage(albumCover[0].getWidth(null), albumCover[0].getHeight(null), BufferedImage.TYPE_INT_ARGB); // Als PNG
                    Graphics2D g2d = bImage.createGraphics();
                    g2d.drawImage(albumCover[0], 0, 0, null);
                    g2d.dispose();
                    path = "media\\albumCovers\\" + tfArtist.getText().toLowerCase().replace(" ", "") + "_" + tfName.getText().toLowerCase().replace(" ", "") + ".png";
                    File outputFile = new File(path);
                    ImageIO.write(bImage, "png", outputFile);
                } else {
                    path = album.getCoverPath();
                }

                // Change album in control class
                String[] genre = tfGenre.getText().replace(" ", "").split(",");
                Album newAlbum = new Album(llNewTracks, tfName.getText(), tfArtist.getText(), Integer.parseInt(tfRelease.getText()), path,
                tfWhereBought.getText(), cbNulltrack.isSelected(), cbVinyl.isSelected(), cbCd.isSelected(), cbCassette.isSelected(), genre);
                newAlbum.setSessions(album.getSessions());
                newAlbum.setTrackList(album.getTrackList());
                pmt.editAlbum(album, newAlbum);
                this.dispose();
            } catch (NumberFormatException | IOException e) {
                Log.error(e.getMessage());
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
     * Adds a new row to track list on frame.
     */
    public void addTrackRow() {
        llTracks.addLast(new TrackEntry(latestIndex[0], new JTextField()));

        JPanel newRow = new JPanel(new GridBagLayout());
        if (latestIndex[0] % 2 == 0 & SETTINGS.getRowContrast()) newRow.setBackground(SETTINGS.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

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
                        AlbumEditFrame.this.addTrackRow();
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

    /**
     * Fills text fields for track names with data from a LinkedList.
     * @param pTracklist LinkedList containing tracks (TrackObjects).
     */
    public void setTracklist(LinkedList<TrackObject> pTracklist) {
        for (int i = 0; i < pTracklist.size(); i++) {
            llTracks.get(i).getTextField().setText(pTracklist.get(i).getTitle());
            if (llTracks.get(i).equals(llTracks.getLast()) & pTracklist.get(i) != pTracklist.getLast()) addTrackRow();
        }
    }

    /**
     * Changes the album cover image to an image of a given URL.
     * Displays error text on album cover button if pUrl is null.
     * @param pUrl URL link to album cover image.
     */
    public void setCoverFromUrl(String pUrl) {
        // If no cover was found or an error occurred
        if (pUrl == null) {
            bCover.setText("Cover nicht gefunden");
            return;
        }

        BufferedImage loadedImg;
        try {
            loadedImg = ImageIO.read(new URL(pUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        albumCover[0] = loadedImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        bCover.setIcon(new ImageIcon(albumCover[0]));
        bCover.setText("");
        Log.info("Cover wurde durch URL gesetzt: " + pUrl);
    }

    /**
     * Opens frame for user to upload own image for album cover and returns it scaled as an Image object.
     * @param pWidth Width of cover.
     * @param pHeight Height of cover.
     * @return Scaled image uploaded by user.
     */
    private Image iconFromUpload(int pWidth, int pHeight) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int result = chooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION) {
            File chosenFile = chooser.getSelectedFile();

            ImageIcon icon = new ImageIcon(chosenFile.getAbsolutePath());

            Image scaledImage = icon.getImage().getScaledInstance(pWidth, pHeight, Image.SCALE_SMOOTH);
            return scaledImage;
        } else {
            return null;
        }
    }

    /**
     * Override dispose method to focus on parent frame when disposing
     */
    @Override
    public void dispose() {
        super.dispose();
        PARENT.setEnabled(true);
        PARENT.requestFocus();
    }
}
