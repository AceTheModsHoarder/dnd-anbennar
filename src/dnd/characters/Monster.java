package dnd.characters;

public class Monster {
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int dexterity;
    private int armorClass;
    private int expReward;
    private int goldReward;
    private boolean isAlive;
    
    // Updated constructor to match your GameLoop usage
    public Monster(String name, int hp, int attack, int defense) {
        this(name, hp, attack, defense, 10, 25, 10); // Default values for other params
    }
    
    // Full constructor
    public Monster(String name, int hp, int attack, int defense, int dexterity, int expReward, int goldReward) {
        this.name = name;
        this.maxHp = hp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.dexterity = dexterity;
        this.armorClass = 10 + dexterity;
        this.expReward = expReward;
        this.goldReward = goldReward;
        this.isAlive = true;
    }
    
    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getDexterity() { return dexterity; }
    public int getArmorClass() { return armorClass; }
    public int getExpReward() { return expReward; }
    public int getGoldReward() { return goldReward; }
    public boolean isAlive() { return isAlive; }
    
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        if (hp == 0) {
            isAlive = false;
        }
    }
    
    // Factory methods for common monsters (matching your GameLoop)
    public static Monster createGoblin() {
        return new Monster("Goblin", 15, 4, 2);
    }
    
    public static Monster createOrc() {
        return new Monster("Orc", 25, 6, 4);
    }
    
    public static Monster createDragon() {
        return new Monster("Dragon", 50, 10, 6);
    }
}