import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
                for (int i = 0; i < llCheckBoxes.size(); i++) {
                    llCheckBoxes.get(i).setSelected(true);
                    llCheckBoxes.get(i).setEnabled(false);
                }
            } else {
                for (int i = 0; i < llCheckBoxes.size(); i++) {
                    llCheckBoxes.get(i).setEnabled(true);
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
            anchor = GridBagConstraints.NORTH;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Information about time
        // TODO: open calendar
        JPanel panDate = new JPanel(new GridLayout(2, 1));
        JTextField tfDate = new JTextField(LocalDate.now().toString());
        tfDate.setEnabled(false);
        JCheckBox cbUseLocalDate = new JCheckBox("Aktuelles Datum", true);
        cbUseLocalDate.addActionListener(_ -> {
            if (cbUseLocalDate.isSelected()) {
                tfDate.setText(LocalDate.now().toString());
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
            fill = GridBagConstraints.HORIZONTAL;
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
