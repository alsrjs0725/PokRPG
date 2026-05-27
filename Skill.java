import java.util.Random;
import java.util.function.BiConsumer;

public class Skill implements Cloneable {
    public static final int SKILL_COUNT = 40;

    private static final Random RANDOM = new Random();

    int id, pp;
    String name;
    BiConsumer<Pokemon, Pokemon> use;

    private static Skill SKILL_TABLE[] = {
        new Skill("NOTHING", 0, (Pokemon from, Pokemon to) -> {
            // Do nothing.
        }, 0),
        new Skill("몸통박치기", 1, attack("몸통박치기", Pokemon.TYPE.NORMAL, 40), 4),
        new Skill("할퀴기", 2, attack("할퀴기", Pokemon.TYPE.NORMAL, 35), 3),
        new Skill("전광석화", 3, attack("전광석화", Pokemon.TYPE.NORMAL, 45), 5),
        new Skill("파괴광선", 4, attack("파괴광선", Pokemon.TYPE.NORMAL, 120), 18),
        new Skill("불꽃세례", 5, statusAttack("불꽃세례", Pokemon.TYPE.FIRE, 40, 2, 10), 5),
        new Skill("화염방사", 6, statusAttack("화염방사", Pokemon.TYPE.FIRE, 75, 2, 20), 11),
        new Skill("불대문자", 7, statusAttack("불대문자", Pokemon.TYPE.FIRE, 100, 2, 35), 16),
        new Skill("물대포", 8, attack("물대포", Pokemon.TYPE.WATER, 40), 5),
        new Skill("거품광선", 9, attack("거품광선", Pokemon.TYPE.WATER, 60), 8),
        new Skill("하이드로펌프", 10, attack("하이드로펌프", Pokemon.TYPE.WATER, 105), 16),
        new Skill("덩굴채찍", 11, attack("덩굴채찍", Pokemon.TYPE.GRASS, 40), 5),
        new Skill("잎날가르기", 12, attack("잎날가르기", Pokemon.TYPE.GRASS, 65), 8),
        new Skill("솔라빔", 13, attack("솔라빔", Pokemon.TYPE.GRASS, 110), 17),
        new Skill("전기쇼크", 14, statusAttack("전기쇼크", Pokemon.TYPE.ELECTRIC, 40, 3, 10), 5),
        new Skill("십만볼트", 15, statusAttack("십만볼트", Pokemon.TYPE.ELECTRIC, 80, 3, 25), 12),
        new Skill("전기자석파", 16, statusOnly("전기자석파", 3), 8),
        new Skill("얼음뭉치", 17, attack("얼음뭉치", Pokemon.TYPE.ICE, 45), 5),
        new Skill("냉동빔", 18, statusAttack("냉동빔", Pokemon.TYPE.ICE, 80, 5, 20), 12),
        new Skill("눈보라", 19, statusAttack("눈보라", Pokemon.TYPE.ICE, 105, 5, 35), 17),
        new Skill("태권당수", 20, attack("태권당수", Pokemon.TYPE.FIGHTING, 50), 6),
        new Skill("로킥", 21, attack("로킥", Pokemon.TYPE.FIGHTING, 65), 8),
        new Skill("지옥의바퀴", 22, recoilAttack("지옥의바퀴", Pokemon.TYPE.FIGHTING, 95, 4), 13),
        new Skill("독침", 23, statusAttack("독침", Pokemon.TYPE.POISON, 35, 1, 20), 4),
        new Skill("오물공격", 24, statusAttack("오물공격", Pokemon.TYPE.POISON, 70, 1, 35), 10),
        new Skill("맹독", 25, statusOnly("맹독", 1), 9),
        new Skill("진흙뿌리기", 26, attack("진흙뿌리기", Pokemon.TYPE.GROUND, 35), 4),
        new Skill("지진", 27, attack("지진", Pokemon.TYPE.GROUND, 100), 15),
        new Skill("바람일으키기", 28, attack("바람일으키기", Pokemon.TYPE.FLYING, 40), 5),
        new Skill("날개치기", 29, attack("날개치기", Pokemon.TYPE.FLYING, 65), 8),
        new Skill("염동력", 30, attack("염동력", Pokemon.TYPE.PSYCHIC, 45), 6),
        new Skill("사이코키네시스", 31, attack("사이코키네시스", Pokemon.TYPE.PSYCHIC, 90), 13),
        new Skill("벌레먹기", 32, attack("벌레먹기", Pokemon.TYPE.BUG, 55), 7),
        new Skill("돌떨구기", 33, attack("돌떨구기", Pokemon.TYPE.ROCK, 60), 8),
        new Skill("섀도볼", 34, attack("섀도볼", Pokemon.TYPE.GHOST, 80), 12),
        new Skill("용의숨결", 35, statusAttack("용의숨결", Pokemon.TYPE.DRAGON, 70, 3, 20), 10),
        new Skill("물기", 36, attack("물기", Pokemon.TYPE.DARK, 60), 8),
        new Skill("아이언테일", 37, attack("아이언테일", Pokemon.TYPE.STEEL, 85), 12),
        new Skill("요정의바람", 38, attack("요정의바람", Pokemon.TYPE.FAIRY, 55), 7),
        new Skill("HP회복", 39, heal("HP회복", 0.5), 14),
        new Skill("잠자기", 40, rest(), 18),
    };

    Skill(String name, int id, BiConsumer<Pokemon, Pokemon> use, int pp) {
        this.name = name;
        this.id = id;
        this.use = use;
        this.pp = pp;
    }

    private static BiConsumer<Pokemon, Pokemon> attack(String name, Pokemon.TYPE type, int power) {
        return (Pokemon from, Pokemon to) -> dealDamage(name, from, to, type, power);
    }

    private static BiConsumer<Pokemon, Pokemon> statusAttack(
        String name, Pokemon.TYPE type, int power, int statusId, int chance
    ) {
        return (Pokemon from, Pokemon to) -> {
            dealDamage(name, from, to, type, power);
            if (to.getHealth() > 0 && RANDOM.nextInt(100) < chance) {
                applyStatus(to, statusId);
            }
        };
    }

    private static BiConsumer<Pokemon, Pokemon> recoilAttack(
        String name, Pokemon.TYPE type, int power, int recoilDivisor
    ) {
        return (Pokemon from, Pokemon to) -> {
            int damage = dealDamage(name, from, to, type, power);
            int recoil = Math.max(1, damage / recoilDivisor);
            from.setHealth(from.getHealth() - recoil);
            GameManager.raiseEvent(Event.newTextEvent(from.name + "은/는 반동으로 " + recoil + "의 피해를 입었다!"));
        };
    }

    private static BiConsumer<Pokemon, Pokemon> statusOnly(String name, int statusId) {
        return (Pokemon from, Pokemon to) -> {
            GameManager.raiseEvent(Event.newTextEvent(from.name + "의 " + name + "!"));
            applyStatus(to, statusId);
        };
    }

    private static BiConsumer<Pokemon, Pokemon> heal(String name, double ratio) {
        return (Pokemon from, Pokemon to) -> {
            int previousHealth = from.getHealth();
            from.setHealth(previousHealth + (int) Math.ceil(from.getMaxHealth() * ratio));
            int recoveredHealth = from.getHealth() - previousHealth;
            GameManager.raiseEvent(Event.newTextEvent(from.name + "의 " + name + "!\nHP를 " + recoveredHealth + " 회복했다!"));
        };
    }

    private static BiConsumer<Pokemon, Pokemon> rest() {
        return (Pokemon from, Pokemon to) -> {
            int recoveredHealth = from.getMaxHealth() - from.getHealth();
            from.setHealth(from.getMaxHealth());
            applyStatus(from, 4);
            GameManager.raiseEvent(Event.newTextEvent(from.name + "은/는 잠들어 HP를 " + recoveredHealth + " 회복했다!"));
        };
    }

    private static int dealDamage(String name, Pokemon from, Pokemon to, Pokemon.TYPE type, int power) {
        double effectiveness = 1.0;
        for (Pokemon.TYPE defenderType : to.getPokemonType()) {
            effectiveness *= Pokemon.TYPE_DAMAGE_TABLE[type.ordinal()][defenderType.ordinal()];
        }

        int damage = 0;
        if (effectiveness > 0) {
            damage = Math.max(1, (int) Math.round(from.getAttackDamage() * power / 40.0 * effectiveness));
        }
        to.setHealth(to.getHealth() - damage);

        String text = from.name + "의 " + name + "!\n" + damage + "의 대미지를 입혔다!";
        if (effectiveness >= 2.0) {
            text += "\n효과가 굉장했다!";
        } else if (effectiveness == 0) {
            text += "\n효과가 없었다!";
        } else if (effectiveness < 1.0) {
            text += "\n효과가 별로인 듯하다.";
        }
        GameManager.raiseEvent(Event.newTextEvent(text));
        return damage;
    }

    private static void applyStatus(Pokemon target, int statusId) {
        if (target == GameManager.getEnemyPokemon()) {
            GameManager.setEnemyStatus(Status.get(statusId));
        } else if (target == GameManager.getCurrentPokemon()) {
            GameManager.setStatus(Status.get(statusId));
        }
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return obj;
    }

    public static Skill get(int id) {
        for (int i = 0; i < SKILL_TABLE.length; i++) {
            if (SKILL_TABLE[i].id == id) return (Skill) SKILL_TABLE[i].clone();
        }
        return get(0);
    }
}
