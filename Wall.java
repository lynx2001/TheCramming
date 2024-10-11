import java.awt.Image;

import javax.swing.ImageIcon;

public class Wall implements interactable{
    private Image image;
    private int width;
    private int height;
    String path;

    private int x, y;

    public Wall(String path) {
        this.path = path;

        image = new ImageIcon(path).getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    public Wall(int x, int y, String path) {
        this.x = x;
        this.y = y;
        this.path = path;

        image = new ImageIcon(path).getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    public Image getImage() { return image; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}