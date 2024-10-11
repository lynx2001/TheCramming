import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ShootingGame extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;

    private Image mainScreen;
    private Image helpScreen;
    private Image creditsScreen;
    private Image gameScreen;
    private Image gameClearScreen;
    private Image gameOverScreen;

    private boolean isMainScreen, isHelpscreen, isCreditsScreen, isGameScreen, isGameClearScreen, isGameOverScreen;

    private Game game = new Game();

    private Audio backgroundMusic;

    public ShootingGame() {
        setTitle("The Cramming");
        setUndecorated(true);
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);

        init();
    }

    private void init() {
        mainScreen = new ImageIcon("src/images/title.png").getImage();
        helpScreen = new ImageIcon("src/images/help.png").getImage();
        creditsScreen = new ImageIcon("src/images/credits.png").getImage();
        gameScreen = new ImageIcon("src/images/gameBackground.png").getImage();
        gameClearScreen = new ImageIcon("src/images/gameCleared.png").getImage();
        gameOverScreen = new ImageIcon("src/images/gameOver.png").getImage();

        isMainScreen = true;
        isHelpscreen = false;
        isGameScreen = false;
        isGameOverScreen = false;

        backgroundMusic = new Audio("src/audio/SpaceshipHangar.wav", true);
        backgroundMusic.start();

        addKeyListener(new KeyListener());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    game.setShooting(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    game.setShooting(false);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                game.setMousePosition(e.getX(), e.getY());
            }
        });
    }

    private void gameStart() {
        isMainScreen = false;
        isHelpscreen = false;
        backgroundMusic.stop();
        isGameScreen = true;
        game.start();
    }

    public void paint(Graphics g) {
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        try {
            screenDraw(screenGraphic);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g.drawImage(bufferImage, 0, 0, null);
    }

    public void screenDraw(Graphics g) throws IOException {
        if (isMainScreen && mainScreen != null) {
            g.drawImage(mainScreen, 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, null);
        }
        if (isHelpscreen && helpScreen != null) {
            g.drawImage(helpScreen, 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, null);
        }
        if (isCreditsScreen && creditsScreen != null) {
            g.drawImage(creditsScreen, 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, null);
        }
        if (isGameScreen && gameScreen != null) {
            g.drawImage(gameScreen, 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, null);
            game.gameDraw(g);
        }
        if(game.isOver()) {
            if(game.getCleared()){
                isGameClearScreen = true;
                isGameScreen = false;
                }
            else {
                isGameOverScreen = true;
                isGameScreen = false;
            }
        }
        if(isGameClearScreen && gameClearScreen != null) {
            g.drawImage(gameClearScreen, 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, null);
            game.gameOverDraw(g);
        }
        if(isGameOverScreen && gameOverScreen != null) {
            g.drawImage(gameOverScreen, 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, null);
            // 아래 코드 활용 시
            // Game class에서 gameOverDraw(g) 호출 코드 삭제 요망
            game.gameOverDraw(g);
        }
        this.repaint();
    }
    
    class KeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.setUp(true);
                    break;
                case KeyEvent.VK_S:
                    game.setDown(true);
                    break;
                case KeyEvent.VK_A:
                    game.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    game.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(true);
                    break;
                case KeyEvent.VK_ENTER:
                    if (isMainScreen || isHelpscreen) gameStart();
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_R:
                    game.setPause(false);
                    break;
                case KeyEvent.VK_C:
                    if(isMainScreen) {
                        isCreditsScreen = true;
                        isMainScreen = false;
                    }
                    else if (isCreditsScreen) {
                        isCreditsScreen = false;
                        isMainScreen = true;
                    }
                    break;
                case KeyEvent.VK_H:
                    if(isMainScreen) {
                        isHelpscreen = true;
                        isMainScreen = false;
                    }
                    else if (isHelpscreen) {
                        isHelpscreen = false;
                        isMainScreen = true;
                    }
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.setUp(false);
                    break;
                case KeyEvent.VK_S:
                    game.setDown(false);
                    break;
                case KeyEvent.VK_A:
                    game.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    game.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(false);
                    break;
            }
        }
    }
}