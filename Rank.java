import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

public class Rank implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String fileName = "rank.dat";

    //Player의 point를 저장하기 위한 ArrayList
    ArrayList<Player> playerList = new ArrayList<>();

    public class Player implements Comparable<Player>, Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int time;
        private int score;
        private int point;

        Player(String name, int time, int score) {
            this.name = name;
            this.time = time;
            this.score = score;
            this.point = this.score - this.time / 100;
            if ( this.point < 0 ) this.point = 0; // Prevent negative points
        }

        // Player의 Point를 내림차순으로 정렬
        @Override
        public int compareTo(Player player) {
            return Integer.compare(player.point, this.point); // Higher times first
        }


        // Player의 정보를 String으로 변환
        @Override
        public String toString() {
            return "[" + this.name + "] : " + this.point;
        }
    }

    // JOptionPane을 통해 Player의 Name을 게임 화면 속에서 입력
    public String getPlayerNameFromGameScreen() {
        return JOptionPane.showInputDialog(null, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
    }

    // "Rank.dat" 파일 Open - ObjectInputStream
    // 기존 "Rank.dat"를 열어 기존 Rank값을 ArrayList에 저장
    public Rank() throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            @SuppressWarnings("unchecked")
            ArrayList<Player> readObject = (ArrayList<Player>) inputStream.readObject();
            playerList = readObject;
        } catch (FileNotFoundException e) {
            // File not found, ignore
        } catch (EOFException e) {
            // End of file, ignore
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Player의 정보를 ArrayList에 추가
    // ArrayList 정렬
    // Player의 Rank 정보를 "Rank.dat"에 기록 - ObjectOutputStream
    public void addPlayer(String name, int time, int score) {
        Player player = new Player(name, time, score);

        playerList.add(player);
        Collections.sort(playerList);

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(playerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ArrayList에 저장된 Rank 정보를 개임 화면에 출력
    public void displayRank(Graphics g, int screenX, int screenY) {
        g.setFont(new Font("Default", Font.BOLD, 30));
        g.setColor(Color.WHITE);

        for (Player p : playerList) {
            if (screenY > Main.SCREEN_HEIGHT - 70) break;
            g.drawString(p.toString(), screenX, screenY);
            screenY += 40;
        }
    }
}