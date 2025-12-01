import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.LinkedList;

import com.formdev.flatlaf.FlatClientProperties;

public class AlbumViewFrame extends JFrame {
    private LinkedList<TrackEntry> llTracks = new LinkedList<>();

    /**
     * Opens a JFrame where all the album's information can be seen
     * @param pAlbum Album object to view
     * @param pPmt Object of control class
     */
    public AlbumViewFrame(Album pAlbum, Pmt pPmt, Settings settings) {
        this.setTitle(pAlbum.getAlbumName());
        this.setSize(500, 480);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // Main panel that contains upper and lower panels
        JPanel panMain = new JPanel();
        panMain.setLayout(new GridBagLayout());
        panMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Upper panel for album cover, name, artist & release
        JPanel panUpper = new JPanel();
        panUpper.setLayout(new GridBagLayout());

        // JPanel with album cover
        JLabel lCover = new JLabel();
        lCover.setOpaque(true);
        lCover.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        lCover.setForeground(new Color(150, 150, 150));
        lCover.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lCover.setPreferredSize(new Dimension(200, 200));
        lCover.setMinimumSize(new Dimension(200, 200));
        lCover.setMaximumSize(new Dimension(200, 200));
        ImageIcon icon = new ImageIcon(pAlbum.getCoverPath());
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        lCover.setIcon(new ImageIcon(scaledImage));
        panUpper.add(lCover, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            weighty = 0;
            gridheight = 6;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        // Album name
        JPanel panName = new JPanel(new GridLayout(2, 1));
        panName.add(new JLabel("Name:"));
        panName.add(new JLabel(pAlbum.getAlbumName()));
        panUpper.add(panName, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Album artist
        JPanel panArtist = new JPanel(new GridLayout(2, 1));
        panArtist.add(new JLabel("Künstler:"));
        panArtist.add(new JLabel(pAlbum.getAlbumArtist()));
        panUpper.add(panArtist, new GridBagConstraints() {{
            gridx = 1;
            gridy = 1;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Release Year
        JPanel panRelease = new JPanel(new GridLayout(2, 1));
        panRelease.add(new JLabel("Erscheinungsjahr:"));
        panRelease.add(new JLabel(pAlbum.getReleaseYear() + ""));
        panUpper.add(panRelease, new GridBagConstraints() {{
            gridx = 1;
            gridy = 2;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Type of media
        JPanel panMedia = new JPanel(new GridLayout(2, 1));
        panMedia.add(new JLabel("Tonträger:"));

        LinkedList<String> llMedia = new LinkedList<>();
        if (pAlbum.isOnVinyl()) llMedia.add("Vinyl");
        if (pAlbum.isOnCd()) llMedia.add("CD");
        if (pAlbum.isOnCassette()) llMedia.add("Kassette");

        StringBuilder typeOfMedia = null;
        for (String s : llMedia) {
            if (typeOfMedia == null) {
                typeOfMedia = new StringBuilder(s);
            } else {
                typeOfMedia.append(", ").append(s);
            }
        }
        panMedia.add(new JLabel(typeOfMedia.toString()));

        panUpper.add(panMedia, new GridBagConstraints() {{
            gridx = 1;
            gridy = 3;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Where bought?
        JPanel panFromWhere = new JPanel(new GridLayout(2, 1));
        panFromWhere.add(new JLabel("Erhalten von/bei:"));
        panFromWhere.add(new JLabel(pAlbum.getWhereBought()));
        panUpper.add(panFromWhere, new GridBagConstraints() {{
            gridx = 1;
            gridy = 4;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Last time listened
        JPanel panLastListen = new  JPanel(new GridLayout(2, 1));
        panLastListen.add(new JLabel("Letztes Mal gehört:"));
        if (!pAlbum.getListeningTimes().isEmpty()) {
            panLastListen.add(new JLabel(pAlbum.getListeningTimes().getLast().getDate() + ", " + pAlbum.getListeningTimes().getLast().getTime() + " Uhr."));
        } else {
            panLastListen.add(new JLabel("Noch nicht gehört."));
        }
        panUpper.add(panLastListen, new GridBagConstraints() {{
            gridx = 1;
            gridy = 5;
            weightx = 0.7;
            insets = new Insets(0, 5, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Lower panel for track list
        JPanel panLower = new JPanel();
        panLower.setLayout(new GridBagLayout());

        // Track List
        JPanel panTracks = new JPanel();
        panTracks.setLayout(new GridBagLayout());
        int latestIndex = 1;

        // Add category names for table
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.gridx = 0;
        c.weightx = 0;

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

        JLabel lListen = new JLabel("Gehört", SwingConstants.CENTER);
        panTracks.add(lListen, c);

        // Add all tracks from album
        if (pAlbum.containsNulltrack()) latestIndex = 0;
        for(int i = 0; i < pAlbum.getTrackList().size(); i++) {
            JPanel newRow = new JPanel(new GridBagLayout());
            if (i % 2 == 0 & settings.getRowContrast()) newRow.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));

            GridBagConstraints gbcNewRow = new GridBagConstraints();
            gbcNewRow.insets = new Insets(4, 8, 4, 8);
            gbcNewRow.weightx = 0;

            // Track number
            JLabel newRowLabel = new JLabel(String.valueOf(latestIndex), SwingConstants.CENTER);
            newRowLabel.setPreferredSize(new Dimension(20, newRowLabel.getPreferredSize().height));
            gbcNewRow.gridx = 0;
            newRow.add(newRowLabel, gbcNewRow);

            // Track name
            gbcNewRow.gridx = 1;
            gbcNewRow.fill = GridBagConstraints.HORIZONTAL;
            gbcNewRow.weightx = 1.0;
            newRow.add(new JLabel(pAlbum.getTrackList().get(i).getTrackName(), SwingConstants.LEFT), gbcNewRow);

            // Listen counter
            gbcNewRow.gridx = 2;
            gbcNewRow.weightx = 0;
            gbcNewRow.fill = GridBagConstraints.NONE;
            JLabel newRowListen = new JLabel(String.valueOf(pAlbum.getTrackList().get(i).getListenCount()), SwingConstants.CENTER);
            newRowListen.setPreferredSize(new Dimension(lListen.getPreferredSize().width, newRowListen.getPreferredSize().height));
            newRow.add(newRowListen, gbcNewRow);

            // Add row to tracks panel
            GridBagConstraints gbcTrack = new GridBagConstraints();
            gbcTrack.gridx = 0;
            gbcTrack.gridy = i + 1;
            gbcTrack.gridwidth = 3;
            gbcTrack.fill = GridBagConstraints.HORIZONTAL;

            panTracks.add(newRow, gbcTrack);
            latestIndex++;
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

        // Return button
        JPanel panButtons = new JPanel();
        panButtons.setLayout(new GridBagLayout());

        JButton bReturn = new JButton("Zurück");
        bReturn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bReturn.addActionListener(_ -> {
            this.dispose();
        });
        panButtons.add(bReturn, new GridBagConstraints() {{
            gridy = 0;
            gridx = 0;
            weightx = 0.1;
            insets = new Insets(0, 0, 5, 5);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        this.add(panButtons, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}
