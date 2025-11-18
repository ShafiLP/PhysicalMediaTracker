import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

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

