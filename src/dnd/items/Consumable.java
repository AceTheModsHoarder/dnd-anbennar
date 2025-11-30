package dnd.items;

import dnd.characters.Player;

public class Consumable {
    private String name;
    private String description;
    private int healAmount;
    private int quantity;
    
    public Consumable(String name, String description, int healAmount) {
        this.name = name;
        this.description = description;
        this.healAmount = healAmount;
        this.quantity = 1;
    }
    
    public Consumable(String name, String description, int healAmount, int quantity) {
        this.name = name;
        this.description = description;
        this.healAmount = healAmount;
        this.quantity = quantity;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getHealAmount() { return healAmount; }
    public int getQuantity() { return quantity; }
    
    public void use(Player player) {
        if (quantity > 0) {
            player.heal(healAmount);
            quantity--;
            System.out.println("You used " + name + " and healed " + healAmount + " HP!");
        }
    }
    
    public void addQuantity(int amount) {
        quantity += amount;
    }
    
    public boolean isEmpty() {
        return quantity <= 0;
    }
    
    // Common consumables
    public static Consumable createHealthPotion() {
        return new Consumable("Health Potion", "Restores 20 HP", 20);
    }
    
    public static Consumable createGreaterHealthPotion() {
        return new Consumable("Greater Health Potion", "Restores 50 HP", 50);
    }
    
    public Consumable createManaPotion() {
        return new Consumable("Mana Potion", "Restores 15 MP", 0, 1) {
            @Override
            public void use(Player player) {
                player.restoreMana(15);
                quantity--;
                System.out.println("You used " + name + " and restored 15 MP!");
            }
        };
    }
}