import java.awt.Image;

import javax.swing.ImageIcon;

public class FinalsMiddleBossAttack extends EnemyAttack {
    private static Image image = new ImageIcon("src/images/bulletFinals.png").getImage();

    public FinalsMiddleBossAttack(int x, int y, double angle) {
        super(x, y, angle);
        setImage(image);
        setAttack(15);
    }

    public FinalsMiddleBossAttack(EnemyAttack enemyAttack) {
        this(enemyAttack.getX(), enemyAttack.getY(), enemyAttack.getAngle());
    }
}