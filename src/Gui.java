import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;

import com.formdev.flatlaf.FlatLightLaf;

public class Gui extends JFrame {
    private final Pmt PMT;
    private JPanel panAlbums;

    public Gui(Pmt pPmt) {
        PMT = pPmt;

        // Setup FlatLaf
        FlatLightLaf.setup();
        com.formdev.flatlaf.FlatLightLaf.updateUI();

        // Setup JFrame
        this.setTitle("Physical Media Tracker");
        this.setSize(510, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // JButton for adding new album
        JButton bAddAlbum = new JButton("Album hinzufügen");
        bAddAlbum.addActionListener(_ -> {
            new AlbumCreateFrame(PMT);
        });
        this.add(bAddAlbum, BorderLayout.SOUTH);

        // JPanel with search bar and sorter
        JPanel panSearchSort = new JPanel(new GridBagLayout());

        PlaceholderTextField tfSearchBar = new PlaceholderTextField("Suchen");
        tfSearchBar.addActionListener(_ -> {
            // Search for matches when changes are made
            displayAlbumList(PMT.searchForAlbum(tfSearchBar.getText()));
        });
        panSearchSort.add(tfSearchBar, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.8;
            weighty = 1.0;
            insets = new Insets(5, 5, 5, 5);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        JComboBox<String> cbSortBy = new JComboBox<>();
        cbSortBy.addItem("Kürzlich hinzugefügt");
        cbSortBy.addItem("Name");
        cbSortBy.addItem("Künstler");
        cbSortBy.addItem("Erscheinungsjahr");
        cbSortBy.addActionListener(_ -> {
            String selected = (String) cbSortBy.getSelectedItem();

            switch (selected) {
                case "Kürzlich hinzugefügt":
                    updateAlbums();
                    break;
                case "Name":
                    displayAlbumList(PMT.sortByName());
                    break;
                case "Künstler":
                    displayAlbumList(PMT.sortByArtist());
                    break;
                case "Erscheinungsjahr":
                    displayAlbumList(PMT.sortByRelease());
                    break;
                default:
                    // Error
                    System.out.println("Items couldn't be sorted.");
                    break;
            }
        });
        panSearchSort.add(cbSortBy, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.2;
            weighty = 1.0;
            insets = new Insets(5, 0, 5, 5);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        this.add(panSearchSort, BorderLayout.NORTH);
       
        // JPanel with albums
        panAlbums = new JPanel(new WrapLayout(FlowLayout.LEFT, 10, 10)) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = getParent() != null ? getParent().getWidth() : d.width;
                return d;
            }
        };

        // Add albums from JSON save file
        LinkedList<Album> llAlbums = PMT.getAlbumList();
        for (int i = llAlbums.size() - 1; i >= 0; i--) {
            Album idxAlbum = llAlbums.get(i);
            panAlbums.add(new AlbumComponent(idxAlbum, PMT));
        }

        // JScrollPane for scrollable album display
        JScrollPane scrollPane = new JScrollPane(panAlbums);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().addChangeListener(_ -> {
            panAlbums.revalidate();
        });
        
        this.add(scrollPane);

        this.setVisible(true);
    }

    /**
     * Removes all AlbumComponents from albums panel and adds them again
     * Called when changes have been made
     */
    public void updateAlbums() {
        panAlbums.removeAll();
        LinkedList<Album> llAlbums = PMT.getAlbumList();
        for (int i = llAlbums.size() - 1; i >= 0; i--) {
            Album idxAlbum = llAlbums.get(i);
            panAlbums.add(new AlbumComponent(idxAlbum, PMT));
        }
        panAlbums.revalidate();
        panAlbums.repaint();
        System.out.println("GUI updated."); // DEBUG
    }

    public void displayAlbumList(LinkedList<Album> pAlbums) {
        panAlbums.removeAll();
        for (int i = 0; i < pAlbums.size(); i++) {
            Album idxAlbum = pAlbums.get(i);
            panAlbums.add(new AlbumComponent(idxAlbum, PMT));
        }
        panAlbums.revalidate();
        panAlbums.repaint();
        System.out.println("GUI updated."); // DEBUG
    }
}
