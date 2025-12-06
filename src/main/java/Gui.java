import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class Gui extends JFrame {
    private final Pmt PMT;
    private final Settings settings;
    private final JPanel panAlbums;

    public Gui(Pmt pPmt, Settings pSettings) {
        PMT = pPmt;
        settings = pSettings;

        // Setup FlatLaf
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", settings.getAccentColor()));
        if(!settings.isDarkmode()) {
            FlatLightLaf.setup();
            FlatLightLaf.updateUI();
        } else {
            FlatDarkLaf.setup();
            FlatDarkLaf.updateUI();
        }

        // Setup JFrame
        this.setTitle("Physical Media Tracker");
        this.setSize(510, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Add JMenuBar
        initializeJMenuBar();

        // JPanel with search bar and sorter
        JPanel panSearchSort = new JPanel(new GridBagLayout());

        // Search bar
        PlaceholderTextField tfSearchBar = new PlaceholderTextField("Suchen");
        tfSearchBar.addActionListener(e -> {
            // Search for matches when changes are made
            displayAlbumList(PMT.searchForAlbum(tfSearchBar.getText()));
        });
        panSearchSort.add(tfSearchBar, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.6;
            weighty = 1.0;
            insets = new Insets(5, 5, 5, 5);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Sorter
        JComboBox<String> cbSortBy = new JComboBox<>();
        JButton bSortOrder = new JButton("↑");
        cbSortBy.addItem("Kürzlich hinzugefügt");
        cbSortBy.addItem("Name");
        cbSortBy.addItem("Künstler");
        cbSortBy.addItem("Erscheinungsjahr");
        cbSortBy.addItem("Zuletzt gehört");
        cbSortBy.addActionListener(e -> {
            String selected = (String) cbSortBy.getSelectedItem();

            switch (selected) {
                case "Kürzlich hinzugefügt" -> displayAlbumList(PMT.sortAlbums(SortType.LAST_ADDED));
                case "Name" -> displayAlbumList(PMT.sortAlbums(SortType.NAME));
                case "Künstler" -> displayAlbumList(PMT.sortAlbums(SortType.ARTIST));
                case "Erscheinungsjahr" -> displayAlbumList(PMT.sortAlbums(SortType.YEAR));
                case "Zuletzt gehört" -> displayAlbumList(PMT.sortAlbums(SortType.LAST_LISTENED));
                case null, default -> System.out.println("Items couldn't be sorted."); //! Error
            }
        });
        panSearchSort.add(cbSortBy, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.2;
            weighty = 1.0;
            insets = new Insets(5, 0, 5, 0);
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Sort order
        bSortOrder.addActionListener(_ -> {
            if (bSortOrder.getText().equals("↑")) {
                bSortOrder.setText("↓");
                displayAlbumList(PMT.sortAlbums(SortOrder.DESCENDING));
            } else {
                bSortOrder.setText("↑");
                displayAlbumList(PMT.sortAlbums(SortOrder.ASCENDING));
            }
        });
        panSearchSort.add(bSortOrder, new GridBagConstraints() {{
            gridx = 2;
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
        LinkedList<Album> llAlbums = PMT.getAlbumList().reversed();
        for (int i = llAlbums.size() - 1; i >= 0; i--) {
            Album idxAlbum = llAlbums.get(i);
            panAlbums.add(new AlbumComponent(idxAlbum, PMT, settings));
        }

        // JScrollPane for scrollable album display
        JScrollPane scrollPane = new JScrollPane(panAlbums);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().addChangeListener(e -> {
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
            panAlbums.add(new AlbumComponent(idxAlbum, PMT, settings));
        }
        panAlbums.revalidate();
        panAlbums.repaint();
        System.out.println("GUI updated."); // DEBUG
    }

    /**
     * Removes all AlbumComponents from albums panel and adds another list of albums instead
     * Called when sorting albums
     * @param pAlbums New list of albums to display on GUI
     */
    public void displayAlbumList(LinkedList<Album> pAlbums) {
        panAlbums.removeAll();
        for (Album idxAlbum : pAlbums) {
            panAlbums.add(new AlbumComponent(idxAlbum, PMT, settings));
        }
        panAlbums.revalidate();
        panAlbums.repaint();
        System.out.println("GUI updated."); // DEBUG
    }

    public void setUiScale(int pUiScale) {
        for (Component c : panAlbums.getComponents()) {
            if (c instanceof AlbumComponent) ((AlbumComponent) c).setUiScale(pUiScale);
        }
        this.revalidate();
        this.repaint();
    }

    public void applyDarkmode() {
        for (Component c : panAlbums.getComponents()) {
            if (c instanceof AlbumComponent) ((AlbumComponent) c).applyDarkmode();
        }
    }

    public void applyLightmode() {
        for (Component c : panAlbums.getComponents()) {
            if (c instanceof AlbumComponent) ((AlbumComponent) c).applyLightmode();
        }
    }

    /**
     * Adds JMenuBar to JFrame
     */
    private void initializeJMenuBar() {
        // Menu bar at the top
        JMenuBar menuBar = new JMenuBar();

        // * Profile options
        JMenu menuFile = new JMenu("Profil");

        // Item to create a new save file
        JMenuItem itemNewFile = new JMenuItem("Neues Profil");
        itemNewFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.SHIFT_MASK));
        itemNewFile.addActionListener(e -> {
            // TODO
        });
        menuFile.add(itemNewFile);

        // Item to open a save file
        JMenuItem itemOpenFile = new JMenuItem("Profil öffnen");
        itemOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.SHIFT_MASK));
        itemOpenFile.addActionListener(e -> {
            new ProfileSelectionFrame(settings);
        });
        menuFile.add(itemOpenFile);

        // Item to save current file
        JMenuItem itemSaveFile = new JMenuItem("Profil speichern");
        itemSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
        itemSaveFile.addActionListener(e -> {
            // TODO
        });
        menuFile.add(itemSaveFile);

        menuBar.add(menuFile);

        // * Album options
        JMenu menuAlbum = new JMenu("Album");

        // Item to create new album
        JMenuItem itemNewAlbum = new JMenuItem("Neues Album");
        itemNewAlbum.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK));
        itemNewAlbum.addActionListener(e -> {
            new AlbumCreateFrame(PMT, settings);
        });
        menuAlbum.add(itemNewAlbum);

        // Item to sort albums
        JMenuItem itemSort = new JMenuItem("Sortieren");
        itemSort.addActionListener(e -> {
           // TODO
        });
        menuAlbum.add(itemSort);

        menuBar.add(menuAlbum);

        // * Settings
        JMenu menuSettings = new JMenu("Einstellungen");

        // Item to open display settings
        JMenuItem itemDisplay = new JMenuItem("Anzeige");
        itemDisplay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.SHIFT_MASK));
        itemDisplay.addActionListener(e -> {
            new DisplaySettingsFrame(this, settings);
        });
        menuSettings.add(itemDisplay);

        // Item to open general settings
        JMenuItem itemSettings = new JMenuItem("Einstellungen");
        itemSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.SHIFT_MASK));
        itemSettings.addActionListener(e -> {
            new GeneralSettingsFrame(this, settings);
        });
        menuSettings.add(itemSettings);

        menuBar.add(menuSettings);

        // Add JMenuBar to JFrame
        this.setJMenuBar(menuBar);
    }
}
