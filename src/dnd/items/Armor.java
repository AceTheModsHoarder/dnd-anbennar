package dnd.items;

public class Armor {
    private String name;
    private int defenseBonus;
    private int value;
    
    // Add this constructor for 2 parameters
    public Armor(String name, int defenseBonus) {
        this(name, defenseBonus, 0); // Default value for price
    }
    
    public Armor(String name, int defenseBonus, int value) {
        this.name = name;
        this.defenseBonus = defenseBonus;
        this.value = value;
    }
    
    // Getters
    public String getName() { return name; }
    public int getDefenseBonus() { return defenseBonus; }
    public int getValue() { return value; }
    
    // Common armor
    public static Armor createLeatherArmor() {
        return new Armor("Leather Armor", 2, 30);
    }
    
    public static Armor createChainmail() {
        return new Armor("Chainmail", 4, 100);
    }
    
    public static Armor createPlateArmor() {
        return new Armor("Plate Armor", 6, 250);
    }
}