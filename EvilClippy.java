import java.awt.Image;

import javax.swing.ImageIcon;

public class EvilClippy extends Enemy{
    private static Image image = new ImageIcon("src/images/EvilClippy.png").getImage();
    
    public EvilClippy(int x, int y) {
        super(x, y);
        setImage(image);
    }

    public EvilClippy(Enemy enemy) {
        super(enemy.getX(), enemy.getY());
        setImage(image);
    }
}