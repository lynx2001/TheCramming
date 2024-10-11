import java.awt.Image;

import javax.swing.ImageIcon;

public class MidtermsMiddleBoss extends Enemy{
    private static Image image = new ImageIcon("src/images/midTerms.png").getImage();
    
    public MidtermsMiddleBoss(int x, int y) {
        super(x, y);
        setImage(image);
        this.hp = 150;
    }
}