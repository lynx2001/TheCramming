import java.awt.Image;

public class EnemyAttack implements interactable{
    private Image image;
    private int width;
    private int height;
    private int SPEED = 7;

    private int x, y;
    private int attack = 5;
    private double angle;

    public EnemyAttack(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    protected void setImage(Image image) {
        this.image = image;
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
    }

    public void fire() {
        this.x += (int) (SPEED * Math.cos(angle));
        this.y += (int) (SPEED * Math.sin(angle));
    }

    public Image getImage() { return image; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getAttack() { return attack; }
    public double getAngle() { return angle; }

    protected void setAttack(int attack) {
        this.attack = attack;
    }
}