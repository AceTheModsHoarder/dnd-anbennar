package dnd.items;

import dnd.characters.Player;

public class Potion {
    private String name;
    private String description;
    private int healAmount;
    
    public Potion(String name, String description, int healAmount) {
        this.name = name;
        this.description = description;
        this.healAmount = healAmount;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getHealAmount() { return healAmount; }
    
    public void use(Player player) {
        player.heal(healAmount);
        System.out.println("You drink the " + name + " and heal " + healAmount + " HP!");
    }
    
    // Common potions
    public static Potion createHealthPotion() {
        return new Potion("Health Potion", "Restores 20 HP", 20);
    }
    
    public static Potion createGreaterHealthPotion() {
        return new Potion("Greater Health Potion", "Restores 50 HP", 50);
    }
}