import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Image;

import com.formdev.flatlaf.FlatClientProperties;

public class TrackEntry {
    private int index;
    private JLabel lIndex;
    private JTextField tfName;
    private JButton bDelete;

    /**
     * Empty constructor
     */
    TrackEntry() { }

    TrackEntry(int index, JTextField tfName) {
        this.index = index;
        this.tfName = tfName;
        lIndex = new JLabel(index + "");


        // Delete button
        ImageIcon icon = new ImageIcon("media\\icons\\delete.png");
        Image img = icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        bDelete = new JButton(new ImageIcon(img));
        bDelete.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
    }

    public void setIndex(int index) {
        this.index = index;
        lIndex.setText(index + "");
    }

    public int getIndex() {
        return index;
    }

    public void setTextField(JTextField tfName) {
        this.tfName = tfName;
    }

    public JTextField getTextField() {
        return tfName;
    }

    public JLabel getIndexLabel() {
        return lIndex;
    }

    public JButton getDeleteButton() {
        return bDelete;
    }
}