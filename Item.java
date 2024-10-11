import java.awt.Image;

import javax.swing.ImageIcon;

public class Item implements interactable {
    private Image image;
    private int width;
    private int height;
    private int x, y;

    public Item(int x, int y, String path) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(path).getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    public Image getImage() { return image; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
}