class Item {
    int id;
    public static String NAME_TABLE[] = {
        "TestItem",
    };

    void use() {
        Event e = new Event();
        e.type = Event.EVENT_TYPE.ITEM;
        e.item = this;
        GameManager.getInstance().raiseEvent(e);
    }
}

