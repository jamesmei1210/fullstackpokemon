
//class used for storing each pokemon and all of its stats
public class Pokemon {
    //pokemon variables
    int num;
    String name;
    String ability;

    int hp;
    int atk;
    int def;
    int spa;
    int spd;
    int spe;

    String type1;
    String type2;

    //default constructor
    public Pokemon() {
        num = 0;
        name = "MissingNo";
        ability = "Overgrowth";

        hp = 0;
        atk = 0;
        def = 0;
        spa = 0;
        spd = 0;
        spe = 0;

        type1 = "normal";
        type2 = "normal";
    }

    //Constructor
    public Pokemon(int num, String name, String ability,
                   int hp, int atk, int def, int spa, int spd, int spe, String type1, String type2){
        this.num = num;
        this.name = name;
        this.ability = ability;

        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spa = spa;
        this.spd = spd;
        this.spe = spe;

        this.type1 = type1;
        this.type2 = type2;
    }
}
