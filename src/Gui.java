import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

public class Gui extends JFrame {
    private final Pmt PMT;

    public Gui(Pmt pPmt) {
        PMT = pPmt;

        this.setTitle("Physical Media Tracker");
        this.setSize(500, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // JButton for adding new album
        JButton bAddAlbum = new JButton("Album hinzufügen");
        bAddAlbum.addActionListener(_ -> {
            // TODO
        });
        this.add(bAddAlbum, BorderLayout.SOUTH);

        // JScrollPane for scrollable album display
        JScrollPane scrollPane = new JScrollPane();

        // JPanel with albums
        JPanel panAlbums = new JPanel();
        scrollPane.setViewportView(panAlbums);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }
}

class AlbumComponent {
    // TODO
}

