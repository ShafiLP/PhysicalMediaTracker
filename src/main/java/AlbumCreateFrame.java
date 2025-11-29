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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import com.formdev.flatlaf.FlatClientProperties;

public class AlbumCreateFrame extends JFrame {

    /**
     * Opens a JFrame where a new Album object can be created
     * @param pPmt Object of control class
     */
    public AlbumCreateFrame(Pmt pPmt) {
        // Local objects
        LinkedList<TrackEntry> llTracks = new LinkedList<>();
        Image[] albumCover = {null}; // Array so local object can be accessed in action listener

        this.setTitle("Neues Album");
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

        // JButton where cover can be uploaded
        JButton bCover = new JButton("Cover hinzufügen");
        bCover.setOpaque(true);
        bCover.setBackground(new Color(200, 200, 200));
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
        panUpper.add(bCover, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            weighty = 0;
            gridheight = 8;
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
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Where bought?
        PlaceholderTextField tfWhereBought = new PlaceholderTextField("Woher?");
        panUpper.add(tfWhereBought, new GridBagConstraints() {{
            gridx = 1;
            gridy = 7;
            weightx = 0.7;
            insets = new Insets(0, 5, 0, 0);
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Lower panel for track list
        JPanel panLower = new JPanel();
        panLower.setLayout(new GridBagLayout());

        // Track List
        JPanel panTracks = new JPanel(new GridBagLayout());
        int[] latestIndex = {1}; // Must be array to be changable in ActionListener class

        // CheckBox for Nulltracks
        JCheckBox cbNulltrack = new JCheckBox("Beinhaltet Nulltrack"); // If checked, index will start at 0
        cbNulltrack.setBackground(new Color(200, 200, 200));
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

        JLabel lDelete = new JLabel("Löschen", SwingConstants.CENTER);
        panTracks.add(lDelete, c);

        // Add five blank entries as placeholder
        for(int i = 0; i < 5; i++) {
            llTracks.addLast(new TrackEntry(latestIndex[0], new JTextField())); // Add new entry to LinkedList so index and JTextField can easily be accessed

            JPanel newRow = new JPanel(new GridBagLayout());
            if (latestIndex[0] % 2 == 0) newRow.setBackground(new Color(200, 200, 200));

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
        bAddTrack.addActionListener(_ -> {
            llTracks.addLast(new TrackEntry(latestIndex[0], new JTextField()));

            JPanel newRow = new JPanel(new GridBagLayout());
            if (latestIndex[0] % 2 == 0) newRow.setBackground(new Color(200, 200, 200));

            GridBagConstraints gbcNewRow = new GridBagConstraints();
            gbcNewRow.insets = new Insets(4, 8, 4, 8);
            gbcNewRow.weightx = 0;

            // Track number
            JLabel newRowLabel = new JLabel(String.valueOf(latestIndex[0]), SwingConstants.CENTER);
            newRowLabel.setPreferredSize(new Dimension(20, newRowLabel.getPreferredSize().height));
            gbcNewRow.gridx = 0;
            newRow.add(newRowLabel, gbcNewRow);

            // Track name
            gbcNewRow.gridx = 1;
            gbcNewRow.fill = GridBagConstraints.HORIZONTAL;
            gbcNewRow.weightx = 1.0;
            newRow.add(llTracks.getLast().getTextField(), gbcNewRow);

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
            for(int i = 0; i < llTracks.size(); i++) {
                llNewTracks.addLast(new Track(llTracks.get(i).getTextField().getText(), llTracks.get(i).getIndex()));
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
                Album newAlbum = new Album(llNewTracks, tfName.getText(), tfArtist.getText(), Integer.parseInt(tfRelease.getText()), path,
                tfWhereBought.getText(), cbNulltrack.isSelected(), cbVinyl.isSelected(), cbCd.isSelected(), cbCassette.isSelected());
                pPmt.addAlbum(newAlbum);
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
