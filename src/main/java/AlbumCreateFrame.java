import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

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
        LinkedList<TrackEntry> llTrackEntries = new LinkedList<>();
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
        cbNulltrack.addActionListener(_ -> {
            if(cbNulltrack.isSelected()) {
                for(int i = 0; i < llTrackEntries.size(); i++) {
                    llTrackEntries.get(i).setIndex(llTrackEntries.get(i).getIndex() - 1);
                }
            } else {
                for(int i = 0; i < llTrackEntries.size(); i++) {
                    llTrackEntries.get(i).setIndex(llTrackEntries.get(i).getIndex() + 1);
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
        gbcTextField.weightx = 0.8;
        gbcTextField.insets = new Insets(0, 0, 5, 0);
        gbcTextField.anchor = GridBagConstraints.CENTER;
        gbcTextField.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints gbcDelete = new GridBagConstraints();
        gbcDelete.gridx = 2;
        gbcDelete.gridy = 1;
        gbcDelete.weightx = 0.1;
        gbcDelete.insets = new Insets(0, 0, 5, 5);
        gbcDelete.anchor = GridBagConstraints.CENTER;
        gbcDelete.fill = GridBagConstraints.NONE;

        // Add five blank entries as placeholder
        for(int i = 0; i < 5; i++) {
            llTrackEntries.addLast(new TrackEntry(latestIndex[0], new JTextField())); // Add new entry to LinkedList so index and JTextField can easily be accessed

            panTracks.add(llTrackEntries.getLast().getIndexLabel(), gbcIndex);
            gbcIndex.gridy++;

            panTracks.add(llTrackEntries.getLast().getTextField(), gbcTextField);
            gbcTextField.gridy++;

            panTracks.add(llTrackEntries.getLast().getDeleteButton(), gbcDelete);
            gbcDelete.gridy++;

            final TrackEntry DELETE = llTrackEntries.getLast();

            // Add action listener to delete button
            llTrackEntries.getLast().getDeleteButton().addActionListener(_ -> {
                final int DELETEIDX = DELETE.getIndex() - 1;

                // Change name of tracks to the one that are next and remove last track 
                for (int j = DELETEIDX; j < llTrackEntries.size() - 1; j++) {
                    llTrackEntries.get(j).getTextField().setText(llTrackEntries.get(j + 1).getTextField().getText());
                    llTrackEntries.get(j).setIndex(j + 1);
                }
                llTrackEntries.getLast().getIndexLabel().setVisible(false);
                llTrackEntries.getLast().getTextField().setVisible(false);
                llTrackEntries.getLast().getDeleteButton().setVisible(false);
                llTrackEntries.removeLast();
                gbcIndex.gridy--;
                gbcTextField.gridy--;
                gbcDelete.gridy--;
                latestIndex[0]--;
            });

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
            llTrackEntries.addLast(new TrackEntry(latestIndex[0], new JTextField()));

            panTracks.add(llTrackEntries.getLast().getIndexLabel(), gbcIndex);
            gbcIndex.gridy++;

            panTracks.add(llTrackEntries.getLast().getTextField(), gbcTextField);
            gbcTextField.gridy++;

            panTracks.add(llTrackEntries.getLast().getDeleteButton(), gbcDelete);
            gbcDelete.gridy++;

            final TrackEntry DELETE = llTrackEntries.getLast();

            // Add action listener to delete button
            llTrackEntries.getLast().getDeleteButton().addActionListener(_ -> {
                final int DELETEIDX = DELETE.getIndex() - 1;

                // Change name of tracks to the one that are next and remove last track 
                for (int i = DELETEIDX; i < llTrackEntries.size() - 1; i++) {
                    llTrackEntries.get(i).getTextField().setText(llTrackEntries.get(i + 1).getTextField().getText());
                    llTrackEntries.get(i).setIndex(i + 1);
                }
                llTrackEntries.getLast().getIndexLabel().setVisible(false);
                llTrackEntries.getLast().getTextField().setVisible(false);
                llTrackEntries.getLast().getDeleteButton().setVisible(false);
                llTrackEntries.removeLast();
                gbcIndex.gridy--;
                gbcTextField.gridy--;
                gbcDelete.gridy--;
                latestIndex[0]--;
            });

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
            for(int i = 0; i < llTrackEntries.size(); i++) {
                llNewTracks.addLast(new Track(llTrackEntries.get(i).getTextField().getText(), llTrackEntries.get(i).getIndex()));
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
