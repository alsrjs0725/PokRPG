import java.util.function.Consumer;

class Item implements Cloneable{
    int id;
    String name, description;
    Consumer<Pokemon> use;
    
    public static Item ITEM_TABLE[] = {  // id는 중복되면 안됨.
        new Item("NONE", "", 0, (Pokemon p) -> {
            // nothing
        }),
        new Item("상처약", "HP를 20 회복한다", 1, (Pokemon p) -> {
            int beforeHp = p.getHealth();
            p.setHealth(beforeHp + 20);
            int healedHp = p.getHealth() - beforeHp;
            GameManager.raiseEvent(Event.newTextEvent(
                "상처약을 사용하였다. \n" + healedHp + "를 회복하였다"
            ));
        }),
        new Item("고급 상처약", "HP를 50 회복한다", 2, (Pokemon p) -> {
            int beforeHp = p.getHealth();
            p.setHealth(beforeHp + 50);
            int healedHp = p.getHealth() - beforeHp;
            GameManager.raiseEvent(Event.newTextEvent(
                "고급 상처약을 사용하였다. \n" + healedHp + "를 회복하였다"
            ));
        }),
        new Item("만능 회복약", "모든 상태이상을 제거한다", 3, (Pokemon p) -> {
            Status prv = GameManager.getInstance().status;
            prv.deActivate();
            GameManager.getInstance().status = Status.get(0);
            GameManager.raiseEvent(Event.newTextEvent(
                "만능 회복약을 사용했다! 상태이상 " + prv.name + " 이/가 제거되었다!"
            ));
        }),
    };
    
    Item(String name, String description, int id, Consumer<Pokemon> use) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.use = use;
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            
        }
        return obj;
    }

    public static Item get(int id) { 
        for (int i = 0; i < ITEM_TABLE.length; i++) {
            if (ITEM_TABLE[i].id == id) return (Item) ITEM_TABLE[i].clone();
        } return null;
    }
}

