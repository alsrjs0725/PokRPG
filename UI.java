import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

public class UI extends JFrame {
    public static final BufferedImage[][] POKEMON_IMG = new BufferedImage[152][8];
    public static final BufferedImage[][] GRAY_POKEMON_IMG = new BufferedImage[152][8];
    public static Font largeFont, mediumFont, smallFont;
    public MainScreen mainScreen;

    private static UI ui;

    public static UI getInstance() {
        if (ui == null) ui = new UI();
        return ui;
    }

    public static void loadSprite() {
        if (POKEMON_IMG[1][0] != null) return;
        int x, y, tmp;
        BufferedImage baseSprite = null, grayScaledSprite = null;
        try { baseSprite = ImageIO.read(new File("./asset/PokemonSprite.png")); } catch (IOException e) { System.out.println("loadBaseSpriteError"); System.exit(1);}
        try { grayScaledSprite = ImageIO.read(new File("./asset/PokemonSpriteGrayScaled.png")); } catch (IOException e) { System.out.println("loadBaseSpriteError"); System.exit(1);}

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
                GRAY_POKEMON_IMG[i][j] = grayScaledSprite.getSubimage(x + 1 +  (j % 3) * 81, y + 34 + (j / 3) * 81, 80, 80);
            }
            POKEMON_IMG[i][6] = baseSprite.getSubimage(x + 178, y + 1, 32, 32);
            POKEMON_IMG[i][7] = baseSprite.getSubimage(x + 211, y + 1, 32, 32);
            GRAY_POKEMON_IMG[i][6] = grayScaledSprite.getSubimage(x + 211, y + 1, 32, 32);
            GRAY_POKEMON_IMG[i][7] = grayScaledSprite.getSubimage(x + 211, y + 1, 32, 32);
        }
    }


    class MainScreen extends JPanel {
        LeftMenuBar leftMenuBar;
        TextBox textBox;
        MainArea mainArea;

        void sleep(long milisecond) {
            try {Thread.sleep(milisecond);} catch (InterruptedException e) {}
        }
        
        class LeftMenuBar extends JPanel {
            JButton button[] = new JButton[4];

            LeftMenuBar() {
                setLayout(null);
                button[0] = new JButton("이동");
                button[1] = new JButton("도감");
                button[2] = new JButton("배낭");
                button[3] = new JButton("취소");

                for (int i = 0; i < 4; i++) {
                    add(button[i]);
                    button[i].setFont(mediumFont);
                    button[i].setSize(90, 90);
                    button[i].setLocation(5, i * 100 + 5);
                    button[i].setVisible(true);
                }
                button[3].setEnabled(false);

                button[0].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // TODO
                    }
                });
                button[1].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // TODO
                    }
                });
                button[2].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // TODO
                    }
                });
                button[3].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        UI ui = UI.getInstance();
                        ui.mainScreen.mainArea.bs.setVisible(true);
                        ui.mainScreen.textBox.bp.setVisible(true);
                        button[3].setEnabled(false);
                    }
                });

                setSize(100, 400);
                setLocation(0, 0);
                setBackground(Color.red);
                setVisible(rootPaneCheckingEnabled);
            }
        }
        
        class TextBox extends JPanel {

            class LabelPanel extends JPanel {
                JLabel label[] = new JLabel[4];

                @Override
                public void setVisible(boolean aFlag) {
                    if (aFlag) hideAllFrame();
                    super.setVisible(aFlag);
                }

                LabelPanel() {
                    setSize(700, 225);
                    setLocation(0, 0);
                    setLayout(null);
                    setBackground(Color.WHITE);
                    
                    for (int i = 0; i < 4; i++) {
                        label[i] = new JLabel();
                        label[i].setFont(largeFont);
                        label[i].setForeground(Color.BLACK);
                        label[i].setHorizontalAlignment(SwingConstants.LEFT);
                        add(label[i]);
                        label[i].setLocation(10, 5 + 55 * i);
                        label[i].setSize(700, 50);
                        label[i].setVisible(rootPaneCheckingEnabled);
                    }
                    
                }

                void writeText(String s) {
                    int idx = 0, length = 0;
                    String tmp = "";
                    for (int i = 0; i < 4; i++) {
                        label[i].setText("");
                    }
                    setVisible(true);
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
                    setVisible(false);
                }
            }

            class ButtonPanel extends JPanel {
                JButton button[] = new JButton[4];

                @Override
                public void setVisible(boolean aFlag) {
                    if (aFlag) hideAllFrame();
                    super.setVisible(aFlag);
                }

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
                        button[i].setFont(largeFont);
                        button[i].setSize(334, 105);
                        button[i].setLocation((i % 2 == 0)?5:345, (i / 2 == 0)?5:115);
                    }
                    setVisible(false);
                    // TODO BUTTON EVENT LISTENER
                    button[0].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            setVisible(false);
                            int dmg = GameManager.getCurrentPokemon().getAttackDamage();
                            dmg += new Random().nextInt() % (dmg / 5);
                            GameManager.raiseEvent(Event.newAttackEvent(dmg));
                        }
                    });
                    button[1].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            setVisible(false);
                            // TODO
                        }
                    });
                    button[2].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            setVisible(false);
                            UI.getInstance().mainScreen.mainArea.selectItem((Item i) -> {
                                if (i.selectPokemon) {
                                    UI.getInstance().mainScreen.mainArea.selectPokemon((Pokemon p) -> {
                                        GameManager.raiseEvent(Event.newItemEvent(i, p));
                                        UI.getInstance().mainScreen.mainArea.bs.setVisible(true);
                                    });
                                } else {
                                    GameManager.raiseEvent(Event.newItemEvent(i, null));
                                    UI.getInstance().mainScreen.mainArea.bs.setVisible(true);
                                }
                            });
                        }
                    });
                    button[3].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            setVisible(false);
                            UI.getInstance().mainScreen.mainArea.selectPokemon((Pokemon p) -> {
                                int idx = 0;
                                for (int i = 0; i < 6; i++) if (GameManager.getInstance().pokemon[i] == p) idx = i;
                                GameManager.raiseEvent(Event.newChangeEvent(idx));
                            });
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
            }

            void hideAllFrame() {
                lp.setVisible(false);
                bp.setVisible(false);
            }
        }

        class MainArea extends JPanel {
            BattleScreen bs = new BattleScreen();
            SelectPokemonScreen sps = new SelectPokemonScreen();
            SelectItemScreen sis = new SelectItemScreen();

            void hideAllFrame() {
                bs.setVisible(false);
                sps.setVisible(false);
                sis.setVisible(false);
            }

            MainArea() {
                setLayout(null);
                setSize(600, 400);
                setLocation(100, 0);
                setVisible(true);
                add(bs);
                add(sps);
                add(sis);
            }

            class SelectPokemonScreen extends JPanel {

                class PokemonButton extends JPanel {
                    Pokemon pokemon;
                    Consumer<Pokemon> callBack;
                    boolean isEnabled = true, _isEnabled = true;
                    JLabel image = new JLabel(), name = new JLabel(), hp = new JLabel(), lv = new JLabel();
                    MouseAdapter clickListener = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println(pokemon.name);
                            if (!_isEnabled) return;
                            callBack.accept(pokemon);
                        }
                    };

                    PokemonButton(Pokemon pokemon, Consumer<Pokemon> callBack) {
                        this.pokemon = pokemon;
                        this.callBack = callBack;
                        setLayout(null);
                        name.setFont(mediumFont); hp.setFont(smallFont); lv.setFont(smallFont); 
                        image.setSize(127, 127);
                        image.setLocation(0, 0);
                        hp.setSize(112, 57);
                        hp.setLocation(127, 70);
                        name.setSize(168, 70);
                        name.setLocation(127,0);
                        lv.setSize(56,57);
                        lv.setLocation(239,70);
                        image.setVisible(rootPaneCheckingEnabled);
                        hp.setVisible(rootPaneCheckingEnabled);
                        name.setVisible(rootPaneCheckingEnabled);
                        lv.setVisible(rootPaneCheckingEnabled);
                        add(lv);
                        add(name);
                        add(image);
                        add(hp);
                        image.addMouseListener(clickListener);
                        name.addMouseListener(clickListener);
                        lv.addMouseListener(clickListener);
                        hp.addMouseListener(clickListener);
                    }

                    void setPokemon(Pokemon p) {
                        this.pokemon = p;
                        update();
                    }

                    void setButtonEnabled(boolean aFlag) {
                        isEnabled = aFlag;
                        update();
                    }

                    void update() {
                        if (!isEnabled) _isEnabled = false;
                        if (pokemon == null) {
                            _isEnabled = false;
                            image.setVisible(false);
                            hp.setVisible(false);
                            name.setVisible(false);
                            lv.setVisible(false);
                            setBackground(Color.GRAY);
                            return;
                        }
                        if (pokemon.getHealth() == 0) {
                            image.setIcon(new ImageIcon(GRAY_POKEMON_IMG[pokemon.id][6].getScaledInstance(127, 127, Image.SCALE_DEFAULT)));
                            _isEnabled = false;
                        } else {
                            image.setIcon(new ImageIcon(POKEMON_IMG[pokemon.id][6].getScaledInstance(127, 127, Image.SCALE_DEFAULT)));
                            if (isEnabled) _isEnabled = true;
                        }
                        hp.setText(pokemon.getHealth() + "/" + pokemon.getMaxHealth());
                        name.setText(pokemon.name);
                        lv.setText(pokemon.getLevel() + "LV");
                        if (_isEnabled) setBackground(Color.WHITE);
                        else setBackground(Color.GRAY);
                    }

                    // TODO POKEMONBUTTON CLICK LISTSER
                }

                PokemonButton pokemonButton[] = new PokemonButton[6];
                Consumer<Pokemon> eventHandler;
                SelectPokemonScreen() {
                    setSize(600, 400);
                    setLocation(0, 0);
                    setLayout(null);
                    setBackground(Color.GRAY);
                    for (int i = 0; i < 6; i++) {
                        pokemonButton[i] = new PokemonButton(GameManager.getInstance().pokemon[i], (Pokemon p) -> {
                            if (eventHandler == null) return;
                            eventHandler.accept(p);
                            eventHandler = null;
                        });
                        pokemonButton[i].setLocation(3 + (i % 2) * 299, 3 + (i / 2) * 130);
                        pokemonButton[i].setSize(295, 127);
                        pokemonButton[i].setFont(largeFont);
                        add(pokemonButton[i]);
                    }
                }

                @Override
                public void setVisible(boolean aFlag) {
                    if (aFlag) {
                        hideAllFrame();
                        for (int i = 0; i < 6; i++){
                            pokemonButton[i].setPokemon(GameManager.getInstance().pokemon[i]);
                        }
                    }
                    super.setVisible(aFlag);
                }
            }

            class BattleScreen extends JPanel {
                int x = 0, y = 160, enemyX = 360, enemyY = 0;
                double scale = 1.0, enemyScale = 1.0;

                @Override
                public void setVisible(boolean aFlag) {
                    if (aFlag) hideAllFrame();
                    super.setVisible(aFlag);
                    UI.getInstance().mainScreen.leftMenuBar.button[3].setEnabled(!aFlag);
                }

                BattleScreen() {
                    setSize(600, 400);
                    setLocation(0, 0);
                    setBackground(Color.GRAY);

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
                            case Event.EVENT_TYPE.BATTLE_START: 
                                setVisible(true);
                                break;
                                case Event.EVENT_TYPE.CHANGE:
                                    setVisible(true);
                                    // TODO ADD MOTION
                                    break;
                            case Event.EVENT_TYPE.TURN_START:
                                UI.getInstance().mainScreen.mainArea.bs.setVisible(true);
                                UI.getInstance().mainScreen.textBox.bp.setVisible(true);
                                break;
                            case Event.EVENT_TYPE.TEXT:
                                UI.getInstance().mainScreen.textBox.lp.writeText(e.text);
                                break;
                            case Event.EVENT_TYPE.DEAD:
                                {
                                    boolean flag = false;
                                    for (int i = 0; i < 6; i++) {
                                        if (GameManager.getInstance().pokemon[i] == null) continue;
                                        if (GameManager.getInstance().pokemon[i].getHealth() != 0) flag = true;
                                    }
                                    if (flag) selectPokemon((Pokemon p) -> {
                                        for (int i = 0; i < 6; i++) if (p == GameManager.getInstance().pokemon[i]) GameManager.raiseEvent(Event.newChangeEvent(i));
                                    });
                                }
                                UI.getInstance().mainScreen.leftMenuBar.button[3].setEnabled(false);
                                break;
                                
                            default:
                                System.out.println("UnHandled Event in UI->BattleScreen: " + e.type);
                    }});
                }
                
                public void paint(Graphics g) {
                    super.paint(g);
                    g.drawImage(POKEMON_IMG[GameManager.getCurrentPokemon().id][2], x, y, (int) (240 * scale), (int) (240 * scale), null);
                    g.drawImage(POKEMON_IMG[GameManager.getInstance().enemyPokemon.id][0], enemyX, enemyY, (int) (240 * enemyScale), (int) (240 * enemyScale), null);
                    g.setColor(Color.WHITE);
                    g.drawRect(320, 240, 240, 80);
                    g.drawRect(40, 50, 240, 80);
                    g.setFont(mediumFont);
                    g.setColor(Color.BLACK);
                    g.drawString(GameManager.getCurrentPokemon().name, 330, 265);
                    g.drawString(GameManager.getInstance().enemyPokemon.name, 50, 75);
                    g.drawString(GameManager.getCurrentPokemon().getLevel() + "LV", 548 - g.getFontMetrics(mediumFont).stringWidth(GameManager.getCurrentPokemon().getLevel() + "LV"), 265);
                    g.drawString(GameManager.getInstance().enemyPokemon.getLevel() + "LV", 268 - g.getFontMetrics(mediumFont).stringWidth(GameManager.getInstance().enemyPokemon.getLevel() + "LV"), 75);
                    g.fillRect(332, 275, 216, 5);
                    g.fillRect(52, 85, 216, 5);
                    g.drawString(GameManager.getCurrentPokemon().getHealth() + "/" + GameManager.getCurrentPokemon().getMaxHealth(), 548 - g.getFontMetrics(mediumFont).stringWidth(GameManager.getCurrentPokemon().getHealth() + "/" + GameManager.getCurrentPokemon().getMaxHealth()), 310);
                    g.drawString(GameManager.getInstance().enemyPokemon.getHealth() + "/" + GameManager.getInstance().enemyPokemon.getMaxHealth(), 268 - g.getFontMetrics(mediumFont).stringWidth(GameManager.getInstance().enemyPokemon.getHealth() + "/" + GameManager.getInstance().enemyPokemon.getMaxHealth()), 120);
                    g.setColor(Color.RED);
                    g.fillRect(332, 275, 216 * GameManager.getCurrentPokemon().getHealth() / GameManager.getCurrentPokemon().getMaxHealth(), 5);
                    g.fillRect(52, 85, 216 * GameManager.getInstance().enemyPokemon.getHealth() / GameManager.getInstance().enemyPokemon.getMaxHealth(), 5);

                }
            }
            
            class SelectItemScreen extends JPanel {
                class ItemButton extends JPanel {
                    JLabel label[] = new JLabel[3];
                    boolean isEnabled = true, _isEnabled = true;
                    Item item;
                    Consumer<Item> callBack;
                    MouseAdapter clickListener = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println(item.name + " clicked");
                            if (!_isEnabled) return;
                            callBack.accept(item);
                        }
                    };

                    ItemButton(Item item, Consumer<Item> callBack) {
                        this.item = item;
                        this.callBack = callBack;
                        setBorder(new EmptyBorder(10, 5, 10, 10));
                        for (int i = 0; i < 3; i++) {
                            label[i] = new JLabel();
                            label[i].setFont(mediumFont);
                            label[i].addMouseListener(clickListener);
                            label[i].setVisible(true);
                        }
                        System.out.println("ITEM " + item.name + " BUTTON GENERATED!");
                        addMouseListener(clickListener);
                        setLayout(new BorderLayout());
                        add(label[0], BorderLayout.WEST);
                        add(label[1], BorderLayout.CENTER);
                        label[1].setHorizontalAlignment(SwingConstants.CENTER);
                        add(label[2], BorderLayout.EAST);
                        update();
                    }

                    void setButtonEnabled(boolean aFlag) {
                        isEnabled = aFlag;
                        update();
                    }

                    void update() {
                        _isEnabled = true;
                        if (!isEnabled) _isEnabled = false;
                        if (GameManager.getItemCount(item.id) == 0) _isEnabled = false;

                        label[0].setText(Item.get(item.id).name);
                        label[1].setText(Item.get(item.id).description);
                        label[2].setText(GameManager.getItemCount(item.id) + "개");
                        if (_isEnabled) setBackground(Color.WHITE);
                        else setBackground(Color.GRAY);
                    }
                }

                List<ItemButton> btn = new ArrayList<>();
                JScrollPane pane = new JScrollPane();
                JPanel itemList = new JPanel();
                Consumer<Item> eventHandler;

                SelectItemScreen() {
                    setSize(585, 400);
                    setLocation(0, 0);
                    setLayout(new BorderLayout());
                    setBackground(Color.GRAY);
                    add(pane, BorderLayout.CENTER);
                    itemList.setLayout(new GridLayout(0, 1));
                    itemList.setBackground(Color.RED);
                    pane.setViewportView(itemList);
                    pane.setBackground(Color.RED);
                    pane.getVerticalScrollBar().setUnitIncrement(16);
                    pane.setVisible(true);

                    for (int i = 1; i < Item.ITEM_TABLE.length; i++) {
                        ItemButton itemButton = new ItemButton(Item.ITEM_TABLE[i], (Item item) -> {
                            if (eventHandler == null) return;
                            eventHandler.accept(item);
                            eventHandler = null;
                        });
                        btn.add(itemButton);
                        itemList.add(itemButton);
                        itemButton.setPreferredSize(new Dimension(565, 150));
                        itemButton.setVisible(true);
                    }
                }

                @Override
                public void setVisible(boolean aFlag) {
                    if (aFlag) {
                        hideAllFrame();
                        for (ItemButton ibtn : btn) {
                            ibtn.update();
                        }
                    }
                    super.setVisible(aFlag);
                }
                
                
            }
            
            public void selectPokemon(Consumer<Pokemon> func) {
                sps.setVisible(true);
                sps.eventHandler = func;
            }

            public void selectItem(Consumer<Item> func) {
                sis.setVisible(true);
                sis.eventHandler = func;
            }
        }

        MainScreen() {
            leftMenuBar = new LeftMenuBar();
            textBox = new TextBox();
            mainArea = new MainArea();
            setLayout(null);
            add(leftMenuBar);
            add(textBox);
            add(mainArea);
        }
    }
    

    public UI() {
        try { 
            largeFont = Font.createFont(Font.TRUETYPE_FONT, new File("./asset/DungGeunMo.ttf")).deriveFont(38.0f); 
            mediumFont = largeFont.deriveFont(24.0f);
            smallFont = largeFont.deriveFont(18.0f);
        } catch (IOException | FontFormatException e) { 
            System.out.println("loadFontError"); System.exit(1); 
        }
        mainScreen = new MainScreen();
        loadSprite();
        setSize(700, 665);
        // setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(mainScreen);


        setVisible(true);
    }

    public static void main(String args[]) {  // Entry point
        GameManager gm = GameManager.getInstance();
        UI ui = UI.getInstance();
        ui.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
                GameManager.raiseEvent(Event.newExitEvent());
                System.exit(0);
            }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}

        });
        // TODO remove test code
        // Event.raiseAllEvent();
        // gm.raiseEvent(Event.newTextEvent("HELLO"));
        gm.raiseEvent(Event.newBattleStartEvent(Pokemon.generateRandom()));
        // ui.mainScreen.sleep(10000);
        ui.mainScreen.mainArea.selectPokemon((Pokemon p) -> {
            System.out.println(p.name);
        });
        // testcode end
        gm.startLoop();
    }
}
