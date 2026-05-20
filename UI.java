import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class UI extends JFrame{
    public static final BufferedImage[][] POKEMON_IMG = new BufferedImage[152][8];
    public static Font font;

    public static void loadSprite() {
        if (POKEMON_IMG[1][0] != null) return;
        int x, y, tmp;
        BufferedImage baseSprite = null;
        try { baseSprite = ImageIO.read(new File("./asset/PokemonSprite.png")); } catch (IOException e) { System.out.println("loadBaseSpriteError"); System.exit(1);}

        for (int i = 1; i < 152; i++) {
            tmp = i - 1;
            if (i >= 3) tmp++;
            if (i >= 12) tmp++;
            if (i >= 19) tmp++;
            if (i >= 20) tmp++;
            if (i >= 25) tmp++;
            if (i >= 26) tmp++;
            if (i >= 41) tmp++;
            if (i >= 42) tmp++;
            if (i >= 44) tmp++;
            if (i >= 45) tmp++;
            if (i >= 64) tmp++;
            if (i >= 65) tmp++;
            if (i >= 84) tmp++;
            if (i >= 85) tmp++;
            if (i >= 97) tmp++;
            if (i >= 111) tmp++;
            if (i >= 112) tmp++;
            if (i >= 118) tmp++;
            if (i >= 119) tmp++;
            if (i >= 123) tmp++;
            if (i >= 129) tmp++;
            if (i >= 130) tmp++;
            x = (tmp % 10) * 243;
            y = (tmp / 10) * 195;
            for (int j = 0; j < 6; j++) {
                POKEMON_IMG[i][j] = baseSprite.getSubimage(x + 1 +  (j % 3) * 81, y + 34 + (j / 3) * 81, 80, 80);
            }
            POKEMON_IMG[i][6] = baseSprite.getSubimage(x + 178, y + 1, 32, 32);
            POKEMON_IMG[i][7] = baseSprite.getSubimage(x + 211, y + 1, 32, 32);
        }
    }


    class MainScreen extends JPanel {
        LeftMenuBar leftMenuBar;
        TextBox textBox;
        BattleScreen battleScreen;
        
        class LeftMenuBar extends JPanel {
            LeftMenuBar() {
                setSize(100, 400);
                setLocation(0, 0);
                setBackground(Color.red);
                setVisible(rootPaneCheckingEnabled);
            }
        }
        
        class TextBox extends JPanel {
            JLabel label[] = new JLabel[4];
            Event nextEvent;
            TextBox() {
                setLayout(null);
                for (int i = 0; i < 4; i++) {
                    label[i] = new JLabel();
                    label[i].setFont(font);
                    label[i].setForeground(Color.BLACK);
                    label[i].setHorizontalAlignment(SwingConstants.LEFT);
                    add(label[i]);
                    label[i].setLocation(10, 5 + 55 * i);
                    label[i].setSize(700, 50);

                }
                setSize(700, 225);
                setLocation(0, 400);
                setBackground(Color.WHITE);
                setVisible(rootPaneCheckingEnabled);
                GameManager.getInstance().registEventListener((Event e) -> {
                    if (e.type == Event.EVENT_TYPE.TEXT) {
                        writeText(e.text);
                    }
                });
            }

            void writeText(String s) {
                int idx = 0, length = 0;
                String tmp = "";
                for (int i = 0; i < 4; i++) {
                    label[i].setText(tmp);
                    label[i].setVisible(true);
                }
                for (int i = 0; i < s.length(); i++){
                    if (s.charAt(i) == '\n' || length > 32) {
                        idx++;
                        tmp = "";
                        length = 0;
                        if (idx >= 4) {
                            label[0].setText(label[1].getText());
                            label[1].setText(label[2].getText());
                            label[2].setText(label[3].getText());
                            label[3].setText("");
                            idx = 3;
                        }
                        if (s.charAt(i) == '\n') continue;
                    }

                    if (tmp.length() != 0 || s.charAt(i) != ' ') {
                        System.out.println(tmp.length() + ": " + s.charAt(i));
                        tmp = tmp + s.charAt(i);
                        length += (s.charAt(i) < 128)?1:2;
                    }  // 전각 반각 구분

                    label[idx].setText(tmp);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {

                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
        }

        class BattleScreen extends JPanel {
            int x = 0, y = 160, enemyX = 360, enemyY = 0;
            BattleScreen() {
                setSize(600, 400);
                setLocation(100, 0);
                setBackground(Color.GRAY);
                setVisible(rootPaneCheckingEnabled);

                GameManager.getInstance().registEventListener((Event e) -> {  // ATTACK event 처리
                    switch (e.type) {
                        case Event.EVENT_TYPE.ATTACK:
                            x += 10;
                            repaint();
                            try {Thread.sleep(100);} catch (InterruptedException exception) {}
                            x -= 10;
                            repaint();
                            try {Thread.sleep(100);} catch (InterruptedException exception) {}

                            for (int i = 0; i < 3; i++) {
                                enemyX += 10;
                                repaint();
                                try {Thread.sleep(50);} catch (InterruptedException exception) {}
                                enemyX -= 20;
                                repaint();
                                try {Thread.sleep(50);} catch (InterruptedException exception) {}
                                enemyX += 10;
                            }
                            repaint();
                            break;
                        case Event.EVENT_TYPE.ENEMY_ATTACK:
                            enemyX -= 10;
                            repaint();
                            try {Thread.sleep(100);} catch (InterruptedException exception) {}
                            enemyX += 10;
                            repaint();
                            try {Thread.sleep(100);} catch (InterruptedException exception) {}

                            for (int i = 0; i < 3; i++) {
                                x += 10;
                                repaint();
                                try {Thread.sleep(50);} catch (InterruptedException exception) {}
                                x -= 20;
                                repaint();
                                try {Thread.sleep(50);} catch (InterruptedException exception) {}
                                x += 10;
                            }
                            repaint();
                            break;
                        default:
                            System.out.println("UnHandled Event in UI->BattleScreen: " + e.type);
                }});
                //TODO SKILL CHANGE DEAD AND MORE


            }
            
            public void paint(Graphics g) {
                super.paint(g);
                g.drawImage(POKEMON_IMG[1][2], x, y, 240, 240, null);
                g.drawImage(POKEMON_IMG[151][0], enemyX, enemyY, 240, 240, null);
            }
        }

        MainScreen() {
            leftMenuBar = new LeftMenuBar();
            textBox = new TextBox();
            battleScreen = new BattleScreen();
            setLayout(null);
            add(leftMenuBar);
            add(textBox);
            add(battleScreen);
        }
    }

    public UI() {
        try { font = Font.createFont(Font.TRUETYPE_FONT, new File("./asset/DungGeunMo.ttf")).deriveFont(38.0f); } catch (IOException | FontFormatException e) { System.out.println("loadFontError"); System.exit(1); }
        MainScreen mainScreen = new MainScreen();
        loadSprite();
        setSize(700, 665);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(mainScreen);


        setVisible(true);
    }

    public static void main(String args[]) {  // Entry point
        GameManager gm = GameManager.getInstance();
        new UI();
        gm.raiseEvent(Event.newTextEvent("야생의 뮤가 나타났다!\n무엇을 해야할까?"));
        gm.raiseEvent(Event.newAttackEvent(500));
        gm.raiseEvent(Event.newTextEvent("ABC"));
        gm.raiseEvent(Event.newEnemyAttackEvent(500));
        gm.raiseEvent(Event.newTextEvent("DEF"));
        gm.raiseEvent(Event.newAttackEvent(500));
        gm.raiseEvent(Event.newTextEvent("GHK"));
        gm.raiseEvent(Event.newEnemyAttackEvent(500));
    }
}
