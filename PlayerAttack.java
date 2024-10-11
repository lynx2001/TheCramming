import java.awt.Image;

import javax.swing.ImageIcon;

public class PlayerAttack implements interactable{
    private Image image = new ImageIcon("src/images/bulletProtag.png").getImage();
    private int width = image.getWidth(null);
    private int height = image.getHeight(null);
    
    private int speed = 10;
    private int x, y;
    private int attack = 5;
    private double angle;

    public PlayerAttack(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void fire() {
        this.x += (int) (speed * Math.cos(angle));
        this.y += (int) (speed * Math.sin(angle));
    }

    public void attackUp() { this.attack = 10; }
    
    public void attackReset() {this.attack = 5;}

    public Image getImage() { return image; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getAttack() { return attack; }
}