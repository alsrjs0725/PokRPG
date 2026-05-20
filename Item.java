import java.util.function.Consumer;

class Item {
    int id;
    String name;
    Consumer<Pokemon> use;
    
    public static Item ITEM_TABLE[] = {

    };
    
    Item(String name, int id, Consumer<Pokemon> use) {
        this.name = name;
        this.id = id;
        this.use = use;
    }

    void use() {
        Event e = new Event();
        e.type = Event.EVENT_TYPE.ITEM;
        e.item = this;
        GameManager.getInstance().raiseEvent(e);
    }
}

