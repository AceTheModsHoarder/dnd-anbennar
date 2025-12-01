package dnd.characters;

public class Monster {
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int armor; // League-style armor (percentage reduction)
    private int damageResistance; // Flat damage reduction (can be reduced by spells/effects)
    private int dexterity;
    private int expReward;
    private int goldReward;
    private boolean isAlive;
    
    public Monster(String name, int hp, int attack, int armor, int damageResistance) {
        this.name = name;
        this.maxHp = hp;
        this.hp = maxHp;
        this.attack = attack;
        this.armor = armor;
        this.damageResistance = damageResistance;
        this.dexterity = 10;
        this.expReward = (hp + attack + armor) * 2;
        this.goldReward = (hp + attack) / 2;
        this.isAlive = true;
    }
    
    // For backward compatibility
    public Monster(String name, int hp, int attack, int defense) {
        this(name, hp, attack, defense, 0);
    }
    
    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getArmor() { return armor; }
    public int getDamageResistance() { return damageResistance; }
    public int getDexterity() { return dexterity; }
    public int getExpReward() { return expReward; }
    public int getGoldReward() { return goldReward; }
    public boolean isAlive() { return isAlive; }
    
    // Setters for resistance (for spells/effects to modify)
    public void setDamageResistance(int resistance) {
        this.damageResistance = Math.max(0, resistance); // Can't go below 0
    }
    
    public void reduceDamageResistance(int amount) {
        this.damageResistance = Math.max(0, this.damageResistance - amount);
    }
    
    public void increaseDamageResistance(int amount) {
        this.damageResistance += amount;
    }
    
    // Calculate post-mitigation damage (League of Legends formula + flat resistance)
    public int calculatePostMitigationDamage(int rawDamage) {
        // Apply armor reduction first (League formula)
        double armorReducedDamage = rawDamage * (100.0 / (100 + armor));
        
        // Apply flat damage resistance (subtract from damage)
        double finalDamage = Math.max(1, armorReducedDamage - damageResistance);
        
        return (int) Math.round(finalDamage);
    }
    
    public void takeDamage(int rawDamage) {
        int actualDamage = calculatePostMitigationDamage(rawDamage);
        hp = Math.max(0, hp - actualDamage);
        if (hp == 0) {
            isAlive = false;
        }
    }
    
    // Factory methods
    public static Monster createGoblin() {
        return new Monster("Goblin", 15, 4, 10, 2); // 10 armor, 2 flat resistance
    }
    
    public static Monster createOrc() {
        return new Monster("Orc", 25, 6, 20, 5); // 20 armor, 5 flat resistance
    }
    
    public static Monster createDragon() {
        return new Monster("Dragon", 50, 10, 40, 10); // 40 armor, 10 flat resistance
    }
}