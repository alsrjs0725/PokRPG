import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Arrays;

public class Equipment{
    String name;
    Integer id, eventListenerRegistedID;
    public static String NAME_TABLE[] = {
        "TestEqluipment",
    };

    public static List<Consumer<Event>> EVENT_LISTENER_TABLE = new ArrayList<>(Arrays.asList(
        (Event e) -> {
            if (e.type == Event.EVENT_TYPE.ATTACK) e.damage = 9999999;
            if (e.type == Event.EVENT_TYPE.ENEMY_ATTACK) e.damage = 1;
        }
    ));


    Equipment(int id) {
        this.id = id;
        this.name = NAME_TABLE[id];
    }

    public void activate() {
        if (eventListenerRegistedID != null) return; 
        eventListenerRegistedID = GameManager.getInstance().registEventListener(EVENT_LISTENER_TABLE.get(id));
    }
    
    public void deActivate() {
        if (eventListenerRegistedID == null) return;
        GameManager.getInstance().unRegistEventListener(eventListenerRegistedID);
        eventListenerRegistedID = null;
    }



}

