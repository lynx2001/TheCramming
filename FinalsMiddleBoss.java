import java.awt.Image;

import javax.swing.ImageIcon;

public class FinalsMiddleBoss extends Enemy {
    private static Image image = new ImageIcon("src/images/finals.png").getImage();
    
    public FinalsMiddleBoss(int x, int y) {
        super(x, y);
        setImage(image);
        this.hp = 150;
    }
}