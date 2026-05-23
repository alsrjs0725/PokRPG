import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.HashMap;

public class GameManager {
    private static GameManager gm;
    private static final String DEFAULT_SAVE_PATH = ".\\save.json";

    // save Variables
    List<Pokemon> box = new ArrayList<>();
    Pokemon pokemon[] = new Pokemon[6];
    int itemCount[];
    int Equipment[];
    
    // runtime Variable (= Don't need to save)
    int selectedPokemonIdx = 0, pp = 0;
    Map<Integer, Consumer<Event>> eventListeners = new HashMap<>();
    Boolean runningEventLoop = false;
    Boolean autoGameing = false;
    Deque<Event> eventList = new ArrayDeque<>();
    List<Event> raisedEventList = null;
    Pokemon enemyPokemon = null;
    Status enemyStatus = Status.get(0), myStatus = Status.get(0);
    
    private GameManager() {
        load(DEFAULT_SAVE_PATH);
    }

    public static GameManager getInstance() {
        if (gm == null) gm = new GameManager();
        return gm;
    }

    public void save(String path) {
        // TODO
    }

    public void load(String path) {
        // TODO remove test code
        pokemon[0] = Pokemon.generate(151, 100);
        pokemon[1] = Pokemon.generate(1, 10);
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
                    raiseEvent(Event.newTurnStartEvent());
                    break;
                case Event.EVENT_TYPE.ATTACK:
                    enemyPokemon.setHealth(enemyPokemon.getHealth() - e.damage);
                    System.out.println(e.damage);
                    raiseEvent(Event.newTextEvent(e.damage + "의 대미지를 입혔다!"));
                    raiseEvent(Event.newTurnStartEvent());
                    break;
                case Event.EVENT_TYPE.CLEAR_EVENT_QUEUE:
                    eventList.clear();
                    break;
                case Event.EVENT_TYPE.ITEM:
                    e.item.use.accept(e.pokemon);
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
}

