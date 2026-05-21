import java.util.function.Consumer;

class Item {
    int id;
    String name;
    Consumer<Pokemon> use;
    
    public static Item ITEM_TABLE[] = {  // id는 중복되면 안됨.
        new Item("NONE", 0, (Pokemon p) -> {
            // nothing
        }),
        new Item("TEST ITEM", 1, (Pokemon p) -> {
            p.setHealth(p.getHealth());
        }),
    };
    
    Item(String name, int id, Consumer<Pokemon> use) {
        this.name = name;
        this.id = id;
        this.use = use;
    }

    public static Item get(int id) { 
        for (int i = 0; i < ITEM_TABLE.length; i++) {
            if (ITEM_TABLE[i].id == id) return ITEM_TABLE[i];
        } return null;
    }

    

}

