import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

class AlbumComponent extends JPanel {
    private Settings settings;
    private Album album;
    private JLabel lImg;
    private JLabel lName;
    private JLabel lRelease;
    private boolean mouseListenerEnabled = true;

    /**
     * Empty constructor
     */
    public AlbumComponent() {}

    /**
     * Constructor of AlbumComponent class
     * @param album Album object with all album information
     * @param pmt Object of control class
     */
    public AlbumComponent(Gui gui, Pmt pmt, Album album, Settings settings) {
        this.settings = settings;
        this.album = album;

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
            new AddSessionFrame(pmt, album);
        });
        menuView.addActionListener(e -> {
            new AlbumViewFrame(gui, pmt, album, settings);
        });
        menuEdit.addActionListener(e -> {
            new AlbumEditFrame(gui, pmt, album, settings);
        });
        menuDelete.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                null,
                "Wirklich " + album.getAlbumName() + " löschen?",
                "Album löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (n == 0) {
                pmt.deleteAlbum(album);
            } else {
                Log.info("Deletion cancelled");
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
                if (mouseListenerEnabled) popupMenu.show(e.getComponent(), AlbumComponent.this.getWidth()/2, AlbumComponent.this.getHeight()/2);
            }

            // Change border to accent color when hovering
            @Override
            public void mouseEntered(MouseEvent e) {
                if (mouseListenerEnabled) {
                    AlbumComponent.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    AlbumComponent.this.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.focusColor"), 2));
                }
            }

            // Change border back to default when hover exit
            @Override
            public void mouseExited(MouseEvent e) {
                if (mouseListenerEnabled) {
                    AlbumComponent.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    AlbumComponent.this.setBorder(BorderFactory.createLineBorder(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200)));
                }
            }
        });

        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200)));

        // Cover
        ImageIcon icon = new ImageIcon(album.getCoverPath());
        Image img = icon.getImage().getScaledInstance(settings.getUiScale() * 50, settings.getUiScale() * 50, Image.SCALE_SMOOTH);
        lImg = new JLabel(new ImageIcon(img));
        this.add(lImg, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.BOTH;
        }});

        // Name
        lName = new JLabel(album.getAlbumName());
        lName.setFont(settings.getFont());
        if (settings.getRowContrast()) lName.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        lName.setOpaque(true);
        lName.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lName, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            weighty = 0.0001;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Artist
        JLabel lArtist = new JLabel(album.getAlbumArtist());
        lArtist.setFont(settings.getFont());
        lArtist.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lArtist, new GridBagConstraints() {{
            gridx = 0;
            gridy = 2;
            weightx = 1;
            weighty = 0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Release Year
        lRelease = new JLabel(album.getReleaseYear() + "");
        lRelease.setFont(settings.getFont());
        lRelease.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        if (settings.getRowContrast()) lRelease.setBackground(settings.isDarkmode() ? new Color(75, 75, 75) : new Color(200, 200, 200));
        lRelease.setOpaque(true);
        this.add(lRelease, new GridBagConstraints() {{
            gridx = 0;
            gridy = 3;
            weightx = 1;
            weighty = 0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Listen counter
        JLabel lListens = new JLabel(album.getListenCount() + " Mal gehört");
        lListens.setFont(settings.getFont());
        lListens.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        this.add(lListens, new GridBagConstraints() {{
            gridx = 0;
            gridy = 4;
            weightx = 1;
            weighty = 0;
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Set size
        this.setMaximumSize(new Dimension(settings.getUiScale() * 50, getPreferredSize().height));
        this.setMinimumSize(new Dimension(settings.getUiScale() * 50, getPreferredSize().height));
        this.setPreferredSize(new Dimension(settings.getUiScale() * 50, getMaximumSize().height));
    }

    public void setMouseListenerEnabled(boolean mouseListenerEnabled) {
        this.mouseListenerEnabled = mouseListenerEnabled;
    }

    public void setUiScale(int pScale) {
        int width = pScale * 50;

        // Change size of image icon
        ImageIcon icon = new ImageIcon(album.getCoverPath());
        Image img = icon.getImage().getScaledInstance(width, width, Image.SCALE_SMOOTH);
        lImg.setPreferredSize(new Dimension(width, width));
        lImg.setIcon(new ImageIcon(img));

        // Calculate new height
        int height = width + lName.getPreferredSize().height * 4 + getInsets().top + getInsets().bottom;

        // Set maximum size
        this.setPreferredSize(new Dimension(width, height));

        // Revalidate and repaint
        this.revalidate();
        this.repaint();
    }

    /**
     * Changes the contrast rows' background colours and the border to a darker grey
     */
    public void applyDarkmode() {
        if (settings.getRowContrast()) {
            lName.setBackground(new Color(75, 75, 75));
            lRelease.setBackground(new Color(75, 75, 75));
        }
        this.setBorder(BorderFactory.createLineBorder(new Color(75, 75, 75)));
    }

    /**
     * Changes the contrast rows' background colours and the border to a lighter grey
     */
    public void applyLightmode() {
        if (settings.getRowContrast()) {
            lName.setBackground(new Color(200, 200, 200));
            lRelease.setBackground(new Color(200, 200, 200));
        }
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }
}