import java.util.function.Consumer;

class Item {
    int id;
    String name;
    Consumer<Pokemon> use;
    
    public static Item ITEM_TABLE[] = {
        new Item("TestItem", 0, (Pokemon p) -> {
            p.setHealth(p.getMaxHealth());
        }),
    };
    
    Item(String name, int id, Consumer<Pokemon> use) {
        this.name = name;
        this.id = id;
        this.use = use;
    }
}

