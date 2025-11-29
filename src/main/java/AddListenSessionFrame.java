import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

public class AddListenSessionFrame extends JFrame {
    public AddListenSessionFrame(Pmt pPmt, Album pAlbum) {
        this.setTitle("Hör-Session hinzufügen");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // Add tracks with check boxes
        int[] currentIdx = {1};
        if (pAlbum.containsNulltrack()) currentIdx[0] = 0;
        JPanel panTracks = new JPanel(new GridBagLayout());

        GridBagConstraints gbcCb = new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0.1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }};

        GridBagConstraints gbcIdx = new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }};

        GridBagConstraints gbcTrack = new GridBagConstraints() {{
            gridx = 2;
            gridy = 0;
            weightx = 0.8;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }};

        LinkedList<JCheckBox> llCheckBoxes = new LinkedList<>();
        for (int i = 0; i < pAlbum.getTrackList().size(); i++) {
            // CheckBox
            llCheckBoxes.addLast(new JCheckBox("", true));
            llCheckBoxes.getLast().setEnabled(false);
            panTracks.add(llCheckBoxes.getLast(), gbcCb);
            gbcCb.gridy++;

            // Index
            panTracks.add(new JLabel(currentIdx[0] + ""), gbcIdx);
            gbcIdx.gridy++;

            // Track name
            panTracks.add(new JLabel(pAlbum.getTrackList().get(i).getTrackName()), gbcTrack);
            gbcTrack.gridy++;

            currentIdx[0]++;
        }
        //! TEST: WITH JTABLE INSTEAD OF MULTIPLE SWING OBJECTS
        /*
        String[] columnNames = {"Nr.", "Trackname", "Gehört"};
        Object[][] data = {
                {1, "Track Eins", 3},
                {2, "Track Zwei", 3},
                {3, "Track Drei", 4}
        };
        JTable table = new JTable(data, columnNames);*/

        // Wrap panTracks so its content stays top-aligned inside the scroll pane
        JPanel panTracksWrapper = new JPanel(new BorderLayout());
        panTracksWrapper.add(panTracks, BorderLayout.NORTH);

        // Add track panel to ScrollPane
        JScrollPane spTracks = new JScrollPane(panTracksWrapper);
        spTracks.getVerticalScrollBar().setUnitIncrement(16);

        // CheckBox if album was fully listened
        JCheckBox cbFullListen = new JCheckBox("Komplett angehört", true);
        cbFullListen.addActionListener(_ -> {
            if (cbFullListen.isSelected()) {
                for (JCheckBox llCheckBox : llCheckBoxes) {
                    llCheckBox.setSelected(true);
                    llCheckBox.setEnabled(false);
                }
            } else {
                for (JCheckBox llCheckBox : llCheckBoxes) {
                    llCheckBox.setEnabled(true);
                }
            }
        });

        // Upper panel
        JPanel panUpper = new JPanel(new GridBagLayout());
        panUpper.add(new JLabel("Wähle alle Tracks aus, die du gehört hast!", SwingConstants.CENTER), new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        panUpper.add(cbFullListen, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        this.add(panUpper, BorderLayout.NORTH);

        // Center panel 
        JPanel panCenter = new JPanel(new GridBagLayout());
        panCenter.add(spTracks, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            weighty = 1.0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }});

        // Information about date
        JPanel panDate = new JPanel(new GridLayout(2, 1));
        PlaceholderTextField tfDate = new PlaceholderTextField("DD.MM.YYYY");
        tfDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        tfDate.setEnabled(false);
        JCheckBox cbUseLocalDate = new JCheckBox("Aktuelles Datum", true);
        cbUseLocalDate.addActionListener(_ -> {
            if (cbUseLocalDate.isSelected()) {
                tfDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                tfDate.setEnabled(false);
            } else {
                tfDate.setText("");
                tfDate.setEnabled(true);
            }
        });
        panDate.add(tfDate);
        panDate.add(cbUseLocalDate);
        panCenter.add(panDate, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            anchor  = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        // Information about time
        JPanel panTime = new  JPanel(new GridLayout(2, 1));
        PlaceholderTextField tfTime = new PlaceholderTextField("hh:mm");
        tfTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        tfTime.setEnabled(false);
        JCheckBox cbUseLocalTime = new JCheckBox("Aktuelle Uhrzeit", true);
        cbUseLocalTime.addActionListener(_ -> {
            if (cbUseLocalTime.isSelected()) {
                tfTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                tfTime.setEnabled(false);
            } else {
                tfTime.setText("");
                tfTime.setEnabled(true);
            }
        });
        panTime.add(tfTime);
        panTime.add(cbUseLocalTime);
        panCenter.add(panTime, new GridBagConstraints() {{
            gridx = 0;
            gridy = 2;
            weightx = 1.0;
            anchor  = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        // Add tracks to frame
        this.add(panCenter, BorderLayout.CENTER);

        // Confirm & Return buttons
        JPanel panButtons = new JPanel();
        panButtons.setLayout(new GridBagLayout());

        JButton bReturn = new JButton("X");
        bReturn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        bReturn.addActionListener(e -> {
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
        bConfirm.addActionListener(e -> {
            Album editedAlbum = pAlbum;
            editedAlbum.incraseListenCount();
            for (int i = 0; i < editedAlbum.getTrackList().size(); i++) {
                if (llCheckBoxes.get(i).isSelected()) editedAlbum.getTrackList().get(i).incraseListenCount();
            }
            editedAlbum.addListenTime(tfDate.getText(), tfTime.getText());
            pPmt.editAlbum(pAlbum, editedAlbum);
            this.dispose();
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
}
