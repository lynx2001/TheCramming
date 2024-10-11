import java.awt.Image;

public class Enemy implements interactable {
    private Image image;
    private int width;
    private int height;

    private int x, y;
    protected int hp = 10;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setImage(Image image) {
        this.image = image;
        this.width = this.image.getWidth(null);
        this.height = this.image.getHeight(null);
    }

    public Image getImage() { return image; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHP() { return hp; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setHP(int val) { hp = val; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
}