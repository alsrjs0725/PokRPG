public class Pokemon {
    public enum TYPE {
        EMPTY,
        NORMAL,
        FIRE,
        WATER,
        ELECTRIC,
        GRASS,
        ICE,
        FIGHTING,
        POISON,
        GROUND,
        FLYING,
        PSYCHIC,
        BUG,
        ROCK,
        GHOST,
        DRAGON,
        DARK,
        STEEL,
        FAIRY
    };

    public static final String NAME_TABLE[] = {
        "",
        "이상해씨", "이상해풀", "이상해꽃",
        "파이리", "리자드", "리자몽",
        "꼬부기", "어니부기", "거북왕",
        "캐터피", "단데기", "버터플",
        "뿔충이", "딱충이", "독침붕",
        "구구", "피죤", "피죤투",
        "꼬렛", "레트라",
        "깨비참", "깨비드릴조",
        "아보", "아보크",
        "피카츄", "라이츄",
        "모래두지", "고지",
        "니드런", "니드리나", "니드퀸",
        "니드런", "니드리노", "니드킹",
        "삐삐", "픽시",
        "식스테일", "나인테일",
        "푸린", "푸크린",
        "주뱃", "골뱃",
        "뚜벅쵸", "냄새꼬", "라플레시아",
        "파라스", "파라섹트",
        "콘팡", "도나리",
        "디그다", "닥트리오",
        "나옹", "페르시온",
        "고라파덕", "골덕",
        "망키", "성원숭",
        "가디", "윈디",
        "발챙이", "슈륙챙이", "강챙이",
        "캐이시", "윤겔라", "후딘",
        "알통몬", "근육몬", "괴력몬",
        "모다피", "우츠동", "우츠보트",
        "왕눈해", "독파리",
        "꼬마돌", "데구리", "딱구리",
        "포니타", "날쌩마",
        "야돈", "야도란",
        "코일", "레어코일",
        "파오리",
        "두두", "두트리오",
        "쥬쥬", "쥬레곤",
        "질퍽이", "질뻐기",
        "셀러", "파르셀",
        "고오스", "고우스트", "팬텀",
        "롱스톤",
        "슬리프", "슬리퍼",
        "크랩", "킹크랩",
        "찌리리공", "붐볼",
        "아라리", "나시",
        "탕구리", "텅구리",
        "시라소몬", "홍수몬",
        "내루미",
        "또가스", "또도가스",
        "뿔카노", "코뿌리",
        "럭키",
        "덩쿠리",
        "캥카",
        "쏘드라", "시드라",
        "콘치", "왕콘치",
        "별가사리", "아쿠스타",
        "마임맨",
        "스라크",
        "루주라",
        "에레브", "마그마",
        "쁘사이저",
        "켄타로스",
        "잉어킹", "갸라도스",
        "라프라스",
        "메타몽",
        "이브이", "샤미드", "쥬피썬더", "부스터",
        "폴리곤",
        "암나이트", "암스타",
        "투구", "투구푸스",
        "프테라",
        "잠만보",
        "프리져", "썬더", "파이어",
        "미뇽", "신뇽", "망나뇽",
        "뮤츠",
        "뮤"
    };

    public static TYPE[][] POKEMON_TYPE_TABLE = {
        {TYPE.EMPTY, TYPE.EMPTY},
        {TYPE.GRASS, TYPE.POISON},      // 001 이상해씨
        {TYPE.GRASS, TYPE.POISON},      // 002 이상해풀
        {TYPE.GRASS, TYPE.POISON},      // 003 이상해꽃
        {TYPE.FIRE, TYPE.EMPTY},        // 004 파이리
        {TYPE.FIRE, TYPE.EMPTY},        // 005 리자드
        {TYPE.FIRE, TYPE.FLYING},       // 006 리자몽
        {TYPE.WATER, TYPE.EMPTY},       // 007 꼬부기
        {TYPE.WATER, TYPE.EMPTY},       // 008 어니부기
        {TYPE.WATER, TYPE.EMPTY},       // 009 거북왕
        {TYPE.BUG, TYPE.EMPTY},         // 010 캐터피
        {TYPE.BUG, TYPE.EMPTY},         // 011 단데기
        {TYPE.BUG, TYPE.FLYING},        // 012 버터플
        {TYPE.BUG, TYPE.POISON},        // 013 뿔충이
        {TYPE.BUG, TYPE.POISON},        // 014 딱충이
        {TYPE.BUG, TYPE.POISON},        // 015 독침붕
        {TYPE.NORMAL, TYPE.FLYING},     // 016 구구
        {TYPE.NORMAL, TYPE.FLYING},     // 017 피죤
        {TYPE.NORMAL, TYPE.FLYING},     // 018 피죤투
        {TYPE.NORMAL, TYPE.EMPTY},      // 019 꼬렛
        {TYPE.NORMAL, TYPE.EMPTY},      // 020 레트라
        {TYPE.NORMAL, TYPE.FLYING},     // 021 깨비참
        {TYPE.NORMAL, TYPE.FLYING},     // 022 깨비드릴조
        {TYPE.POISON, TYPE.EMPTY},      // 023 아보
        {TYPE.POISON, TYPE.EMPTY},      // 024 아보크
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 025 피카츄
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 026 라이츄
        {TYPE.GROUND, TYPE.EMPTY},      // 027 모래두지
        {TYPE.GROUND, TYPE.EMPTY},      // 028 고지
        {TYPE.POISON, TYPE.EMPTY},      // 029 니드런
        {TYPE.POISON, TYPE.EMPTY},      // 030 니드리나
        {TYPE.POISON, TYPE.GROUND},     // 031 니드퀸
        {TYPE.POISON, TYPE.EMPTY},      // 032 니드런
        {TYPE.POISON, TYPE.EMPTY},      // 033 니드리노
        {TYPE.POISON, TYPE.GROUND},     // 034 니드킹
        {TYPE.NORMAL, TYPE.EMPTY},      // 035 삐삐
        {TYPE.NORMAL, TYPE.EMPTY},      // 036 픽시
        {TYPE.FIRE, TYPE.EMPTY},        // 037 식스테일
        {TYPE.FIRE, TYPE.EMPTY},        // 038 나인테일
        {TYPE.NORMAL, TYPE.EMPTY},      // 039 푸린
        {TYPE.NORMAL, TYPE.EMPTY},      // 040 푸크린
        {TYPE.POISON, TYPE.FLYING},     // 041 주뱃
        {TYPE.POISON, TYPE.FLYING},     // 042 골뱃
        {TYPE.GRASS, TYPE.POISON},      // 043 뚜벅쵸
        {TYPE.GRASS, TYPE.POISON},      // 044 냄새꼬
        {TYPE.GRASS, TYPE.POISON},      // 045 라플레시아
        {TYPE.BUG, TYPE.GRASS},         // 046 파라스
        {TYPE.BUG, TYPE.GRASS},         // 047 파라섹트
        {TYPE.BUG, TYPE.POISON},        // 048 콘팡
        {TYPE.BUG, TYPE.POISON},        // 049 도나리
        {TYPE.GROUND, TYPE.EMPTY},      // 050 디그다
        {TYPE.GROUND, TYPE.EMPTY},      // 051 닥트리오
        {TYPE.NORMAL, TYPE.EMPTY},      // 052 나옹
        {TYPE.NORMAL, TYPE.EMPTY},      // 053 페르시온
        {TYPE.WATER, TYPE.EMPTY},       // 054 고라파덕
        {TYPE.WATER, TYPE.EMPTY},       // 055 골덕
        {TYPE.FIGHTING, TYPE.EMPTY},    // 056 망키
        {TYPE.FIGHTING, TYPE.EMPTY},    // 057 성원숭
        {TYPE.FIRE, TYPE.EMPTY},        // 058 가디
        {TYPE.FIRE, TYPE.EMPTY},        // 059 윈디
        {TYPE.WATER, TYPE.EMPTY},       // 060 발챙이
        {TYPE.WATER, TYPE.EMPTY},       // 061 슈륙챙이
        {TYPE.WATER, TYPE.FIGHTING},    // 062 강챙이
        {TYPE.PSYCHIC, TYPE.EMPTY},     // 063 캐이시
        {TYPE.PSYCHIC, TYPE.EMPTY},     // 064 윤겔라
        {TYPE.PSYCHIC, TYPE.EMPTY},     // 065 후딘
        {TYPE.FIGHTING, TYPE.EMPTY},    // 066 알통몬
        {TYPE.FIGHTING, TYPE.EMPTY},    // 067 근육몬
        {TYPE.FIGHTING, TYPE.EMPTY},    // 068 괴력몬
        {TYPE.GRASS, TYPE.POISON},      // 069 모다피
        {TYPE.GRASS, TYPE.POISON},      // 070 우츠동
        {TYPE.GRASS, TYPE.POISON},      // 071 우츠보트
        {TYPE.WATER, TYPE.POISON},      // 072 왕눈해
        {TYPE.WATER, TYPE.POISON},      // 073 독파리
        {TYPE.ROCK, TYPE.GROUND},       // 074 꼬마돌
        {TYPE.ROCK, TYPE.GROUND},       // 075 데구리
        {TYPE.ROCK, TYPE.GROUND},       // 076 딱구리
        {TYPE.FIRE, TYPE.EMPTY},        // 077 포니타
        {TYPE.FIRE, TYPE.EMPTY},        // 078 날쌩마
        {TYPE.WATER, TYPE.PSYCHIC},     // 079 야돈
        {TYPE.WATER, TYPE.PSYCHIC},     // 080 야도란
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 081 코일
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 082 레어코일
        {TYPE.NORMAL, TYPE.FLYING},     // 083 파오리
        {TYPE.NORMAL, TYPE.FLYING},     // 084 두두
        {TYPE.NORMAL, TYPE.FLYING},     // 085 두트리오
        {TYPE.WATER, TYPE.EMPTY},       // 086 쥬쥬
        {TYPE.WATER, TYPE.ICE},         // 087 쥬레곤
        {TYPE.POISON, TYPE.EMPTY},      // 088 질퍽이
        {TYPE.POISON, TYPE.EMPTY},      // 089 질뻐기
        {TYPE.WATER, TYPE.EMPTY},       // 090 셀러
        {TYPE.WATER, TYPE.ICE},         // 091 파르셀
        {TYPE.GHOST, TYPE.POISON},      // 092 고오스
        {TYPE.GHOST, TYPE.POISON},      // 093 고우스트
        {TYPE.GHOST, TYPE.POISON},      // 094 팬텀
        {TYPE.ROCK, TYPE.GROUND},       // 095 롱스톤
        {TYPE.PSYCHIC, TYPE.EMPTY},     // 096 슬리프
        {TYPE.PSYCHIC, TYPE.EMPTY},     // 097 슬리퍼
        {TYPE.WATER, TYPE.EMPTY},       // 098 크랩
        {TYPE.WATER, TYPE.EMPTY},       // 099 킹크랩
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 100 찌리리공
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 101 붐볼
        {TYPE.GRASS, TYPE.PSYCHIC},     // 102 아라리
        {TYPE.GRASS, TYPE.PSYCHIC},     // 103 나시
        {TYPE.GROUND, TYPE.EMPTY},      // 104 탕구리
        {TYPE.GROUND, TYPE.EMPTY},      // 105 텅구리
        {TYPE.FIGHTING, TYPE.EMPTY},    // 106 시라소몬
        {TYPE.FIGHTING, TYPE.EMPTY},    // 107 홍수몬
        {TYPE.NORMAL, TYPE.EMPTY},      // 108 내루미
        {TYPE.POISON, TYPE.EMPTY},      // 109 또가스
        {TYPE.POISON, TYPE.EMPTY},      // 110 또도가스
        {TYPE.GROUND, TYPE.ROCK},       // 111 뿔카노
        {TYPE.GROUND, TYPE.ROCK},       // 112 코뿌리
        {TYPE.NORMAL, TYPE.EMPTY},      // 113 럭키
        {TYPE.GRASS, TYPE.EMPTY},       // 114 덩쿠리
        {TYPE.NORMAL, TYPE.EMPTY},      // 115 캥카
        {TYPE.WATER, TYPE.EMPTY},       // 116 쏘드라
        {TYPE.WATER, TYPE.EMPTY},       // 117 시드라
        {TYPE.WATER, TYPE.EMPTY},       // 118 콘치
        {TYPE.WATER, TYPE.EMPTY},       // 119 왕콘치
        {TYPE.WATER, TYPE.EMPTY},       // 120 별가사리
        {TYPE.WATER, TYPE.PSYCHIC},     // 121 아쿠스타
        {TYPE.PSYCHIC, TYPE.EMPTY},     // 122 마임맨
        {TYPE.BUG, TYPE.FLYING},        // 123 스라크
        {TYPE.ICE, TYPE.PSYCHIC},       // 124 루주라
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 125 에레브
        {TYPE.FIRE, TYPE.EMPTY},        // 126 마그마
        {TYPE.BUG, TYPE.EMPTY},         // 127 쁘사이저
        {TYPE.NORMAL, TYPE.EMPTY},      // 128 켄타로스
        {TYPE.WATER, TYPE.EMPTY},       // 129 잉어킹
        {TYPE.WATER, TYPE.FLYING},      // 130 갸라도스
        {TYPE.WATER, TYPE.ICE},         // 131 라프라스
        {TYPE.NORMAL, TYPE.EMPTY},      // 132 메타몽
        {TYPE.NORMAL, TYPE.EMPTY},      // 133 이브이
        {TYPE.WATER, TYPE.EMPTY},       // 134 샤미드
        {TYPE.ELECTRIC, TYPE.EMPTY},    // 135 쥬피썬더
        {TYPE.FIRE, TYPE.EMPTY},        // 136 부스터
        {TYPE.NORMAL, TYPE.EMPTY},      // 137 폴리곤
        {TYPE.ROCK, TYPE.WATER},        // 138 암나이트
        {TYPE.ROCK, TYPE.WATER},        // 139 암스타
        {TYPE.ROCK, TYPE.WATER},        // 140 투구
        {TYPE.ROCK, TYPE.WATER},        // 141 투구푸스
        {TYPE.ROCK, TYPE.FLYING},       // 142 프테라
        {TYPE.NORMAL, TYPE.EMPTY},      // 143 잠만보
        {TYPE.ICE, TYPE.FLYING},        // 144 프리져
        {TYPE.ELECTRIC, TYPE.FLYING},   // 145 썬더
        {TYPE.FIRE, TYPE.FLYING},       // 146 파이어
        {TYPE.DRAGON, TYPE.EMPTY},      // 147 미뇽
        {TYPE.DRAGON, TYPE.EMPTY},      // 148 신뇽
        {TYPE.DRAGON, TYPE.FLYING},     // 149 망나뇽
        {TYPE.PSYCHIC, TYPE.EMPTY},     // 150 뮤츠
        {TYPE.PSYCHIC, TYPE.EMPTY}      // 151 뮤
    };

    public static double[][] TYPE_DAMAGE_TABLE = {
        // DEFENDER →
        // EMP NOR FIR WAT ELE GRA ICE FIG POI GRO FLY PSY BUG ROC GHO DRA DAR STE FAI
        /* EMPTY */   {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        /* NORMAL */  {1,1,1,1,1,1,1,1,1,1,1,1,1,0.5,0,1,1,0.5,1},
        /* FIRE */    {1,1,0.5,0.5,1,2,2,1,1,1,1,1,2,0.5,1,0.5,1,2,1},
        /* WATER */   {1,1,2,0.5,1,0.5,1,1,1,2,1,1,1,2,1,0.5,1,1,1},
        /* ELECTRIC*/ {1,1,1,2,0.5,0.5,1,1,1,0,2,1,1,1,1,0.5,1,1,1},
        /* GRASS */   {1,1,0.5,2,1,0.5,1,1,0.5,2,0.5,1,0.5,2,1,0.5,1,0.5,1},
        /* ICE */     {1,1,0.5,0.5,1,2,0.5,1,1,2,2,1,1,1,1,2,1,0.5,1},
        /* FIGHT */   {1,2,1,1,1,1,2,1,0.5,1,0.5,0.5,0.5,2,0,1,2,2,0.5},
        /* POISON */  {1,1,1,1,1,2,1,1,0.5,0.5,1,1,1,0.5,0.5,1,1,0,2},
        /* GROUND */  {1,1,2,1,2,0.5,1,1,2,1,0,1,0.5,2,1,1,1,2,1},
        /* FLYING */  {1,1,1,1,0.5,2,1,2,1,1,1,1,2,0.5,1,1,1,0.5,1},
        /* PSYCHIC */ {1,1,1,1,1,1,1,2,2,1,1,0.5,1,1,1,1,0,0.5,1},
        /* BUG */     {1,1,0.5,1,1,2,1,0.5,0.5,1,0.5,2,1,1,0.5,1,2,0.5,0.5},
        /* ROCK */    {1,1,2,1,1,1,2,0.5,1,0.5,2,1,2,1,1,1,1,0.5,1},
        /* GHOST */   {1,0,1,1,1,1,1,1,1,1,1,2,1,1,2,1,0.5,1,1},
        /* DRAGON */  {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,0.5,0},
        /* DARK */    {1,1,1,1,1,1,1,0.5,1,1,1,2,1,1,2,1,0.5,1,0.5},
        /* STEEL */   {1,1,0.5,0.5,0.5,1,2,1,1,1,1,1,1,2,1,1,1,0.5,2},
        /* FAIRY */   {1,1,0.5,1,1,1,1,2,0.5,1,1,1,1,1,1,2,2,0.5,1}
    };

    
    // From Save Variables
    public int id;
    private int level, health, individualValue, xp;
    private Skill skill[];  // MAX 4
    private Equipment Equiped;

    // Non From Save Variables
    private int maxHealth;
    private TYPE type[];   // MAX 2
    public String name;



    Pokemon(int id, int level, int health, int individualValue, int xp, Skill skill[], int equiped_id) {
        this.id = id;
        this.level = level;
        this.health = health;
        this.individualValue = individualValue;
        this.xp = xp;
        this.skill = skill;

        type = POKEMON_TYPE_TABLE[id];
        name = NAME_TABLE[id];

        // TODO Edit temp code
        this.maxHealth = level * 15;
        // temp code end
    }

    Skill[] getPokemonSkill() { return skill.clone(); }
    TYPE[] getPokemonType() { return type.clone(); }
    int getMaxHealth() { return maxHealth; }
    void setMaxHealth(int h) { maxHealth = h; }
    int getHealth() { return health; }
    void setHealth(int health) { this.health = Math.min(maxHealth, Math.max(0, health)); }

}