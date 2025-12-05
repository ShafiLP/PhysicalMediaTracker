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

public class AlbumEditFrame extends JFrame implements CoverSearcher {
    private Settings settings;
    private LinkedList<TrackEntry> llTracks = new LinkedList<>();
    private Image[] albumCover = {null};
    private JButton bCover;
    private JCheckBox cbNulltrack;
    private JLabel lDelete;
    private int[] latestIndex = {1}; // Must be array to be changable in ActionListener class
    private JPanel panTracks;

    /**
     * Opens a JFrame where all the album's information can be edited
     * @param album Album object to edit
     * @param pmt Object of control class
     */
    public AlbumEditFrame(Album album, Pmt pmt, Settings settings) {
        this.settings = settings;

        this.setTitle(album.getAlbumName());
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

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
        bCover.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
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
            gridheight = 8;
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
            insets = new Insets(0, 5, 0, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Button to search for cover in web
        JButton bSearchForCover = new JButton("Nach Cover suchen");
        bSearchForCover.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bSearchForCover.addActionListener(e -> {
            try {
                AlbumCoverSearcher.searchCover(this, tfName.getText(), tfArtist.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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
        panTracks = new JPanel();
        panTracks.setLayout(new GridBagLayout());

        // CheckBox for Nulltracks
        cbNulltrack = new JCheckBox("Beinhaltet Nulltrack", album.containsNulltrack()); // If checked, index will start at 0
        cbNulltrack.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
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

        // Add category names for table
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.gridx = 0;
        c.weightx = 0;
        c.gridy = 1;

        JLabel lNr = new JLabel("Nr.", SwingConstants.CENTER);
        lNr.setPreferredSize(new Dimension(20, lNr.getPreferredSize().height));
        panTracks.add(lNr, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        JLabel lName = new JLabel("Trackname", SwingConstants.CENTER);
        lNr.setPreferredSize(new Dimension(20, lName.getPreferredSize().height));
        panTracks.add(lName, c);

        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;

        lDelete = new JLabel("Löschen", SwingConstants.CENTER);
        panTracks.add(lDelete, c);

        // Add all tracks from album
        if (album.containsNulltrack()) latestIndex[0] = 0;
        for(int i = 0; i < album.getTrackList().size(); i++) {
            llTracks.addLast(new TrackEntry(latestIndex[0], new JTextField(album.getTrackList().get(i).getTrackName())));

            JPanel newRow = new JPanel(new GridBagLayout());
            if (latestIndex[0] % 2 == 0 & settings.getRowContrast()) newRow.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

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
                pmt.editAlbum(album, newAlbum);
                this.dispose();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

    public void addTrackRow() {
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

            Image scaledImage = icon.getImage().getScaledInstance(pWidth, pHeight, Image.SCALE_SMOOTH);
            return scaledImage;
        } else {
            return null;
        }
    }
}
