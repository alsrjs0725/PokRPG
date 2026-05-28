import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.HashMap;

public class GameManager {
    private static GameManager gm;
    private static final String SAVE_FILE = "./save.json";

    // save Variables
    List<Pokemon> box = new ArrayList<>();
    private Pokemon pokemon[] = new Pokemon[6];
    HashMap<Integer, Integer> itemCount = new HashMap<>();
    Set<Integer> pokemonDict = new HashSet<>();
    
    // runtime Variables (= Don't need to save)
    int selectedPokemonIdx = 0, pp = 0;
    Map<Integer, Consumer<Event>> eventListeners = new HashMap<>();
    Boolean runningEventLoop = false;
    Boolean autoGameing = false;
    Deque<Event> eventList = new ArrayDeque<>();
    List<Event> raisedEventList = null;
    Pokemon enemyPokemon = null;
    Status enemyStatus = Status.get(0), status = Status.get(0);
    boolean currentPokemonCanAct = true, enemyPokemonCanAct = true;
    boolean itemFinishedBattle = false;
    
    
    private GameManager() {}

    public static GameManager getInstance() {
        if (gm == null) {
            gm = new GameManager();
            gm.load();
        }
        return gm;
    }

    public void save() {
        boolean flag = true;
        for (int i = 0; i < 6; i++) if (pokemon[i] != null) flag = false;
        if (flag) return;
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"party\": [");
        appendPokemonList(json, pokemon);
        json.append("],\n  \"box\": [");
        appendPokemonList(json, box.toArray(new Pokemon[0]));
        json.append("],\n  \"itemCount\": ");
        appendCountMap(json, itemCount);
        json.append(",\n  \"pokemonDict\": ");
        appendIntSet(json, pokemonDict);
        json.append("\n}\n");

        try {
            Files.write(Paths.get(SAVE_FILE), json.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public void load() {
        // testLoad();

        Path savePath = Paths.get(SAVE_FILE);
        if (Files.exists(savePath)) {
            try {
                String json = new String(Files.readAllBytes(savePath), StandardCharsets.UTF_8);
                Map<String, Object> root = asObject(new JsonParser(json).parse());
                Pokemon[] loadedPokemon = readPokemonArray(root.get("party"), pokemon.length);
                List<Pokemon> loadedBox = readPokemonList(root.get("box"));
                HashMap<Integer, Integer> loadedItems = readCountMap(root.get("itemCount"));
                Set<Integer> loadedPokemonDict = readIntSet(root.get("pokemonDict"));

                pokemon = loadedPokemon;
                box = loadedBox;
                itemCount = loadedItems;
                pokemonDict = loadedPokemonDict;
                raiseEvent(Event.newBattleStartEvent(Pokemon.generateRandom()));
            } catch (IOException | RuntimeException e) {
                System.err.println("Failed to load save file; using test data: " + e.getMessage());
            }
        } else {
            pokemon[0] = Pokemon.generate(1, 1);
            enemyPokemon = Pokemon.generate(1, 1);
            raiseEvent(Event.newStartPokemonEvent());
        }

        selectUsablePokemon();
    }

    private void testLoad() {
        box.clear();
        pokemon = new Pokemon[6];
        itemCount.clear();
        pokemonDict.clear();

        pokemon[0] = Pokemon.generate(151, 100);
        pokemon[1] = Pokemon.generate(1, 10);
        pokemon[1].setHealth(1);
        enemyPokemon = Pokemon.generate(151, 5);
        itemCount.put(1, 2);
        itemCount.put(4, 5);
        itemCount.put(5, 2);
        itemCount.put(6, 3);
        itemCount.put(7, 1);
        itemCount.put(8, 2);
    }

    private void selectUsablePokemon() {
        selectedPokemonIdx = 0;
        for (int i = 0; i < 6; i++) {
            if (pokemon[i] == null) continue;
            if (pokemon[i].getHealth() == 0) continue;
            selectedPokemonIdx = i;
            break;
        }
    }

    private static void appendPokemonList(StringBuilder json, Pokemon[] pokemonList) {
        for (int i = 0; i < pokemonList.length; i++) {
            if (i > 0) json.append(", ");
            appendPokemon(json, pokemonList[i]);
        }
    }

    private static void appendPokemon(StringBuilder json, Pokemon p) {
        if (p == null) {
            json.append("null");
            return;
        }

        json.append("{\"id\":").append(p.id)
            .append(",\"health\":").append(p.getHealth())
            .append(",\"individualValue\":").append(p.getIndividualValue())
            .append(",\"xp\":").append(p.getXp())
            .append(",\"skillIds\":[");
        for (int i = 0; i < 4; i++) {
            if (i > 0) json.append(',');
            Skill skill = p.getPokemonSkill(i);
            json.append(skill == null ? 0 : skill.id);
        }
        json.append("]}");
    }

    private static void appendCountMap(StringBuilder json, Map<Integer, Integer> counts) {
        json.append('{');
        boolean first = true;
        for (Map.Entry<Integer, Integer> count : counts.entrySet()) {
            if (!first) json.append(',');
            json.append('"').append(count.getKey()).append("\":").append(count.getValue());
            first = false;
        }
        json.append('}');
    }

    private static void appendIntSet(StringBuilder json, Set<Integer> values) {
        json.append('[');
        boolean first = true;
        for (Integer value : values) {
            if (!first) json.append(',');
            json.append(value);
            first = false;
        }
        json.append(']');
    }

    private static Pokemon[] readPokemonArray(Object value, int length) {
        List<Object> values = asList(value);
        Pokemon[] result = new Pokemon[length];
        for (int i = 0; i < values.size() && i < length; i++) {
            result[i] = readPokemon(values.get(i));
        }
        return result;
    }

    private static List<Pokemon> readPokemonList(Object value) {
        List<Object> values = asList(value);
        List<Pokemon> result = new ArrayList<>();
        for (Object pokemonValue : values) {
            Pokemon p = readPokemon(pokemonValue);
            if (p != null) result.add(p);
        }
        return result;
    }

    private static Pokemon readPokemon(Object value) {
        if (value == null) return null;
        Map<String, Object> values = asObject(value);
        int id = intValue(values.get("id"));
        if (id <= 0 || id >= Pokemon.NAME_TABLE.length) {
            throw new IllegalArgumentException("Invalid Pokemon id: " + id);
        }

        Skill[] skills = new Skill[4];
        List<Object> skillIds = asList(values.get("skillIds"));
        for (int i = 0; i < skills.length; i++) {
            skills[i] = Skill.get(i < skillIds.size() ? intValue(skillIds.get(i)) : 0);
        }
        Pokemon p = new Pokemon(
            id,
            0,
            intValue(values.get("individualValue")),
            intValue(values.get("xp")),
            skills
        );
        p.setHealth(intValue(values.get("health")));
        return p;
    }

    private static HashMap<Integer, Integer> readCountMap(Object value) {
        Map<String, Object> values = asObject(value);
        HashMap<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            result.put(Integer.parseInt(entry.getKey()), intValue(entry.getValue()));
        }
        return result;
    }

    private static Set<Integer> readIntSet(Object value) {
        HashSet<Integer> result = new HashSet<>();
        if (value == null) return result;
        List<Object> values = asList(value);
        for (Object item : values) {
            result.add(intValue(item));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asObject(Object value) {
        if (!(value instanceof Map)) throw new IllegalArgumentException("Expected JSON object");
        return (Map<String, Object>) value;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> asList(Object value) {
        if (!(value instanceof List)) throw new IllegalArgumentException("Expected JSON array");
        return (List<Object>) value;
    }

    private static int intValue(Object value) {
        if (!(value instanceof Number)) throw new IllegalArgumentException("Expected JSON number");
        return ((Number) value).intValue();
    }

    private static class JsonParser {
        private final String value;
        private int position = 0;

        JsonParser(String value) {
            this.value = value;
        }

        Object parse() {
            Object parsed = readValue();
            skipWhitespace();
            if (position != value.length()) throw error("Unexpected trailing content");
            return parsed;
        }

        private Object readValue() {
            skipWhitespace();
            if (position >= value.length()) throw error("Unexpected end of JSON");
            char next = value.charAt(position);
            if (next == '{') return readObject();
            if (next == '[') return readArray();
            if (next == '"') return readString();
            if (next == 'n') return readNull();
            if (next == '-' || Character.isDigit(next)) return readNumber();
            throw error("Unexpected character");
        }

        private Map<String, Object> readObject() {
            HashMap<String, Object> result = new HashMap<>();
            position++;
            skipWhitespace();
            if (consume('}')) return result;
            while (true) {
                skipWhitespace();
                if (position >= value.length() || value.charAt(position) != '"') {
                    throw error("Expected object key");
                }
                String key = readString();
                skipWhitespace();
                expect(':');
                result.put(key, readValue());
                skipWhitespace();
                if (consume('}')) return result;
                expect(',');
            }
        }

        private List<Object> readArray() {
            List<Object> result = new ArrayList<>();
            position++;
            skipWhitespace();
            if (consume(']')) return result;
            while (true) {
                result.add(readValue());
                skipWhitespace();
                if (consume(']')) return result;
                expect(',');
            }
        }

        private String readString() {
            StringBuilder result = new StringBuilder();
            expect('"');
            while (position < value.length()) {
                char current = value.charAt(position++);
                if (current == '"') return result.toString();
                if (current != '\\') {
                    result.append(current);
                    continue;
                }
                if (position >= value.length()) throw error("Invalid string escape");
                char escape = value.charAt(position++);
                switch (escape) {
                    case '"': result.append('"'); break;
                    case '\\': result.append('\\'); break;
                    case '/': result.append('/'); break;
                    case 'b': result.append('\b'); break;
                    case 'f': result.append('\f'); break;
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case 'u':
                        if (position + 4 > value.length()) throw error("Invalid unicode escape");
                        result.append((char) Integer.parseInt(value.substring(position, position + 4), 16));
                        position += 4;
                        break;
                    default: throw error("Invalid string escape");
                }
            }
            throw error("Unterminated string");
        }

        private Object readNumber() {
            int start = position;
            if (value.charAt(position) == '-') position++;
            while (position < value.length() && Character.isDigit(value.charAt(position))) position++;
            try {
                return Long.parseLong(value.substring(start, position));
            } catch (NumberFormatException e) {
                throw error("Invalid number");
            }
        }

        private Object readNull() {
            if (!value.startsWith("null", position)) throw error("Invalid null");
            position += 4;
            return null;
        }

        private boolean consume(char expected) {
            if (position < value.length() && value.charAt(position) == expected) {
                position++;
                return true;
            }
            return false;
        }

        private void expect(char expected) {
            if (!consume(expected)) throw error("Expected '" + expected + "'");
        }

        private void skipWhitespace() {
            while (position < value.length() && Character.isWhitespace(value.charAt(position))) position++;
        }

        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException(message + " at position " + position);
        }
    }

    public void startLoop() {
        Event e;
        while (true) {
            if (eventList.isEmpty()) {
                try {
                    Thread.sleep(10);
                    continue;
                } catch (InterruptedException e1) {

                }
            }
            
            e = eventList.removeFirst();
            raisedEventList = new ArrayList<>();
            System.out.println("Event Raised! Event type: " + e.type);
            for (Consumer<Event> el : eventListeners.values()) {
                el.accept(e);
            }

            // TODO GameManager 레벨에서 이벤트로 인해 처리해야 할 것 진행
            switch(e.type) {
                case Event.EVENT_TYPE.BATTLE_START:
                    enemyPokemon = e.pokemon;
                    raiseEvent(Event.newTextEvent("야생의 " + e.pokemon.name + "을/를 마주쳤다!"));
                    raiseEvent(Event.newTurnStartEvent());
                    break;
                case Event.EVENT_TYPE.ATTACK:
                    enemyPokemon.setHealth(enemyPokemon.getHealth() - e.damage);
                    addPP(5);
                    System.out.println(e.damage);
                    raiseEvent(Event.newTextEvent(e.damage + "의 대미지를 입혔다!"));
                    raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.SKILL:
                    if (e.skill == null || e.skill.id == 0 || getPP() < e.skill.pp) {
                        raiseEvent(Event.newTextEvent("스킬을 사용할 PP가 부족하다!"));
                        raiseEvent(Event.newTurnEndEvent());
                        break;
                    }
                    addPP(-e.skill.pp);
                    e.skill.use.accept(getCurrentPokemon(), enemyPokemon);
                    raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.CLEAR_EVENT_QUEUE:
                    eventList.clear();
                    break;
                case Event.EVENT_TYPE.ITEM:
                    itemCount.put(e.item.id, itemCount.get(e.item.id) - 1);
                    itemFinishedBattle = false;
                    e.item.use.accept(e.pokemon);
                    if (!itemFinishedBattle) raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.EXIT:
                    save();
                    System.exit(0);
                    break;
                case Event.EVENT_TYPE.CHANGE:
                    raiseEvent(Event.newTextEvent(pokemon[e.idx].name + "! 너로 정했다!"));
                    selectedPokemonIdx = e.idx;
                    raiseEvent(Event.newTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.TURN_END:
                    if (enemyPokemon.getHealth() == 0) {
                        raiseEvent(Event.newEnemyDeadEvent());
                    } else {
                        raiseEvent(Event.newEnemyTurnStartEvent());
                    }
                    break;
                case Event.EVENT_TYPE.ENEMY_DEAD:
                    giveDefeatDrop();
                    raiseEvent(Event.newBattleEndEvent(enemyPokemon.getXp() / 30 + enemyPokemon.getLevel()));
                    break;
                case Event.EVENT_TYPE.ENEMY_TURN_START:
                    // TODO what enemy do (= AI);
                    if (!enemyPokemonCanAct) {
                        raiseEvent(Event.newEnemyTurnEndEvent());
                        break;
                    }
                    int dmg = enemyPokemon.getAttackDamage();
                    dmg += new Random().nextInt() % (dmg / 5);
                    raiseEvent(Event.newEnemyAttackEvent(dmg));
                    break;
                case Event.EVENT_TYPE.ENEMY_ATTACK:
                    getCurrentPokemon().setHealth(getCurrentPokemon().getHealth() - e.damage);
                    System.out.println(e.damage);
                    raiseEvent(Event.newTextEvent(e.damage + "의 대미지를 입었다!"));
                    raiseEvent(Event.newEnemyTurnEndEvent());
                    break;
                case Event.EVENT_TYPE.ENEMY_TURN_END:
                    if (getCurrentPokemon().getHealth() == 0) {
                        raiseEvent(Event.newDeadEvent());
                    } else {
                        raiseEvent(Event.newTurnStartEvent());
                    }
                    break;
                case Event.EVENT_TYPE.DEAD:
                    boolean flag = true;
                    raiseEvent(Event.newTextEvent(getCurrentPokemon().name + "이/가 쓰러졌다"));
                    for (int i = 0; i < 6; i++) {
                        if (pokemon[i] == null) continue;
                        if (pokemon[i].getHealth() != 0) flag = false;
                    }
                    if (flag) {
                        raiseEvent(Event.newTextEvent("눈 앞이 캄캄해졌다"));
                        // TODO 포켓몬 센터로 이동
                        // TODO 테스트코드 제거
                        for (int i = 0; i < 6; i++) {
                            if (pokemon[i] == null) continue;
                            pokemon[i].setHealth(pokemon[i].getMaxHealth());
                        }
                        // 테스트 코드 끝
                    }
                    break;
                case Event.EVENT_TYPE.BATTLE_END:
                    if (e.xp != 0) {
                        raiseEvent(Event.newTextEvent(enemyPokemon.name + "를 쓰러뜨렸다!\n" + e.xp + "의 경험치를 얻었다."));
                        getCurrentPokemon().setXp(getCurrentPokemon().getXp() + e.xp);
                    }
                    raiseEvent(Event.newBattleStartEvent(Pokemon.generateRandom()));
                    break;
                    case Event.EVENT_TYPE.DELAY:
                    try {
                        Thread.sleep(e.milliseconds);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                case Event.EVENT_TYPE.TURN_START:
                case Event.EVENT_TYPE.NOTHING:
                case Event.EVENT_TYPE.START_POKEMON_EVENT:
                case Event.EVENT_TYPE.TEXT:
                    break;
                default:
                    System.out.println("Unhandled Event in GameManager: " + e.type);
            }

            processPendingStatusClear();

            for (int i = raisedEventList.size() - 1; i >= 0; i--) {
                eventList.addFirst(raisedEventList.get(i));
            }
            raisedEventList = null;
        }
    }

    private void processPendingStatusClear() {
        if (status != null && !status.isNone() && status.shouldClear()) {
            setStatus(Status.get(0));
        }
        if (enemyStatus != null && !enemyStatus.isNone() && enemyStatus.shouldClear()) {
            setEnemyStatus(Status.get(0));
        }
    }

    public static void raiseEvent(Event e) {
        GameManager gm = getInstance();
        if (gm.raisedEventList != null) {
            gm.raisedEventList.add(e);
        } else {
            gm.eventList.addLast(e);
        }
    }

    public static Integer registEventListener(Consumer<Event> el) {
        GameManager gm = getInstance();
        Integer idx = 0;
        while (gm.eventListeners.containsKey(idx)) idx++;
        gm.eventListeners.put(idx, el);
        return idx;
    }

    public static void unRegistEventListener(Integer idx) {
        GameManager gm = getInstance();
        if (gm.eventListeners.containsKey(idx)) gm.eventListeners.remove(idx);
    }
    
    public static Pokemon getCurrentPokemon() {
        GameManager gm = getInstance();
        return gm.pokemon[gm.selectedPokemonIdx];
    }

    public static Pokemon getPokemon(int idx) {
        return getInstance().pokemon[idx];
    }

    public static void setStartingPokemon(Pokemon starter) {
        GameManager gm = getInstance();
        gm.pokemon = new Pokemon[6];
        gm.pokemon[0] = starter;
        gm.addPokemonToDict(starter);
        gm.selectedPokemonIdx = 0;
        gm.itemCount.clear();
        gm.itemCount.put(1, 3);
        gm.itemCount.put(4, 3);
        gm.status.deActivate();
        gm.status = Status.get(0);
        gm.enemyStatus.deActivate();
        gm.enemyStatus = Status.get(0);
        gm.currentPokemonCanAct = true;
        gm.enemyPokemonCanAct = true;
        gm.pp = 0;
    }

    public static void setEnemyStatus(Status s) {
        GameManager gm = getInstance();
        gm.enemyStatus.deActivate();
        gm.enemyStatus = s;
        s.activate();
        gm.enemyPokemonCanAct = true;
    }

    public static void setStatus(Status s) {
        GameManager gm = getInstance();
        gm.status.deActivate();
        gm.status = s;
        s.activate();
        gm.currentPokemonCanAct = true;
    }

    public static boolean canCurrentPokemonAct() {
        return getInstance().currentPokemonCanAct;
    }

    public static void setCurrentPokemonCanAct(boolean value) {
        getInstance().currentPokemonCanAct = value;
    }

    public static boolean canEnemyPokemonAct() {
        return getInstance().enemyPokemonCanAct;
    }

    public static void setEnemyPokemonCanAct(boolean value) {
        getInstance().enemyPokemonCanAct = value;
    }

    public void tryCapture(int baseChance, boolean guaranteed) {
        int missingHealth = enemyPokemon.getMaxHealth() - enemyPokemon.getHealth();
        int chance = Math.min(95, baseChance + missingHealth * 50 / enemyPokemon.getMaxHealth());

        raiseEvent(Event.newTextEvent("."));
                
        raiseEvent(Event.newTextEvent(".."));
        raiseEvent(Event.newTextEvent("..."));
        
        if (!guaranteed && new Random().nextInt(100) >= chance) {
            raiseEvent(Event.newTextEvent("포켓몬이 볼에서 빠져나왔다!"));
            return;
        }

        boolean placedInParty = false;
        for (int i = 0; i < pokemon.length; i++) {
            if (pokemon[i] != null) continue;
            pokemon[i] = enemyPokemon;
            placedInParty = true;
            break;
        }
        if (!placedInParty) box.add(enemyPokemon);

        addPokemonToDict(enemyPokemon);

        raiseEvent(Event.newTextEvent(enemyPokemon.name + "을/를 잡았다!"));
        if (!placedInParty) {
            raiseEvent(Event.newTextEvent(enemyPokemon.name + "은/는 박스로 전송되었다."));
        }
        itemFinishedBattle = true;
        raiseEvent(Event.newBattleEndEvent(0));
    }

    private void giveDefeatDrop() {
        Random random = new Random();
        int dropRoll = random.nextInt(100);

        if (dropRoll < 30) {
            List<Item> items = new ArrayList<>();
            for (Item item : Item.ITEM_TABLE) {
                if (item.id != 0) items.add(item);
            }
            if (items.isEmpty()) return;
            Item item = items.get(random.nextInt(items.size()));
            itemCount.put(item.id, getItemCount(item.id) + 1);
            raiseEvent(Event.newTextEvent(item.name + "을/를 획득했다!"));
        }
    }

    public static int getItemCount(int id) {
        Integer rtn = getInstance().itemCount.get(id);
        return ((rtn == null) ? 0:rtn);
    }

    public static void setItmeCount(int id, int value) {
        getInstance().itemCount.put(id, value);
    }

    public static Pokemon getEnemyPokemon() {
        return getInstance().enemyPokemon;
    }
    
    public static int getPP() {
        return getInstance().pp;
    }

    public static void setPP(int value) {
        getInstance().pp = Math.max(0, Math.min(40, value));
    }

    public static void addPP(int value) {
        setPP(getPP() + value);
    }

    public static boolean isCapturedPokemon(int id) {
        return getInstance().pokemonDict.contains(id);
    }

    private void addPokemonToDict(Pokemon pokemon) {
        if (pokemon != null) pokemonDict.add(pokemon.id);
    }
}

