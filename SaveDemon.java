import java.awt.Image;

import javax.swing.ImageIcon;

public class SaveDemon extends Enemy{
    private static Image image = new ImageIcon("src/images/SaveDemon.png").getImage();
    
    public SaveDemon(int x, int y) {
        super(x, y);
        setImage(image);
    }

    public SaveDemon(Enemy enemy) {
        super(enemy.getX(), enemy.getY());
        setImage(image);
    }
}