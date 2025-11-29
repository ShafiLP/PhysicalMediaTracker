import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JTextField;

/**
 * Class of PlaceholderTextField
 * Extended object of JTextField that displays a grey placeholder text when content is empty
 */
public class PlaceholderTextField extends JTextField {
    private String placeholder;

    /**
     * Creates a new JTextField that displays a placeholder text when empty
     * @param pPlaceholder Text to display when text field contains no user input
     */
    public PlaceholderTextField(String pPlaceholder) {
        placeholder = pPlaceholder;
    }

    /**
     * Changes placeholder text of text field
     * @param pPlaceholder New placeholder text to display when text field contains no user input
     */
    public void setPlaceholder(String pPlaceholder) {
        placeholder = pPlaceholder;
        repaint();
    }

    // Override paintComponent() so text field displays the placeholder text automatically
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(placeholder == null || placeholder.isEmpty()) return;
        if (!getText().isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.GRAY);
        Insets ins = getInsets();
        FontMetrics fm = g2.getFontMetrics();
        int x = ins.left + 2;
        int y = (getHeight() - fm.getHeight()) / 2 +fm.getAscent();
        g2.drawString(placeholder, x, y);
        g2.dispose();
    }
}
