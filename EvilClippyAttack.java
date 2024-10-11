import java.awt.Image;

import javax.swing.ImageIcon;

public class EvilClippyAttack extends EnemyAttack {
    private static Image image = new ImageIcon("src/images/bulletEvilClippy.png").getImage();

    public EvilClippyAttack(int x, int y, double angle) {
        super(x, y, angle);
        setImage(image);
    }

    public EvilClippyAttack(EnemyAttack enemyAttack) {
        this(enemyAttack.getX(), enemyAttack.getY(), enemyAttack.getAngle());
    }
}