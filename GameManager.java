import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.HashMap;

public class GameManager {
    private static GameManager gm;
    private static final String SAVE_FILE = "./save.json";

    // save Variables
    List<Pokemon> box = new ArrayList<>();
    Pokemon pokemon[] = new Pokemon[6];
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
        pokemon[0].setHealth(20);
        pokemon[1] = Pokemon.generate(1, 10);
        pokemon[1].setHealth(1);
        enemyPokemon = Pokemon.generate(151, 5);
        // end test code
        
        
        for (int i = 0; i < 6; i++) {
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
                    System.out.println(e.damage);
                    raiseEvent(Event.newTextEvent(e.damage + "의 대미지를 입혔다!"));
                    raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.CLEAR_EVENT_QUEUE:
                    eventList.clear();
                    break;
                case Event.EVENT_TYPE.ITEM:
                    e.item.use.accept(e.pokemon);
                    raiseEvent(Event.newTurnEndEvent());
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
                    raiseEvent(Event.newBattleEndEvent(enemyPokemon.getXp() / 30 + enemyPokemon.getLevel()));
                    break;
                case Event.EVENT_TYPE.ENEMY_TURN_START:
                    // TODO what enemy do (= AI);
                    raiseEvent(Event.newEnemyAttackEvent(enemyPokemon.getAttackDamage()));
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
                case Event.EVENT_TYPE.TURN_START:
                case Event.EVENT_TYPE.NOTHING:
                case Event.EVENT_TYPE.TEXT:
                    break;
                default:
                    System.out.println("Unhandled Event in GameManager: " + e.type);
            }

            for (int i = raisedEventList.size() - 1; i >= 0; i--) {
                eventList.addFirst(raisedEventList.get(i));
            }
            raisedEventList = null;
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

    public static void setEnemyStatus(Status s) {
        GameManager gm = getInstance();
        gm.enemyStatus.deActivate();
        gm.enemyStatus = s;
        s.activate();
    }

    public static void setStatus(Status s) {
        GameManager gm = getInstance();
        gm.status.deActivate();
        gm.status = s;
        s.activate();
    }
}

