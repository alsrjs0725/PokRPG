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
        pokemon[0] = new Pokemon(151, 1, 1, 1, 1, new Skill[2], 0);
        pokemon[1] = new Pokemon(1, 1, 1, 1, 1, new Skill[2], 0);
        enemyPokemon = new Pokemon(151, 1, 1, 1, 1, new Skill[2], 0);
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
                    raiseEvent(Event.newTextEvent(e.damage + "의 대미지를 입혔다!"));
                    raiseEvent(Event.newTurnStartEvent());
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

    public void raiseEvent(Event e) {
        if (raisedEventList != null) {
            raisedEventList.add(e);
        } else {
            eventList.addLast(e);
        }
    }

    public Integer registEventListener(Consumer<Event> el) {
        Integer idx = 0;
        while (eventListeners.containsKey(idx)) idx++;
        eventListeners.put(idx, el);
        return idx;
    }

    public void unRegistEventListener(Integer idx) {
        if (eventListeners.containsKey(idx)) eventListeners.remove(idx);
    }
}

