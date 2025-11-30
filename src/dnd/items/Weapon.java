package dnd.items;

public class Weapon {
    private String name;
    private int attackBonus;
    private int value;
    
    public Weapon(String name, int attackBonus) {
        this(name, attackBonus, 0); // Default value for price
    }
    
    public Weapon(String name, int attackBonus, int value) {
        this.name = name;
        this.attackBonus = attackBonus;
        this.value = value;
    }
    
    // Getters
    public String getName() { return name; }
    public int getAttackBonus() { return attackBonus; }
    public int getValue() { return value; }
    
    // Common weapons
    public static Weapon createDagger() {
        return new Weapon("Dagger", 2, 15);
    }
    
    public static Weapon createSword() {
        return new Weapon("Sword", 4, 50);
    }
    
    public static Weapon createAxe() {
        return new Weapon("Axe", 5, 75);
    }
}