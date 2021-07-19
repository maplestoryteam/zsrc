package gui.ͼƬ.gui1;

import javax.swing.*;
import java.awt.*;

public class ZPanel extends JPanel {

    private static final long serialVersionUID = 6702278957072713279L;
    private Icon wallpaper;

    public ZPanel() {
    }

    protected void paintComponent(Graphics g) {
        if (null != wallpaper) {
            processBackground(g);
        }
    }

    public void setBackground(Icon wallpaper) {
        this.wallpaper = wallpaper;
        this.repaint();
    }

    private void processBackground(Graphics g) {
        ImageIcon icon = (ImageIcon) wallpaper;
        Image image = icon.getImage();
        int cw = getWidth();
        int ch = getHeight();
        int iw = image.getWidth(this);
        int ih = image.getHeight(this);
        int x = 0;
        int y = 0;
        while (y <= ch) {
            g.drawImage(image, x, y, this);
            x += iw;
            if (x >= cw) {
                x = 0;
                y += ih;
            }
        }
    }
}