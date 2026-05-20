import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Arrays;

public class Equipment{
    String name;
    Integer id, eventListenerRegistedID;
    Consumer<Event> eventListener;

    public static Equipment EQUIPMENT_TABLE[] = {
        new Equipment("Test Equipment", 0, (Event e) -> {  // ID 0
            if (e.type == Event.EVENT_TYPE.ATTACK) e.damage = 9999999;
            if (e.type == Event.EVENT_TYPE.ENEMY_ATTACK) e.damage = 1;
        }),
    };


    Equipment(String name, int id, Consumer<Event> el) {
        this.name = name;
        this.id = id;
        this.eventListener = el;
    }

    public void activate() {
        if (eventListenerRegistedID != null) return; 
        eventListenerRegistedID = GameManager.getInstance().registEventListener(eventListener);
    }
    
    public void deActivate() {
        if (eventListenerRegistedID == null) return;
        GameManager.getInstance().unRegistEventListener(eventListenerRegistedID);
        eventListenerRegistedID = null;
    }



}

