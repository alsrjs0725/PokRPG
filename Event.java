public class Event {
    enum EVENT_TYPE {       // 사용하는 변수 목록(이벤트 발생시 변경해야 하는 변수 목록, 나머지는 null), 
        NOTHING,            //
        BATTLE_START,       // pokemon(enemy)
        TURN_START,         //
        ATTACK,             // damage
        SKILL,              // skill
        ITEM,               // item, pokemon(selected pokemon)
        CHANGE,             // idx (= change to GameManger.getInstance().pokemon[idx])
        DEAD,               // 
        TURN_END,           // 
        ENEMY_TURN_START,   //
        ENEMY_ATTACK,       // damage
        ENEMY_SKILL,        // skill
        ENEMY_TURN_END,     //
        ENEMY_DEAD,         //
        BATTLE_END,         // xp
        IN_BOX,             // idx (=selectedPokemonIdx)
        OUT_BOX,            // pokemon, idx (=selectedPokemonIdx)
        TEXT,               // show text
        EXIT,               // 
        CLEAR_EVENT_QUEUE   // BE CAREFUL!
    };

    public EVENT_TYPE type;
    public Item item = null;
    public Equipment equipment = null;
    public Integer idx = null, damage = null, xp=null;
    public Pokemon pokemon = null;
    public String text = null;
    public Skill skill;

    public static Event newNothingEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.NOTHING;
        return e;
    }

    public static Event newBattleStartEvent(Pokemon enemy) {
        Event e = new Event();
        e.type = EVENT_TYPE.BATTLE_START;
        e.pokemon = enemy;
        return e;
    }

    public static Event newTurnStartEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.TURN_START;
        return e;
    }

    public static Event newAttackEvent(int damage) {
        Event e = new Event();
        e.type = EVENT_TYPE.ATTACK;
        e.damage = damage;
        return e;
    }

    public static Event newSkillEvent(Skill skill) {
        Event e = new Event();
        e.type = EVENT_TYPE.SKILL;
        e.skill = skill;
        return e;
    }

    public static Event newItemEvent(Item item, Pokemon pokemon) {
        Event e = new Event();
        e.type = EVENT_TYPE.ITEM;
        e.item = item;
        e.pokemon = pokemon;
        return e;
    }

    public static Event newChangeEvent(int idx) {
        Event e = new Event();
        e.type = EVENT_TYPE.CHANGE;
        e.idx = idx;
        return e;
    }

    public static Event newDeadEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.DEAD;
        return e;
    }

    public static Event newTurnEndEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.TURN_END;
        return e;
    }

    public static Event newEnemyTurnStartEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.ENEMY_TURN_START;
        return e;
    }

    public static Event newEnemyAttackEvent(int damage) {
        Event e = new Event();
        e.type = EVENT_TYPE.ENEMY_ATTACK;
        e.damage = damage;
        return e;
    }

    public static Event newEnemySkillEvent(Skill skill) {
        Event e = new Event();
        e.type = EVENT_TYPE.ENEMY_SKILL;
        e.skill = skill;
        return e;
    }

    public static Event newEnemyTurnEndEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.ENEMY_TURN_END;
        return e;
    }

    public static Event newEnemyDeadEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.ENEMY_DEAD;
        return e;
    }

    public static Event newBattleEndEvent(int xp) {
        Event e = new Event();
        e.type = EVENT_TYPE.BATTLE_END;
        e.xp = xp;
        return e;
    }

    public static Event newInBoxEvent(int idx) {
        Event e = new Event();
        e.type = EVENT_TYPE.IN_BOX;
        e.idx = idx;
        return e;
    }

    public static Event newOutBoxEvent(Pokemon pokemon, int idx) {
        Event e = new Event();
        e.type = EVENT_TYPE.OUT_BOX;
        e.pokemon = pokemon;
        e.idx = idx;
        return e;
    }

    public static Event newTextEvent(String text) {
        Event e = new Event();
        e.type = EVENT_TYPE.TEXT;
        e.text = text;
        return e;
    }

    public static Event newClearEventQueueEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.CLEAR_EVENT_QUEUE;
        return e;
    }

    public static Event newExitEvent() {
        Event e = new Event();
        e.type = EVENT_TYPE.EXIT;
        return e;
    }

    public static void raiseAllEvent() {
        GameManager.raiseEvent(Event.newNothingEvent());
        GameManager.raiseEvent(Event.newBattleStartEvent(Pokemon.generate(151, 3)));
        GameManager.raiseEvent(Event.newTurnStartEvent());
        GameManager.raiseEvent(Event.newAttackEvent(3));
        GameManager.raiseEvent(Event.newSkillEvent(Skill.get(1)));
        GameManager.raiseEvent(Event.newItemEvent(Item.get(0), GameManager.getCurrentPokemon()));
        GameManager.raiseEvent(Event.newChangeEvent(1));
        GameManager.raiseEvent(Event.newDeadEvent());
        GameManager.raiseEvent(Event.newTurnEndEvent());
        GameManager.raiseEvent(Event.newEnemyTurnStartEvent());
        GameManager.raiseEvent(Event.newEnemyAttackEvent(3));
        GameManager.raiseEvent(Event.newEnemySkillEvent(Skill.get(0)));
        GameManager.raiseEvent(Event.newEnemyTurnEndEvent());
        GameManager.raiseEvent(Event.newEnemyDeadEvent());
        GameManager.raiseEvent(Event.newBattleEndEvent(10000));
        GameManager.raiseEvent(Event.newInBoxEvent(1));
        GameManager.raiseEvent(Event.newOutBoxEvent(Pokemon.generate(151, 3), 2));
        GameManager.raiseEvent(Event.newTextEvent("HELLO"));
        GameManager.raiseEvent(Event.newClearEventQueueEvent());
    }
}
