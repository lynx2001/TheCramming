import java.awt.Image;

import javax.swing.ImageIcon;

public class MidtermsMiddleBossAttack extends EnemyAttack {
    private static Image image = new ImageIcon("src/images/bulletMidterms.png").getImage();

    public MidtermsMiddleBossAttack(int x, int y, double angle) {
        super(x, y, angle);
        setImage(image);
        setAttack(15);
    }

    public MidtermsMiddleBossAttack(EnemyAttack enemyAttack) {
        this(enemyAttack.getX(), enemyAttack.getY(), enemyAttack.getAngle());
    }
}