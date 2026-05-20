import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.HashMap;

public class GameManager {
    private static GameManager gm;
    private static final String DEFAULT_SAVE_PATH = ".\\save.json";

    // save Variables
    List<Pokemon> box = new ArrayList<>();
    Pokemon pokemon[] = new Pokemon[8];
    int selectedPokemonIdx = 0;
    
    // runtime Variable (= Don't need to save)
    Map<Integer, Consumer<Event>> eventListeners = new HashMap<>();
    Boolean runningEventLoop = false;
    Boolean autoGameing = false;
    List<Event> eventList = new ArrayList<>();
    
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
        //TODO
    }

    public void raiseEvent(Event e) {
        eventList.add(e);
        if (runningEventLoop) return;
        runningEventLoop = true;
        while (!eventList.isEmpty()) {
            e = eventList.removeLast();
            for (Consumer<Event> el : eventListeners.values()) {
                el.accept(e);
            }
            // TODO GameManager 레벨에서 이벤트로 인해 처리해야 할 것 진행
        }
        runningEventLoop = false;
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

