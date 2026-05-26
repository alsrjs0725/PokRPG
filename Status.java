import java.util.Random;
import java.util.function.Consumer;

public class Status implements Cloneable{  // 상태이상
    String name;
    Integer id, eventListenerRegistedID = null;
    Consumer<Event> eventListener;
    int remainingTurns = 0;
    boolean clearRequested = false;
    boolean clearOnTurnEnd = false;

    private static final Random RANDOM = new Random();

    private static Status STATUS_TABLE[] = {
        new Status("NONE", 0, (Event e) -> {

        }),
        new Status("독", 1, null) {
            @Override
            protected void handleEvent(Event e) {
                if (!isTurnStartEvent(e)) return;
                Pokemon p = getTargetPokemon();
                int damage = Math.max(1, p.getMaxHealth() / 8);
                p.setHealth(p.getHealth() - damage);
                GameManager.raiseEvent(Event.newTextEvent(p.name + "은/는 독에 걸려 있다!\n" + damage + "의 피해를 입었다!"));
            }
        },
        new Status("화상", 2, null) {
            @Override
            protected void handleEvent(Event e) {
                if (isTurnStartEvent(e)) {
                    Pokemon p = getTargetPokemon();
                    int damage = Math.max(1, p.getMaxHealth() / 16);
                    p.setHealth(p.getHealth() - damage);
                    GameManager.raiseEvent(Event.newTextEvent(p.name + "은/는 화상을 입었다!\n" + damage + "의 피해를 입었다!"));
                    return;
                }

                if ((e.type == Event.EVENT_TYPE.ATTACK && !isEnemySide()) || (e.type == Event.EVENT_TYPE.ENEMY_ATTACK && isEnemySide())) {
                    if (e.damage != null) e.damage = Math.max(1, e.damage / 2);
                }
            }
        },
        new Status("마비", 3, null) {
            @Override
            protected void handleEvent(Event e) {
                if (!isTurnStartEvent(e)) return;

                setCanAct(true);
                if (RANDOM.nextInt(100) < 25) {
                    setCanAct(false);
                    GameManager.raiseEvent(Event.newTextEvent(getTargetPokemon().name + "은/는 마비되어 움직일 수 없다!"));
                }
            }
        },
        new Status("잠듦", 4, null) {
            @Override
            protected void onActivate() {
                remainingTurns = 2 + RANDOM.nextInt(3);
                clearRequested = false;
                clearOnTurnEnd = false;
            }

            @Override
            protected void handleEvent(Event e) {
                boolean turnStartEvent = isTurnStartEvent(e);
                boolean turnEndEvent = (isEnemySide() && e.type == Event.EVENT_TYPE.ENEMY_TURN_END)
                    || (!isEnemySide() && e.type == Event.EVENT_TYPE.TURN_END);
                if (!turnStartEvent && !turnEndEvent) return;

                if (turnStartEvent) {
                    setCanAct(false);
                    if (remainingTurns > 0) {
                        GameManager.raiseEvent(Event.newTextEvent(getTargetPokemon().name + "은/는 잠들어 있다!"));
                        remainingTurns--;
                    }
                    if (remainingTurns <= 0) {
                        clearOnTurnEnd = true;
                    }
                }

                if (turnEndEvent && clearOnTurnEnd) {
                    clearOnTurnEnd = false;
                    clearRequested = true;
                    GameManager.raiseEvent(Event.newTextEvent(getTargetPokemon().name + "은/는 잠에서 깨어났다!"));
                }
            }
        },
        new Status("얼음", 5, null) {
            @Override
            protected void handleEvent(Event e) {
                if (!isTurnStartEvent(e)) return;

                if (RANDOM.nextInt(100) < 50) {
                    setCanAct(true);
                    clearRequested = true;
                    GameManager.raiseEvent(Event.newTextEvent(getTargetPokemon().name + "의 얼음이 녹았다!"));
                } else {
                    setCanAct(false);
                    GameManager.raiseEvent(Event.newTextEvent(getTargetPokemon().name + "은/는 얼어붙어 있다!"));
                }
            }
        },
    };

    Status(String name, int id, Consumer<Event> eventListener) {
        this.name = name;
        this.id = id;
        this.eventListener = eventListener;
    }

    protected void handleEvent(Event e) {
        if (eventListener != null) eventListener.accept(e);
    }

    protected void onActivate() {
    }

    protected boolean isNone() {
        return id != null && id == 0;
    }

    protected boolean isEnemySide() {
        return GameManager.getInstance().enemyStatus == this;
    }

    protected boolean isTurnStartEvent(Event e) {
        return (!isEnemySide() && e.type == Event.EVENT_TYPE.TURN_START)
            || (isEnemySide() && e.type == Event.EVENT_TYPE.ENEMY_TURN_START);
    }

    protected Pokemon getTargetPokemon() {
        return isEnemySide() ? GameManager.getEnemyPokemon() : GameManager.getCurrentPokemon();
    }

    protected void setCanAct(boolean canAct) {
        if (isEnemySide()) {
            GameManager.setEnemyPokemonCanAct(canAct);
        } else {
            GameManager.setCurrentPokemonCanAct(canAct);
        }
    }

    public boolean shouldClear() {
        return clearRequested;
    }

    public void clearRequestedFlag() {
        clearRequested = false;
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            
        }
        return obj;
    }

    public void activate() {
        if (eventListenerRegistedID != null || isNone()) return;
        clearRequested = false;
        onActivate();
        eventListenerRegistedID = GameManager.registEventListener(this::handleEvent);
    }

    public void deActivate() {
        if (eventListenerRegistedID == null) return;
        GameManager.unRegistEventListener(eventListenerRegistedID);
        eventListenerRegistedID = null;
        clearRequested = false;
        clearOnTurnEnd = false;
        remainingTurns = 0;
    }

    public static Status get(int id) {
        for (int i = 0; i < STATUS_TABLE.length; i++) {
            if (STATUS_TABLE[i].id == id) return (Status) STATUS_TABLE[i].clone();
        }
        return (Status) STATUS_TABLE[0].clone();
    }
}
