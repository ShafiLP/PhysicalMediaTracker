import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JTextField;

public class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField(String pPlaceholder) {
        placeholder = pPlaceholder;
    }

    public void setPlaceholder(String pPlaceholder) {
        placeholder = pPlaceholder;
        repaint();
    }

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
