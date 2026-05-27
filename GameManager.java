import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.HashMap;

public class GameManager {
    private static GameManager gm;
    private static final String SAVE_FILE = "./save.json";

    // save Variables
    List<Pokemon> box = new ArrayList<>();
    private Pokemon pokemon[] = new Pokemon[6];
    HashMap<Integer, Integer> itemCount = new HashMap<>();
    HashMap<Integer, Integer> equipmentCount = new HashMap<>();
    
    // runtime Variables (= Don't need to save)
    int selectedPokemonIdx = 0, pp = 0;
    Map<Integer, Consumer<Event>> eventListeners = new HashMap<>();
    Boolean runningEventLoop = false;
    Boolean autoGameing = false;
    Deque<Event> eventList = new ArrayDeque<>();
    List<Event> raisedEventList = null;
    Pokemon enemyPokemon = null;
    Status enemyStatus = Status.get(0), status = Status.get(0);
    boolean currentPokemonCanAct = true, enemyPokemonCanAct = true;
    boolean itemFinishedBattle = false;
    
    
    private GameManager() {
        load();
    }

    public static GameManager getInstance() {
        if (gm == null) gm = new GameManager();
        return gm;
    }

    public void save() {
        // TODO
    }

    public void load() {
        // TODO remove test code
        pokemon[0] = Pokemon.generate(151, 100);
        pokemon[1] = Pokemon.generate(1, 10);
        pokemon[1].setHealth(1);
        enemyPokemon = Pokemon.generate(151, 5);
        itemCount.put(1, 2);
        itemCount.put(4, 5);
        itemCount.put(5, 2);
        itemCount.put(6, 3);
        itemCount.put(7, 1);
        itemCount.put(8, 2);
        // end test code
        
        
        for (int i = 0; i < 6; i++) {
            if (pokemon[i] == null) continue;
            if (pokemon[i].getHealth() == 0) continue;
            selectedPokemonIdx = i;
            break;
        }
        //TODO
    }

    public void startLoop() {
        Event e;
        while (true) {
            if (eventList.isEmpty()) {
                try {
                    Thread.sleep(10);
                    continue;
                } catch (InterruptedException e1) {

                }
            }
            
            e = eventList.removeFirst();
            raisedEventList = new ArrayList<>();
            System.out.println("Event Raised! Event type: " + e.type);
            for (Consumer<Event> el : eventListeners.values()) {
                el.accept(e);
            }

            // TODO GameManager 레벨에서 이벤트로 인해 처리해야 할 것 진행
            switch(e.type) {
                case Event.EVENT_TYPE.BATTLE_START:
                    enemyPokemon = e.pokemon;
                    raiseEvent(Event.newTextEvent("야생의 " + e.pokemon.name + "을/를 마주쳤다!"));
                    raiseEvent(Event.newTurnStartEvent());
                    break;
                case Event.EVENT_TYPE.ATTACK:
                    enemyPokemon.setHealth(enemyPokemon.getHealth() - e.damage);
                    addPP(5);
                    System.out.println(e.damage);
                    raiseEvent(Event.newTextEvent(e.damage + "의 대미지를 입혔다!"));
                    raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.CLEAR_EVENT_QUEUE:
                    eventList.clear();
                    break;
                case Event.EVENT_TYPE.ITEM:
                    itemCount.put(e.item.id, itemCount.get(e.item.id) - 1);
                    itemFinishedBattle = false;
                    e.item.use.accept(e.pokemon);
                    if (!itemFinishedBattle) raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.EXIT:
                    save();
                    System.exit(0);
                    break;
                case Event.EVENT_TYPE.CHANGE:
                    raiseEvent(Event.newTextEvent(pokemon[e.idx].name + "! 너로 정했다!"));
                    selectedPokemonIdx = e.idx;
                    raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.TURN_END:
                    if (enemyPokemon.getHealth() == 0) {
                        raiseEvent(Event.newEnemyDeadEvent());
                    } else {
                        raiseEvent(Event.newEnemyTurnStartEvent());
                    }
                    break;
                case Event.EVENT_TYPE.ENEMY_DEAD:
                    // TODO CALCULATE XP
                    giveDefeatDrop();
                    raiseEvent(Event.newBattleEndEvent(enemyPokemon.getXp() / 30 + enemyPokemon.getLevel()));
                    break;
                case Event.EVENT_TYPE.ENEMY_TURN_START:
                    // TODO what enemy do (= AI);
                    if (!enemyPokemonCanAct) {
                        raiseEvent(Event.newEnemyTurnEndEvent());
                        break;
                    }
                    int dmg = enemyPokemon.getAttackDamage();
                    dmg += new Random().nextInt() % (dmg / 5);
                    raiseEvent(Event.newEnemyAttackEvent(dmg));
                    break;
                case Event.EVENT_TYPE.ENEMY_ATTACK:
                    getCurrentPokemon().setHealth(getCurrentPokemon().getHealth() - e.damage);
                    System.out.println(e.damage);
                    raiseEvent(Event.newTextEvent(e.damage + "의 대미지를 입었다!"));
                    raiseEvent(Event.newEnemyTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.ENEMY_TURN_END:
                    if (getCurrentPokemon().getHealth() == 0) {
                        raiseEvent(Event.newDeadEvent());
                    } else {
                        raiseEvent(Event.newTurnStartEvent());
                    }
                    break;
                case Event.EVENT_TYPE.DEAD:
                    boolean flag = true;
                    raiseEvent(Event.newTextEvent(getCurrentPokemon().name + "이/가 쓰러졌다"));
                    for (int i = 0; i < 6; i++) {
                        if (pokemon[i] == null) continue;
                        if (pokemon[i].getHealth() != 0) flag = false;
                    }
                    if (flag) {
                        raiseEvent(Event.newTextEvent("눈 앞이 캄캄해졌다"));
                        // TODO 포켓몬 센터로 이동
                        // TODO 테스트코드 제거
                        for (int i = 0; i < 6; i++) {
                            if (pokemon[i] == null) continue;
                            pokemon[i].setHealth(pokemon[i].getMaxHealth());
                        }
                        // 테스트 코드 끝
                    }
                    break;
                case Event.EVENT_TYPE.BATTLE_END:
                    if (e.xp != 0) {
                        raiseEvent(Event.newTextEvent(enemyPokemon.name + "를 쓰러뜨렸다!\n" + e.xp + "의 경험치를 얻었다."));
                        getCurrentPokemon().setXp(getCurrentPokemon().getXp() + e.xp);
                    }
                    raiseEvent(Event.newBattleStartEvent(Pokemon.generateRandom()));
                    break;
                    case Event.EVENT_TYPE.DELAY:
                    try {
                        Thread.sleep(e.milliseconds);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                case Event.EVENT_TYPE.TURN_START:
                case Event.EVENT_TYPE.NOTHING:
                case Event.EVENT_TYPE.TEXT:
                    break;
                default:
                    System.out.println("Unhandled Event in GameManager: " + e.type);
            }

            processPendingStatusClear();

            for (int i = raisedEventList.size() - 1; i >= 0; i--) {
                eventList.addFirst(raisedEventList.get(i));
            }
            raisedEventList = null;
        }
    }

    private void processPendingStatusClear() {
        if (status != null && !status.isNone() && status.shouldClear()) {
            setStatus(Status.get(0));
        }
        if (enemyStatus != null && !enemyStatus.isNone() && enemyStatus.shouldClear()) {
            setEnemyStatus(Status.get(0));
        }
    }

    public static void raiseEvent(Event e) {
        GameManager gm = getInstance();
        if (gm.raisedEventList != null) {
            gm.raisedEventList.add(e);
        } else {
            gm.eventList.addLast(e);
        }
    }

    public static Integer registEventListener(Consumer<Event> el) {
        GameManager gm = getInstance();
        Integer idx = 0;
        while (gm.eventListeners.containsKey(idx)) idx++;
        gm.eventListeners.put(idx, el);
        return idx;
    }

    public static void unRegistEventListener(Integer idx) {
        GameManager gm = getInstance();
        if (gm.eventListeners.containsKey(idx)) gm.eventListeners.remove(idx);
    }
    
    public static Pokemon getCurrentPokemon() {
        GameManager gm = getInstance();
        return gm.pokemon[gm.selectedPokemonIdx];
    }

    public static Pokemon getPokemon(int idx) {
        return getInstance().pokemon[idx];
    }

    public static void setEnemyStatus(Status s) {
        GameManager gm = getInstance();
        gm.enemyStatus.deActivate();
        gm.enemyStatus = s;
        s.activate();
        gm.enemyPokemonCanAct = true;
    }

    public static void setStatus(Status s) {
        GameManager gm = getInstance();
        gm.status.deActivate();
        gm.status = s;
        s.activate();
        gm.currentPokemonCanAct = true;
    }

    public static boolean canCurrentPokemonAct() {
        return getInstance().currentPokemonCanAct;
    }

    public static void setCurrentPokemonCanAct(boolean value) {
        getInstance().currentPokemonCanAct = value;
    }

    public static boolean canEnemyPokemonAct() {
        return getInstance().enemyPokemonCanAct;
    }

    public static void setEnemyPokemonCanAct(boolean value) {
        getInstance().enemyPokemonCanAct = value;
    }

    public void tryCapture(int baseChance, boolean guaranteed) {
        int missingHealth = enemyPokemon.getMaxHealth() - enemyPokemon.getHealth();
        int chance = Math.min(95, baseChance + missingHealth * 50 / enemyPokemon.getMaxHealth());

        raiseEvent(Event.newTextEvent("."));
                
        raiseEvent(Event.newTextEvent(".."));
        raiseEvent(Event.newTextEvent("..."));
        
        if (!guaranteed && new Random().nextInt(100) >= chance) {
            raiseEvent(Event.newTextEvent("포켓몬이 볼에서 빠져나왔다!"));
            return;
        }

        boolean placedInParty = false;
        for (int i = 0; i < pokemon.length; i++) {
            if (pokemon[i] != null) continue;
            pokemon[i] = enemyPokemon;
            placedInParty = true;
            break;
        }
        if (!placedInParty) box.add(enemyPokemon);

        raiseEvent(Event.newTextEvent(enemyPokemon.name + "을/를 잡았다!"));
        if (!placedInParty) {
            raiseEvent(Event.newTextEvent(enemyPokemon.name + "은/는 박스로 전송되었다."));
        }
        itemFinishedBattle = true;
        raiseEvent(Event.newBattleEndEvent(0));
    }

    private void giveDefeatDrop() {
        Random random = new Random();
        int dropRoll = random.nextInt(100);

        if (dropRoll < 30) {
            List<Item> items = new ArrayList<>();
            for (Item item : Item.ITEM_TABLE) {
                if (item.id != 0) items.add(item);
            }
            if (items.isEmpty()) return;
            Item item = items.get(random.nextInt(items.size()));
            itemCount.put(item.id, getItemCount(item.id) + 1);
            raiseEvent(Event.newTextEvent(item.name + "을/를 획득했다!"));
        } else if (dropRoll < 40) {
            List<Equipment> equipments = new ArrayList<>();
            for (Equipment equipment : Equipment.EQUIPMENT_TABLE) {
                if (equipment.id != 0) equipments.add(equipment);
            }
            if (equipments.isEmpty()) return;
            Equipment equipment = equipments.get(random.nextInt(equipments.size()));
            int currentCount = equipmentCount.containsKey(equipment.id) ? equipmentCount.get(equipment.id) : 0;
            equipmentCount.put(equipment.id, currentCount + 1);
            raiseEvent(Event.newTextEvent(equipment.name + "을/를 획득했다!"));
        }
    }

    public static int getItemCount(int id) {
        Integer rtn = getInstance().itemCount.get(id);
        return ((rtn == null) ? 0:rtn);
    }

    public static void setItmeCount(int id, int value) {
        getInstance().itemCount.put(id, value);
    }

    public static Pokemon getEnemyPokemon() {
        return getInstance().enemyPokemon;
    }
    
    public static int getPP() {
        return getInstance().pp;
    }

    public static void setPP(int value) {
        getInstance().pp = Math.max(0, Math.min(40, value));
    }

    public static void addPP(int value) {
        setPP(getPP() + value);
    }
}

