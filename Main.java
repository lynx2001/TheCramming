import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;

    static {
        // Retrieve the screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = screenSize.width;
        SCREEN_HEIGHT = screenSize.height;
    }

    public static void main(String[] args) {
        new ShootingGame();
    }
}