package dnd.items;

public class Weapon {
    private String name;
    private int attackBonus;
    private int value;
    private int percentArmorPenetration;
    private int flatArmorPenetration;
    
    public Weapon(String name, int attackBonus) {
        this(name, attackBonus, 0, 0, 0);
    }
    
    public Weapon(String name, int attackBonus, int value, int percentArmorPen, int flatArmorPen) {
        this.name = name;
        this.attackBonus = attackBonus;
        this.value = value;
        this.percentArmorPenetration = Math.min(100, percentArmorPen);
        this.flatArmorPenetration = Math.max(0, flatArmorPen);
    }
    
    // Getters
    public String getName() { return name; }
    public int getAttackBonus() { return attackBonus; }
    public int getValue() { return value; }
    public int getPercentArmorPenetration() { return percentArmorPenetration; }
    public int getFlatArmorPenetration() { return flatArmorPenetration; }
    
    // Weapon creations with only armor penetration
    public static Weapon createDagger() {
        return new Weapon("Dagger", 2, 15, 10, 2);
    }
    
    public static Weapon createSword() {
        return new Weapon("Sword", 4, 50, 0, 5);
    }
    
    public static Weapon createAxe() {
        return new Weapon("Axe", 5, 75, 25, 0);
    }
    
    public static Weapon createMagicSword() {
        return new Weapon("Magic Sword", 6, 150, 30, 8);
    }
}