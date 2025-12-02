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
    
    public void setArmor(int armor) {
    this.armor = Math.max(0, armor); // Armor can't be negative
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
    // Store original values for logging
    int originalHP = hp;
    
    // Calculate post-mitigation damage (League of Legends formula + flat resistance)
    double armorReducedDamage = rawDamage * (100.0 / (100 + armor));
    
    // Apply flat damage resistance (subtract from damage)
    double finalDamage = Math.max(1, armorReducedDamage - damageResistance);
    
    int actualDamage = (int) Math.round(finalDamage);
    
    // Debug logging
    if (true) { // You can make this conditional
        System.out.println("\n=== MONSTER DAMAGE CALCULATION ===");
        System.out.println("Raw Damage: " + rawDamage);
        System.out.println("Armor: " + armor);
        System.out.println("Damage after armor: " + rawDamage + " * (100 / (100 + " + armor + ")) = " + 
                         String.format("%.1f", armorReducedDamage));
        System.out.println("Damage Resistance: " + damageResistance);
        System.out.println("Damage after resistance: " + String.format("%.1f", armorReducedDamage) + " - " + 
                         damageResistance + " = " + String.format("%.1f", finalDamage));
        System.out.println("Final Damage (rounded): " + actualDamage);
        System.out.println("HP: " + originalHP + " - " + actualDamage + " = " + (originalHP - actualDamage));
    }
    
    hp = Math.max(0, originalHP - actualDamage);
    if (hp == 0) {
        isAlive = false;
    }
}
}