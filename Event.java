public class Event {
    enum EVENT_TYPE {       // 사용하는 변수 목록(이벤트 발생시 변경해야 하는 변수 목록, 나머지는 null), 
                            // GameManager에서 raise event 이전에 유효한 변수가 다 있는지 CHECK
        NOTHING,            //
        BATTLE_START,       // pokemon(enemy)
        TURN_START,         //
        ATTACK,             // damage
        SKILL,              // idx (=skill idx)
        ITEM,               // item, pokemon(selected pokemon)
        CHANGE,             // slotIdx
        DEAD,               // slotIdx
        TURN_END,           // 
        ENEMY_TURN_START,   //
        ENEMY_ATTACK,       // damage
        ENEMY_SKILL,        // idx (=skill idx)
        ENEMY_TURN_END,     //
        ENEMY_DEAD,         //
        BATTLE_END,         // xp
        CHANGE_POKEMON,     // idx (=selectedPokemonIdx)
        IN_BOX,             // idx (=selectedPokemonIdx)
        OUT_BOX,            // pokemon, idx (=selectedPokemonIdx)
        TEXT,               // show text
    };

    public EVENT_TYPE type;
    public Item item = null;
    public Equipment equipment = null;
    public Integer idx = null, damage = null, xp=null;
    public Pokemon pokemon = null;
    public String text = null;

    public static Event newBattleStartEvent(Pokemon enemy) {
        Event e = new Event();
        e.type = EVENT_TYPE.BATTLE_START;
        e.pokemon = enemy;

        return e;
    }

    public static Event newTextEvent(String text) {
        Event e = new Event();
        e.type = Event.EVENT_TYPE.TEXT;
        e.text = text;
        return e;
    }

    public static Event newAttackEvent(int damage) {
        Event e = new Event();
        e.type = Event.EVENT_TYPE.ATTACK;
        e.damage = damage;
        return e;
    }

    public static Event newEnemyAttackEvent(int damage) {
        Event e = new Event();
        e.type = Event.EVENT_TYPE.ENEMY_ATTACK;
        e.damage = damage;
        return e;
    }

    public static Event newTurnStartEvent() {
        Event e = new Event();
        e.type = Event.EVENT_TYPE.TURN_START;
        return e;
    }
}
