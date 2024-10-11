import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {
    private int delay = 20;
    private long pretime;
    private int cnt;
    private int score;
    private long lastPotionTime;
    private long lastShoeTime;
    private long shoePickupTime;
    private long lastSniperTime;
    private long sniperPickupTime;
    private static final int POTION_INTERVAL = 10000; // 10 seconds in milliseconds
    private static final int SHOE_INTERVAL = 20000; // 20 sec
    private static final int SNIPER_INTERVAL = 30000; // 30 sec
    private static final int SHOE_DURATION = 5000; // 5 sec
    private static final int SNIPER_DURATION = 5000; // 5 sec

    private Image stageTransit = new ImageIcon("src/images/stageChange.png").getImage();
    private boolean stageInTransit = false;
    private boolean isPaused = false;

    private Image player = new ImageIcon("src/images/CSEProtag.png").getImage();
    private Image playerDefault = new ImageIcon("src/images/CSEProtag.png").getImage();
    private Image playerLeft = new ImageIcon("src/images/playerLeft.png").getImage();
    private Image playerRight = new ImageIcon("src/images/playerRight.png").getImage();

    public static int playerX;
    public static int playerY;
    private int playerWidth = player.getWidth(null);
    private int playerHeight = player.getHeight(null);
    private int playerSpeed = 10;
    private int playerHp = 50;

    private boolean up, down, left, right, shooting;
    private boolean isOver;
    private boolean gameCleared = false;
    private boolean rankDisplayed; // rank를 display를 위한 flag

    private int stage;
    private boolean middleBossIsMaded = false;
    private boolean finalBossIsMaded = false;
    private boolean wallIsMaded = false;

    private ArrayList<Wall> wallList = new ArrayList<>();
    private ArrayList<PlayerAttack> playerAttackList = new ArrayList<>();
    private ArrayList<Enemy> enemyList = new ArrayList<>();
    private ArrayList<EnemyAttack> enemyAttackList = new ArrayList<>();
    private Potion potion;
    private Shoe shoe;
    private Sniper sniper;

    private Wall wall;
    private PlayerAttack playerAttack;
    private Enemy enemy;
    private EnemyAttack enemyAttack;

    private Audio backgroundMusic;
    private Audio hitSound;

    private Point mousePosition = new Point(0, 0);

    private Rank rank = null;

    public void setMousePosition(int x, int y) {
        this.mousePosition.setLocation(x, y);
    }

    @Override
    public void run() {
        backgroundMusic = new Audio("src/audio/GoodNight.au", true);
        hitSound = new Audio("src/audio/hit.wav", false);

        reset();
        while (true) {
            while (!isOver) {
                pretime = System.currentTimeMillis();
                if (System.currentTimeMillis() - pretime < delay) {
                    try {
                        Thread.sleep(delay - System.currentTimeMillis() + pretime);
                        if (stageInTransit) { // stage가 변경되는 중일 경우
                            enemyList.clear();
                            enemyAttackList.clear();
                            playerAttackList.clear();
                            playerHp = 50;
                            playerCoordReset();
                            itemReset();
                            if (backgroundMusic != null) {
                                backgroundMusic.stop();
                            }
                            if (stage == 2) {
                                backgroundMusic = new Audio("src/audio/BeyondLimits.au", true);
                                backgroundMusic.start();
                            } else if (stage == 3) {
                                backgroundMusic = new Audio("src/audio/TheFinalBattle.wav", true);
                                backgroundMusic.start();
                            }
                            continue;
                        }

                        // stage에 따라 적의 출현 및 공격 패턴을 다르게 설정
                        if (stage == 1) enemyAppearProcess();
                        else if (stage == 2) {
                            if (score <= 3000) enemyAppearProcess();
                            else if (score > 3000 && !middleBossIsMaded) {
                                isPaused = false;
                                middleBossAppearProcess();
                                enemyAppearProcess();
                            }
                        } else if (stage == 3) finalBossAppearProcess();

                        keyProcess();
                        wallMakeProcess();
                        playerAttackProcess();
                        enemyMoveProcess();
                        enemyAttackProcess();
                        potionProcess();
                        shoeProcess();
                        sniperProcess();
                        checkStage();
                        checkGameOver();
                        cnt++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        isOver = false;
        cnt = 0;
        score = 0;
        rankDisplayed = false;
        stage = 1;
        middleBossIsMaded = false;
        finalBossIsMaded = false;

        backgroundMusic.start();

        playerCoordReset();
        playerHp = 50;
        playerAttackList.clear();
        enemyList.clear();
        enemyAttackList.clear();
        itemReset();
    }

    public void playerCoordReset() {
        playerX = (Main.SCREEN_WIDTH - playerWidth) / 2;
        playerY = (Main.SCREEN_HEIGHT - playerHeight) / 2;
    }

    public void itemReset() {
        potion = null;
        shoe = null;
        sniper = null;
        lastPotionTime = System.currentTimeMillis();
        lastShoeTime = System.currentTimeMillis();
        lastSniperTime = System.currentTimeMillis();
    }

    // 키보드 입력에 따라 player의 움직임을 처리
    private void keyProcess() {
        int oldX = playerX;
        int oldY = playerY;

        if (up && playerY - playerSpeed > 0) playerY -= playerSpeed;
        if (down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT) playerY += playerSpeed;
        if (left && playerX - playerSpeed > 0) {
            playerX -= playerSpeed;
            player = playerLeft;
        }
        if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH) {
            playerX += playerSpeed;
            player = playerRight;
        }

        if (shooting && cnt % 15 == 0) { // 발사여부 검사 & 총알을 발사하는 시간 간격 설정
            double angle = Math.atan2(mousePosition.y - (playerY + playerHeight / 2), mousePosition.x - (playerX + playerWidth / 2));
            playerAttack = new PlayerAttack(playerX + playerWidth / 2, playerY + playerHeight / 2, angle);
            playerAttackList.add(playerAttack);
        }

        if (wallCollision()) {
            playerX = oldX;
            playerY = oldY;
        }

        checkPotionCollision();
        checkShoeCollision();
        checkSniperCollision();
    }

    private void wallMakeProcess() {
        Wall referenceWall = new Wall("src/images/Columns.png");
        int x = (int) (Main.SCREEN_WIDTH / 6 - (3 / 2.0 * referenceWall.getWidth())), y = (int) (Main.SCREEN_HEIGHT / 5 - (3 / 2.0 * referenceWall.getHeight()));
        String path = "src/images/Columns.png";

        if (!wallIsMaded) { // 벽이 만들어지지 않았을 경우 벽을 한번 생성함
            for (int i = 1; i <= 30; i++) {
                Wall wall = new Wall(x, y, path);
                wallList.add(wall);
                if (i % 6 == 0) {
                    x = (int) (Main.SCREEN_WIDTH / 6 - (3 / 2.0 * wall.getWidth()));
                    y += (int) (Main.SCREEN_HEIGHT / 5 - (1 / 2.0 * wall.getHeight()));
                } else {
                    x += (int) (Main.SCREEN_WIDTH / 6 - (1 / 2.0 * wall.getWidth()));
                }
            }
            wallIsMaded = true;
        } else if (middleBossIsMaded) wallList.clear(); // 중간보스가 나타나면 벽을 제거
    }


    private boolean wallCollision() { // player와 벽의 충돌을 확인
        boolean bool = false;
        for (int i = 0; i < wallList.size(); i++) {
            wall = wallList.get(i);
            if (checkPlayerCollision(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight())) {
                bool = true;
                break;
            }
        }

        return bool;
    }


    private boolean wallCollision(int x, int y, int width, int height) {
        // x, y좌표와 크기를 파라미터로 받아 벽과 오브젝트의 충돌을 확인
        Rectangle temp = new Rectangle(x, y, width, height);
        boolean bool = false;
        for (int i = 0; i < wallList.size(); i++) {
            wall = wallList.get(i);

            Rectangle wallRect = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            if (temp.intersects(wallRect)) {
                bool = true;
                break;
            }
        }

        return bool;
    }

    private boolean checkPlayerCollision(int x, int y, int width, int height) {
        // player와 주어진 오브젝트의 충돌을 확인
        Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
        Rectangle tempRect = new Rectangle(x, y, width, height);

        return playerRect.intersects(tempRect); // 두 오브젝트의 좌표상의 교차여부를 화인
    }

    private boolean checkEnemyCollision(int x, int y, int width, int height, Enemy enemyObj) {
        // 적과 오브젝트의 충돌을 확인
        Rectangle enemyRect = new Rectangle(enemyObj.getX(), enemyObj.getY(), enemyObj.getWidth(), enemyObj.getHeight());
        Rectangle tempRect = new Rectangle(x, y, width, height);

        return enemyRect.intersects(tempRect);
    }

    private void playerAttackProcess() {
        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            playerAttack.fire(); // 처음 주어진 angle 값에 따라 이동

            for (int j = 0; j < enemyList.size(); j++) {
                enemy = enemyList.get(j);
                if (checkEnemyCollision(playerAttack.getX(), playerAttack.getY(), playerAttack.getWidth(), playerAttack.getHeight(), enemy)) {
                    // 적과 총알이 충돌 시 적의 체력을 감소시키고 총알은 사라짐
                    enemy.setHP(enemy.getHP() - playerAttack.getAttack());
                    playerAttackList.remove(playerAttack);
                }
                for (Wall wall : wallList) {
                    // 적과 벽이 충돌 시 총알은 사라짐
                    Rectangle playerAttackRect = new Rectangle(playerAttack.getX(), playerAttack.getY(), playerAttack.getWidth(), playerAttack.getHeight());
                    Rectangle wallRect = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
                    if (playerAttackRect.intersects(wallRect)) {
                        playerAttackList.remove(playerAttack);
                        break;
                    }
                }
                if (enemy.getHP() <= 0) {
                    // 적을 제거시 적의 종류에 따라 score(점수)를 획득
                    if (enemy instanceof FinalBoss) {
                        enemyList.remove(enemy);
                        score += 1000;
                    } else if (enemy instanceof MidtermsMiddleBoss || enemy instanceof FinalsMiddleBoss) {
                        enemyList.remove(enemy);
                        score += 350;
                    } else {
                        enemyList.remove(enemy);
                        score += 100;
                    }
                }
            }
        }
    }

    private void enemyAppearProcess() {
        int locaRand = cnt / 100;
        if (cnt % 100 == 0) {
            // locaRand 값에 따라 적이 출현하는 위치 설정
            if (locaRand % 3 == 0) {
                enemy = new Enemy((int) (Math.random() * (Main.SCREEN_WIDTH - 200)) + 100, 0); // North
            } else if (locaRand % 5 == 0) {
                enemy = new Enemy(Main.SCREEN_WIDTH, (int) (Math.random() * (Main.SCREEN_WIDTH - 200)) + 100); // East
            } else if (locaRand % 7 == 0) {
                enemy = new Enemy(0, (int) (Math.random() * (Main.SCREEN_WIDTH - 200)) + 100); // West
            } else {
                enemy = new Enemy((int) (Math.random() * (Main.SCREEN_WIDTH - 200)) + 100, Main.SCREEN_HEIGHT); // South
            }

            if ((int) (Math.random() * 2) == 0) {
                // SaveDemon과 EvilClippy 중에서 랜덤하게 생성
                SaveDemon enemy1 = new SaveDemon(enemy);
                enemyList.add(enemy1);
            } else if ((int) (Math.random() * 2) == 1) {
                EvilClippy enemy2 = new EvilClippy(enemy);
                enemyList.add(enemy2);
            }


        }
    }

    private void middleBossAppearProcess() {
        // 중간보스 두 마리 생성
        if (!middleBossIsMaded) {
            enemy = new MidtermsMiddleBoss(Main.SCREEN_WIDTH / 6, Main.SCREEN_HEIGHT / 6);
            enemyList.add(enemy);
            enemy = new FinalsMiddleBoss(Main.SCREEN_WIDTH / 6 * 5, Main.SCREEN_HEIGHT / 6 * 5);
            enemyList.add(enemy);

            middleBossIsMaded = true;
        }
    }

    private void finalBossAppearProcess() {
        // 최종보스 생성
        if (!finalBossIsMaded) {
            enemy = new FinalBoss();
            enemyList.add(enemy);

            finalBossIsMaded = true;
        }
    }

    private void enemyAttackProcess() {
        if (cnt % 50 == 0) {
            // EvilClippy와 SaveDemon의 공격 패턴
            for (int i = 0; i < enemyList.size(); i++) {
                enemy = enemyList.get(i);
                double angle = Math.atan2(playerY + playerHeight / 2 - (enemy.getY() + enemy.getHeight() / 2), playerX + playerWidth / 2 - (enemy.getX() + enemy.getWidth() / 2));
                enemyAttack = new EnemyAttack(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2, angle);

                if (enemy instanceof EvilClippy) {
                    EvilClippyAttack evilClippyAttack = new EvilClippyAttack(enemyAttack);
                    enemyAttackList.add(evilClippyAttack);
                } else if (enemy instanceof SaveDemon) {
                    SaveDemonAttack saveDemonAttack = new SaveDemonAttack(enemyAttack);
                    enemyAttackList.add(saveDemonAttack);
                }
            }
        }
        if ((cnt % 100 / 25) < 1 && cnt % 10 == 0) {
            // 중간보스의 공격패턴
            // 한 번 공격시 3발의 총알을 연속적으로 발사
            for (int i = 0; i < enemyList.size(); i++) {
                enemy = enemyList.get(i);
                double angle = Math.atan2(playerY + playerHeight / 2 - (enemy.getY() + enemy.getHeight() / 2), playerX + playerWidth / 2 - (enemy.getX() + enemy.getWidth() / 2));
                enemyAttack = new EnemyAttack(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2, angle);

                if (enemy instanceof MidtermsMiddleBoss) {
                    MidtermsMiddleBossAttack midtermsMiddleBossAttack = new MidtermsMiddleBossAttack(enemyAttack);
                    enemyAttackList.add(midtermsMiddleBossAttack);
                } else if (enemy instanceof FinalsMiddleBoss) {
                    FinalsMiddleBossAttack finalsMiddleBossAttack = new FinalsMiddleBossAttack(enemyAttack);
                    enemyAttackList.add(finalsMiddleBossAttack);
                }
            }

        }

        if ((cnt % 100 / 25) < 1 && cnt % 5 == 0) {
            // 최종보스의 공격패턴
            // 한 번 공격시 5발의 총알을 세 방향으로 발사
            for (int i = 0; i < enemyList.size(); i++) {
                enemy = enemyList.get(i);
                double angle = Math.atan2(playerY + playerHeight / 2 - (enemy.getY() + enemy.getHeight() / 2), playerX + playerWidth / 2 - (enemy.getX() + enemy.getWidth() / 2));
                enemyAttack = new EnemyAttack(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2, angle);

                if (enemy instanceof FinalBoss) {
                    EnemyAttack enemyAttack_left = new EnemyAttack(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2, angle - Math.toRadians(25));
                    EnemyAttack enemyAttack_right = new EnemyAttack(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2, angle + Math.toRadians(25));
                    FinalBossAttack finalBossAttack = new FinalBossAttack(enemyAttack);
                    FinalBossAttack finalBossAttack_left = new FinalBossAttack(enemyAttack_left);
                    FinalBossAttack finalBossAttack_right = new FinalBossAttack(enemyAttack_right);
                    enemyAttackList.add(finalBossAttack);
                    enemyAttackList.add(finalBossAttack_left);
                    enemyAttackList.add(finalBossAttack_right);
                }
            }

        }

        for (int i = 0; i < enemyAttackList.size(); i++) {
            enemyAttack = enemyAttackList.get(i);
            enemyAttack.fire();
            if (checkPlayerCollision(enemyAttack.getX(), enemyAttack.getY(), enemyAttack.getWidth(), enemyAttack.getHeight())) {
                hitSound.start();
                playerHp -= enemyAttack.getAttack();
                enemyAttackList.remove(enemyAttack);
            }
            // 총알이 화면 밖으로 나가거나 벽과 충돌 시 제거
            if (enemyAttack.getX() < 0 || enemyAttack.getX() > Main.SCREEN_WIDTH || enemyAttack.getY() < 0 || enemyAttack.getY() > Main.SCREEN_HEIGHT) {
                enemyAttackList.remove(enemyAttack);
            }
            for (Wall wall : wallList) {
                Rectangle enemyAttackRect = new Rectangle(enemyAttack.getX(), enemyAttack.getY(), enemyAttack.getWidth(), enemyAttack.getHeight());
                Rectangle wallRect = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
                if (enemyAttackRect.intersects(wallRect)) {
                    enemyAttackList.remove(enemyAttack);
                    break;
                }
            }
        }
    }

    private void potionProcess() {
        if (potion == null && System.currentTimeMillis() - lastPotionTime >= POTION_INTERVAL) {
            int locationX;
            int locationY;
            potion = new Potion(0, 0);
            while (true) {
                locationX = (int) (Math.random() * (Main.SCREEN_WIDTH - 100));
                locationY = (int) (Math.random() * (Main.SCREEN_HEIGHT - 100));

                if (stage == 3) {
                    do {
                        locationY = (int) (Math.random() * (Main.SCREEN_HEIGHT - 100)) + 300;
                    } while (locationY > Main.SCREEN_HEIGHT);
                }

                if (!wallCollision(locationX, locationY, potion.getWidth(), potion.getHeight())) {
                    potion.setX(locationX);
                    potion.setY(locationY);
                    break;
                }
            }

        }
    }

    private void checkPotionCollision() {
        if (potion != null) {
            Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
            Rectangle potionRect = new Rectangle(potion.getX(), potion.getY(), potion.getWidth(), potion.getHeight());
            if (playerRect.intersects(potionRect)) {
                playerHp = Math.min(playerHp + 10, 50); // player의 체력이 50을 넘지 않도록 함
                potion = null;
                lastPotionTime = System.currentTimeMillis();
            }
        }
    }

    private void shoeProcess() {
        if (shoe == null && System.currentTimeMillis() - lastShoeTime >= SHOE_INTERVAL) {
            int locationX;
            int locationY;
            shoe = new Shoe(0, 0);
            while (true) {
                locationX = (int) (Math.random() * (Main.SCREEN_WIDTH - 100));
                locationY = (int) (Math.random() * (Main.SCREEN_HEIGHT - 100));

                if (stage == 3) {
                    do {
                        locationY = (int) (Math.random() * (Main.SCREEN_HEIGHT - 100)) + 300;
                    } while (locationY > Main.SCREEN_HEIGHT);
                }

                if (!wallCollision(locationX, locationY, shoe.getWidth(), shoe.getHeight())) {
                    shoe.setX(locationX);
                    shoe.setY(locationY);
                    break;
                }
            }
        }

        if (shoePickupTime != 0 && System.currentTimeMillis() - shoePickupTime >= SHOE_DURATION) {
            playerSpeed = 10;
            shoePickupTime = 0;
        }
    }

    private void checkShoeCollision() {
        if (shoe != null) {
            Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
            Rectangle shoeRect = new Rectangle(shoe.getX(), shoe.getY(), shoe.getWidth(), shoe.getHeight());
            if (playerRect.intersects(shoeRect)) {
                playerSpeed += 5;
                shoe = null;
                lastShoeTime = System.currentTimeMillis();

                shoePickupTime = System.currentTimeMillis();
            }
        }
    }

    private void sniperProcess() {
        if (sniper == null && System.currentTimeMillis() - lastSniperTime >= SNIPER_INTERVAL) {
            int locationX;
            int locationY;
            sniper = new Sniper(0, 0);
            while (true) {
                locationX = (int) (Math.random() * (Main.SCREEN_WIDTH - 100));
                locationY = (int) (Math.random() * (Main.SCREEN_HEIGHT - 100));

                if (stage == 3) {
                    do {
                        locationY = (int) (Math.random() * (Main.SCREEN_HEIGHT - 100)) + 300;
                    } while (locationY > Main.SCREEN_HEIGHT);
                }

                if (!wallCollision(locationX, locationY, sniper.getWidth(), sniper.getHeight())) {
                    sniper.setX(locationX);
                    sniper.setY(locationY);
                    break;
                }
            }
        }

        if (sniperPickupTime != 0 && System.currentTimeMillis() - sniperPickupTime >= SNIPER_DURATION) {
            for (PlayerAttack playerAttack : playerAttackList) {
                playerAttack.attackReset();
            }
            sniperPickupTime = 0;
        }
    }

    private void checkSniperCollision() {
        if (sniper != null) {
            Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
            Rectangle sniperRect = new Rectangle(sniper.getX(), sniper.getY(), sniper.getWidth(), sniper.getHeight());
            if (playerRect.intersects(sniperRect)) {
                for (PlayerAttack playerAttack : playerAttackList) {
                    playerAttack.attackUp();
                }
                sniper = null;
                lastSniperTime = System.currentTimeMillis();

                sniperPickupTime = System.currentTimeMillis();
            }
        }
    }

    private void checkGameOver() {
        if (playerHp <= 0 || finalBossIsMaded && enemyList.isEmpty()) {
            if (finalBossIsMaded && enemyList.isEmpty()) {
                gameCleared = true;
            }
            isOver = true;
            rankDisplayed = true;
            backgroundMusic.stop();
        }
    }

    private void checkStage() { // 조건과 비교해 stage의 상태를 확인, 변경
        if (0 <= score && score < 2000) {
            stage = 1;
        } else if (score == 2000 && !isPaused) {
            stage = 2;
            stageInTransit = true;
            isPaused = true;
        } else if (middleBossIsMaded && enemyList.size() == 0 && !isPaused) {
            stage = 3;
            stageInTransit = true;
            isPaused = true;
        }
    }

    private void enemyMoveProcess() {
        for (int i = 0; i < enemyList.size(); i++) {
            enemy = enemyList.get(i);

            if (enemy instanceof FinalBoss) continue;

            int speed;

            if (enemy instanceof SaveDemon) { // 적의 종류에 따라 속도가 다름
                speed = 3;
            } else if (enemy instanceof EvilClippy) {
                speed = 2;
            } else {
                speed = 1;
            }
            moveEnemy(enemy, speed); // 적의 움직임을 처리
        }
    }

    private void moveEnemy(Enemy enemy, int speed) {
        int deltaX = 0;
        int deltaY = 0;

        int distX = playerX - enemy.getX();
        int distY = playerY - enemy.getY();

        if (!(-speed <= distX && distX <= speed)) {
            // 적과 player의 x 좌표의 차이가 적의 이동보다 작을 시 불필요한 움직임을 하지 않음
            if (enemy.getX() < playerX) deltaX = speed;
            else if (enemy.getX() > playerX) deltaX = -speed;
        }

        if (!(-speed <= distY && distY <= speed)) {
            // 적과 player의 y 좌표의 차이가 적의 이동보다 작을 시 불필요한 움직임을 하지 않음
            if (enemy.getY() < playerY) deltaY = speed;
            else if (enemy.getY() > playerY) deltaY = -speed;
        }


        //적이 새로운 x 또는 y 좌표로 이동 시에 충돌이 발생하는지를 확인하며 이동
        if (!willCollide(enemy.getX() + deltaX, enemy.getY(), enemy)) {
            enemy.setX(enemy.getX() + deltaX);
        }
        if (!willCollide(enemy.getX(), enemy.getY() + deltaY, enemy)) {
            enemy.setY(enemy.getY() + deltaY);
        }
    }

    private boolean willCollide(int newX, int newY, Enemy enemy) {
        // 적이 새로운 좌표로 충돌하는지를 확인
        Rectangle enemyRect = new Rectangle(newX, newY, enemy.getWidth(), enemy.getHeight());
        Rectangle wallRect;
        for (Wall wall : wallList) {
            wallRect = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            if (enemyRect.intersects(wallRect)) {
                return true;
            }
        }
        return false;
    }

    public void setUp(boolean up) {
        this.up = up;
        if (!up && !down && !left && !right) player = playerDefault;
    }

    public void setDown(boolean down) {
        this.down = down;
        if (!up && !down && !left && !right) player = playerDefault;
    }

    public void setLeft(boolean left) {
        this.left = left;
        if (!up && !down && !left && !right) player = playerDefault;
    }

    public void setRight(boolean right) {
        this.right = right;
        if (!up && !down && !left && !right) player = playerDefault;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public void setPause(boolean stageInTransit) {
        this.stageInTransit = stageInTransit;
    }

    public boolean isOver() {
        return isOver;
    }

    public void gameDraw(Graphics g) {
        playerDraw(g);
        wallDraw(g);
        enemyDraw(g);
        potionDraw(g);
        shoeDraw(g);
        sniperDraw(g);
        infoDraw(g);

        if (isOver()) {
            gameOverDraw(g);
        }

        if (stageInTransit) {
            stageTransitDraw(g);
        }
    }

    private void playerDraw(Graphics g) {
        g.drawImage(player, playerX, playerY, null);
        g.setColor(Color.GREEN);
        g.fillRect(playerX - playerWidth / 2, playerY - 40, playerHp * 4, 20); // player의 체력바

        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            g.drawImage(playerAttack.getImage(), playerAttack.getX(), playerAttack.getY(), null);
        }
    }

    private void wallDraw(Graphics g) {
        for (int i = 0; i < wallList.size(); i++) {
            wall = wallList.get(i);
            g.drawImage(wall.getImage(), wall.getX(), wall.getY(), null);
        }
    }

    private void enemyDraw(Graphics g) {
        for (int i = 0; i < enemyList.size(); i++) {
            enemy = enemyList.get(i);
            g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), null);
            g.setColor(Color.RED);
            g.setFont(new Font("Default", Font.BOLD, 30));

            // 적의 종류에 따라 체력바의 위치를 다르게 설정
            if (enemy instanceof MidtermsMiddleBoss) {
                g.drawString("Midterms", Main.SCREEN_WIDTH / 4, Main.SCREEN_HEIGHT * 3 / 4 - 20);
                g.fillRect(Main.SCREEN_WIDTH / 4, Main.SCREEN_HEIGHT * 3 / 4, enemy.getHP() * 6, 30);
            }

            if (enemy instanceof FinalsMiddleBoss) {
                g.drawString("Finals", Main.SCREEN_WIDTH / 4, Main.SCREEN_HEIGHT * 3 / 4 + 100);
                g.fillRect(Main.SCREEN_WIDTH / 4, Main.SCREEN_HEIGHT * 3 / 4 + 120, enemy.getHP() * 6, 30);
            }

            if (enemy instanceof FinalBoss) {
                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), null);
                g.drawString("The Group Project", Main.SCREEN_WIDTH / 4, Main.SCREEN_HEIGHT * 3 / 4 - 20);
                g.fillRect(Main.SCREEN_WIDTH / 4, Main.SCREEN_HEIGHT * 3 / 4, enemy.getHP() * 3, 30);
            }
        }

        for (int i = 0; i < enemyAttackList.size(); i++) {
            enemyAttack = enemyAttackList.get(i);
            g.drawImage(enemyAttack.getImage(), enemyAttack.getX(), enemyAttack.getY(), null);
        }
    }

    private void stageTransitDraw(Graphics g) {
        g.drawImage(stageTransit, 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, null);

        String pauseText = "Stage Cleared: Proceeding to Stage " + stage;
        String pauseText2 = "Press 'R' to Continue";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Default", Font.BOLD, 50));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(pauseText);
        int x = (Main.SCREEN_WIDTH - textWidth) / 2;
        int y = Main.SCREEN_HEIGHT / 2;
        g.drawString(pauseText, x, y);

        textWidth = fm.stringWidth(pauseText2);
        x = (Main.SCREEN_WIDTH - textWidth) / 2;
        y += 60;
        g.drawString(pauseText2, x, y);
    }

    private void potionDraw(Graphics g) {
        if (potion != null) {
            g.drawImage(potion.getImage(), potion.getX(), potion.getY(), null);
        }
    }

    private void shoeDraw(Graphics g) {
        if (shoe != null) {
            g.drawImage(shoe.getImage(), shoe.getX(), shoe.getY(), null);
        }
    }

    private void sniperDraw(Graphics g) {
        if (sniper != null) {
            g.drawImage(sniper.getImage(), sniper.getX(), sniper.getY(), null);
        }
    }

    private void infoDraw(Graphics g) {
        g.setFont(new Font("Default", Font.BOLD, 20));
        g.setColor(Color.MAGENTA);
        g.drawString("SCORE : " + score, 50, 50);
    }

    public void gameOverDraw(Graphics g) {
        int screenX = Main.SCREEN_WIDTH / 2 - 80;
        int screenY = Main.SCREEN_HEIGHT / 2 - 270;

        if (rankDisplayed) {
            String name = "NULL";
            try {
                rank = new Rank();
                name = rank.getPlayerNameFromGameScreen();

                if (name != null && !name.trim().isEmpty()) // 빈 이름이 또는 공백만이 입력되었을 경우
                    rank.addPlayer(name, cnt, score);
                else
                    rank.addPlayer("NULL", cnt, score);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            rankDisplayed = false;
        }

        if (rank != null)
            rank.displayRank(g, screenX, screenY);
    }

    public boolean getCleared() { // 게임 종료 시 클리어 여부를 반환
        return gameCleared;
    }
}