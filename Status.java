import java.util.function.Consumer;

public class Status implements Cloneable{  // 상태이상
    String name;
    Integer id, eventListenerRegistedID = null;
    Consumer<Event> eventListener;

    
    private static Status STATUS_TABLE[] = {
        new Status("NONE", 0, (Event e) -> {

        }),
        new Status("DEBUG STATUS", 1, (Event e) -> {
            if (e.type == Event.EVENT_TYPE.TURN_START) {
                GameManager gm = GameManager.getInstance();
                Pokemon p = gm.pokemon[gm.selectedPokemonIdx];
                p.setHealth(p.getHealth() + (p.getMaxHealth() / 16));
            }
        }),
    };

    Status(String name, int id, Consumer<Event> eventListener) {
        this.name = name;
        this.id = id;
        this.eventListener = eventListener;
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            
        }
        return obj;
    }

    public void activate() {
        if (eventListenerRegistedID != null) return;
        eventListenerRegistedID = GameManager.registEventListener(eventListener);
    }

    public void deActivate() {
        if (eventListenerRegistedID == null) return;
        GameManager.unRegistEventListener(eventListenerRegistedID);
    }

    public static Status get(int id) {
        for (int i = 0; i < STATUS_TABLE.length; i++) {
            if (STATUS_TABLE[i].id == id) return (Status) STATUS_TABLE[i].clone();
        } return null;
    }
}
