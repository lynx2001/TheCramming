import java.awt.Image;

import java.util.Random;

import javax.swing.ImageIcon;

public class FinalBossAttack extends EnemyAttack {
    private static Image image = new ImageIcon("src/images/bulletEvilClippy4X.png").getImage();
    private static Image image2 = new ImageIcon("src/images/bulletSaveDemon4X.png").getImage();
    private static Image image3 = new ImageIcon("src/images/bulletFinals2X.png").getImage();
    private static Image image4 = new ImageIcon("src/images/bulletMidterms2X.png").getImage();

    public FinalBossAttack(int x, int y, double angle) {
        super(x, y, angle);
        Random rand = new Random();
        int random = rand.nextInt(4);

        switch (random) {
            case 0:
                setImage(image);
                break;
            case 1:
                setImage(image2);
                break;
            case 2:
                setImage(image3);
                break;
            default:
                setImage(image4);
                break;
        }
        setAttack(20);
    }

    public FinalBossAttack(EnemyAttack enemyAttack) {
        this(enemyAttack.getX(), enemyAttack.getY(), enemyAttack.getAngle());
    }
}