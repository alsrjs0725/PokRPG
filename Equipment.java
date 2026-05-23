import java.util.function.Consumer;

public class Equipment implements Cloneable{
    String name, description;
    Integer id, eventListenerRegistedID;
    Consumer<Event> eventListener;

    public static Equipment EQUIPMENT_TABLE[] = {
        new Equipment("NONE", "", 0, (Event e) -> {  // ID 0

        }),
        new Equipment("DEBUG EQUIPMENT", "", 1, (Event e) -> {  // ID 1
            if (e.type == Event.EVENT_TYPE.ATTACK) e.damage = 9999;
            if (e.type == Event.EVENT_TYPE.ENEMY_ATTACK) e.damage = 1;
        }),
    };


    Equipment(String name, String description, int id, Consumer<Event> el) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.eventListener = el;
    }

    public void activate() {
        if (eventListenerRegistedID != null) return; 
        eventListenerRegistedID = GameManager.registEventListener(eventListener);
    }
    
    public void deActivate() {
        if (eventListenerRegistedID == null) return;
        GameManager.unRegistEventListener(eventListenerRegistedID);
        eventListenerRegistedID = null;
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            
        }
        return obj;
    }

    public static Equipment get(int id) {
        for (int i = 0; i < EQUIPMENT_TABLE.length; i++) {
            if (EQUIPMENT_TABLE[i].id == id) return (Equipment) EQUIPMENT_TABLE[i].clone();
        } return null;
    }

}

