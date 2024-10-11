import java.awt.Image;

import javax.swing.ImageIcon;

public class FinalBoss extends Enemy {
    private Image image = new ImageIcon("src/images/FinalBossHP100.png").getImage();
    private Image image2 = new ImageIcon("src/images/FinalBossHP80.png").getImage();
    private Image image3 = new ImageIcon("src/images/FinalBossHP60.png").getImage();
    private Image image4 = new ImageIcon("src/images/FinalBossHP40.png").getImage();
    private Image image5 = new ImageIcon("src/images/FinalBossHP20.png").getImage();
    private Image image6 = new ImageIcon("src/images/FinalBossHP0.png").getImage();

    public FinalBoss() {
        super(0, 0);
        setImage(image);
        this.hp = 300;
    }

    @Override
    public int getHP() {
        updateImageBasedOnHp();
        return hp;
    }

    private void updateImageBasedOnHp() {
        if (this.hp >= 240) { setImage(image); }
        else if (this.hp >= 180) { setImage(image2);}
        else if (this.hp >= 120) { setImage(image3);}
        else if (this.hp >= 60) { setImage(image4);}
        else if (this.hp >= 0) { setImage(image5); }
        else setImage(image6);
    }
}