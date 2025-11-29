import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

class AlbumComponent extends JPanel {

    /**
     * Empty constructor
     */
    public AlbumComponent() {}

    /**
     * Constructor of AlbumComponent class
     * @param album Album object with all album information
     * @param pmt Object of control class
     */
    public AlbumComponent(Album album, Pmt pmt) {
        LinkedList<String> llMedia = new LinkedList<>();
        if (album.isOnVinyl()) llMedia.add("Vinyl");
        if (album.isOnCd()) llMedia.add("CD");
        if (album.isOnCassette()) llMedia.add("Kassette");

        // Popup Menu, opens when clicking on AlbumComponent
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuListen = new JMenuItem("Gehört");
        JMenuItem menuView = new JMenuItem("Ansehen");
        JMenuItem menuEdit = new JMenuItem("Bearbeiten");
        JMenuItem menuDelete = new JMenuItem("Löschen");

        menuListen.addActionListener(e -> {
            new AddListenSessionFrame(pmt, album);
        });
        menuView.addActionListener(e -> {
            new AlbumViewFrame(album, pmt);
        });
        menuEdit.addActionListener(e -> {
            new AlbumEditFrame(album, pmt);
        });
        menuDelete.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                null,
                "Wirklich " + album.getAlbumName() + " löschen?",
                "Album löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            switch (n) {
                case 0:
                    pmt.deleteAlbum(album);
                    break;
                default:
                    System.out.println("Deletion cancelled"); // DEBUG
                    break;
            }
        });

        popupMenu.add(menuListen);
        popupMenu.add(menuView);
        popupMenu.add(menuEdit);
        popupMenu.add(menuDelete);

        this.addMouseListener(new MouseAdapter() {
            // Open menu when clicked
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(e.getComponent(), AlbumComponent.this.getWidth()/2, AlbumComponent.this.getHeight()/2);
            }

            // Change border to accent color when hovering
            @Override
            public void mouseEntered(MouseEvent e) {
                AlbumComponent.this.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.focusColor"), 2));
            }

            // Change border back to default when hover exit
            @Override
            public void mouseExited(MouseEvent e) {
                AlbumComponent.this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
            }
        });

        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        // Cover
        ImageIcon icon = new ImageIcon(album.getCoverPath());
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel lImg = new JLabel(new ImageIcon(img));
        this.add(lImg, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Name
        JLabel lName = new JLabel(album.getAlbumName());
        lName.setBackground(new Color(200, 200, 200));
        lName.setOpaque(true);
        lName.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lName, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            weighty = 1.0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Artist
        JLabel lArtist = new JLabel(album.getAlbumArtist());
        lArtist.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lArtist, new GridBagConstraints() {{
            gridx = 0;
            gridy = 2;
            weightx = 1;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Release Year
        JLabel lRelease = new JLabel(album.getReleaseYear() + "");
        lRelease.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        lRelease.setBackground(new Color(200, 200, 200));
        lRelease.setOpaque(true);
        this.add(lRelease, new GridBagConstraints() {{
            gridx = 0;
            gridy = 3;
            weightx = 1;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Listen counter
        JLabel lListens = new JLabel(album.getListenCount() + " Mal gehört");
        lListens.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lListens, new GridBagConstraints() {{
            gridx = 0;
            gridy = 4;
            weightx = 1;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Set size
        this.setMaximumSize(new Dimension(150, getPreferredSize().height));
        this.setMinimumSize(new Dimension(150, getPreferredSize().height));
        this.setPreferredSize(new Dimension(150, getMaximumSize().height));
    }
}