import java.awt.Image;

import javax.swing.ImageIcon;

public class SaveDemonAttack extends EnemyAttack {
    private static Image image = new ImageIcon("src/images/bulletSaveDemon.png").getImage();

    public SaveDemonAttack(int x, int y, double angle) {
        super(x, y, angle);
        setImage(image);
    }

    public SaveDemonAttack(EnemyAttack enemyAttack) {
        this(enemyAttack.getX(), enemyAttack.getY(), enemyAttack.getAngle());
    }

}