import java.util.function.BiConsumer;

public class Skill implements Cloneable{
    int id, pp;
    String name;
    BiConsumer<Pokemon, Pokemon> use;

    private static Skill SKILL_TABLE[] = {
        new Skill("NOTHING", 0, (Pokemon from, Pokemon to) -> {
            // Do Nothing;
        }, 0),

    };

    Skill(String name, int id, BiConsumer<Pokemon, Pokemon> use, int pp) {
        this.name = name;
        this.id = id;
        this.use = use;
        this.pp = pp;
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
        } return null;
    }
}
