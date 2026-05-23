import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        void sleep(long milisecond) {
            try {Thread.sleep(milisecond);} catch (InterruptedException e) {}
        }
        
        class LeftMenuBar extends JPanel {
            LeftMenuBar() {
                setSize(100, 400);
                setLocation(0, 0);
                setBackground(Color.red);
                setVisible(rootPaneCheckingEnabled);
            }
        }
        
        class TextBox extends JPanel {

            class LabelPanel extends JPanel {
                JLabel label[] = new JLabel[4];
                LabelPanel() {
                    setSize(700, 225);
                    setLocation(0, 0);
                    setLayout(null);
                    setBackground(Color.WHITE);
                    
                    for (int i = 0; i < 4; i++) {
                        label[i] = new JLabel();
                        label[i].setFont(font);
                        label[i].setForeground(Color.BLACK);
                        label[i].setHorizontalAlignment(SwingConstants.LEFT);
                        add(label[i]);
                        label[i].setLocation(10, 5 + 55 * i);
                        label[i].setSize(700, 50);
                    }
                    
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
                        sleep(50);
                    }
                    sleep(1000);
                    for (int i = 0; i < 4; i++) label[i].setVisible(false);
                }
            }
            class ButtonPanel extends JPanel {
                JButton button[] = new JButton[4];

                ButtonPanel(){
                    setSize(700, 225);
                    setLocation(0, 0);
                    setLayout(null);

                    button[0] = new JButton("공격");
                    button[1] = new JButton("스킬");
                    button[2] = new JButton("아이템");
                    button[3] = new JButton("교체");
                    for (int i = 0; i < 4; i++) {
                        add(button[i]);
                        button[i].setFont(font);
                        button[i].setSize(334, 105);
                        button[i].setLocation((i % 2 == 0)?5:345, (i / 2 == 0)?5:115);
                    }
                    setVisible(false);
                    // TODO BUTTON EVENT LISTENER
                    button[0].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            setVisible(false);
                            GameManager.raiseEvent(Event.newAttackEvent(GameManager.getCurrentPokemon().getAttackDamage()));
                        }
                    });
                }
            }
            
            LabelPanel lp = new LabelPanel();
            ButtonPanel bp = new ButtonPanel();

            TextBox() {
                setLayout(null);
                setBackground(Color.WHITE);
                add(lp);
                add(bp);
                //TODO button listener
                
                // TextBox init
                setSize(700, 225);
                setLocation(0, 400);
                setBackground(Color.WHITE);
                setVisible(rootPaneCheckingEnabled);

                // regist event handler
                GameManager.registEventListener((Event e) -> {
                    if (e.type == Event.EVENT_TYPE.TEXT) {
                        hideAllFrame();
                        lp.setVisible(true);
                        lp.writeText(e.text);
                    } else if (e.type == Event.EVENT_TYPE.TURN_START) {
                        hideAllFrame();
                        bp.setVisible(true);
                    }
                });
            }

            void hideAllFrame() {
                lp.setVisible(false);
                bp.setVisible(false);
            }
        }

        class BattleScreen extends JPanel {
            int x = 0, y = 160, enemyX = 360, enemyY = 0;
            BattleScreen() {
                setSize(600, 400);
                setLocation(100, 0);
                setBackground(Color.GRAY);
                setVisible(rootPaneCheckingEnabled);

                GameManager.registEventListener((Event e) -> {  // event 처리
                    switch (e.type) { // TODO SKILL CHANGE DEAD AND MORE
                        case Event.EVENT_TYPE.ATTACK:
                            x += 10;
                            repaint();
                            sleep(100);
                            x -= 10;
                            repaint();
                            sleep(100);

                            for (int i = 0; i < 3; i++) {
                                enemyX += 10;
                                repaint();
                                sleep(50);
                                enemyX -= 20;
                                repaint();
                                sleep(50);
                                enemyX += 10;
                            }
                            repaint();
                            break;
                        case Event.EVENT_TYPE.ENEMY_ATTACK:
                            enemyX -= 10;
                            repaint();
                            sleep(100);
                            enemyX += 10;
                            repaint();
                            sleep(100);

                            for (int i = 0; i < 3; i++) {
                                x += 10;
                                repaint();
                                sleep(50);
                                x -= 20;
                                repaint();
                                sleep(50);
                                x += 10;
                            }
                            repaint();
                            break;
                            // Below is doing nothing list
                        case Event.EVENT_TYPE.TURN_START:
                        case Event.EVENT_TYPE.TEXT:
                        case Event.EVENT_TYPE.BATTLE_START: 
                            break;
                        default:
                            System.out.println("UnHandled Event in UI->BattleScreen: " + e.type);
                }});
            }
            
            public void paint(Graphics g) {
                super.paint(g);
                g.drawImage(POKEMON_IMG[GameManager.getInstance().pokemon[GameManager.getInstance().selectedPokemonIdx].id][2], x, y, 240, 240, null);
                g.drawImage(POKEMON_IMG[GameManager.getInstance().enemyPokemon.id][0], enemyX, enemyY, 240, 240, null);
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
        // TODO remove test code
        // Event.raiseAllEvent();
        gm.raiseEvent(Event.newTextEvent("HELLO"));
        gm.raiseEvent(Event.newTurnStartEvent());
        // testcode end
        gm.startLoop();
    }
}
