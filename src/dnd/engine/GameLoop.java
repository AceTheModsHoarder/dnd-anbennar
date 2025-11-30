package dnd.engine;

import dnd.characters.Monster;
import dnd.characters.Player;
import dnd.combat.CombatSystem;
import dnd.items.Armor;
import dnd.items.Potion;
import dnd.items.Weapon;
import dnd.skills.BasicAttack;
import dnd.skills.Fireball;
import dnd.skills.Heal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameLoop {
    private final Scanner sc = new Scanner(System.in);
    private final Random random = new Random();
    private Player player;
    private boolean gameRunning;
    
    // Available monsters for random encounters
    private final List<Monster> monsters = new ArrayList<>();
    
    // Available items for shops/chests
    private final List<Weapon> availableWeapons = new ArrayList<>();
    private final List<Armor> availableArmor = new ArrayList<>();
    private final List<Potion> availablePotions = new ArrayList<>();
    
    public GameLoop() {
        initializeGame();
    }
    
    private void initializeGame() {
        System.out.println("=== WELCOME TO D&D ANBENNAR ===");
        System.out.print("Enter your character name: ");
        String name = sc.nextLine();
        
        // Create player
        player = new Player(name);
        
        // Add starting skills
        player.addSkill(new BasicAttack());
        player.addSkill(new Fireball());
        player.addSkill(new Heal());
        
        // Add starting items
        player.addItem(new Weapon("Rusty Dagger", 1));
        player.addItem(Potion.createHealthPotion());
        
        // Initialize monsters
        initializeMonsters();
        
        // Initialize available items
        initializeItems();
        
        gameRunning = true;
        
        System.out.println("\nWelcome, " + player.getName() + "!");
        System.out.println("You start your adventure with a Rusty Dagger and a Health Potion.");
        System.out.println("You have learned: Power Attack, Fireball, and Heal skills.");
    }
    
    private void initializeMonsters() {
        monsters.add(new Monster("Goblin", 15, 4, 2));
        monsters.add(new Monster("Orc", 25, 6, 4));
        monsters.add(new Monster("Dragon", 50, 10, 6));
        monsters.add(new Monster("Skeleton", 20, 5, 3));
        monsters.add(new Monster("Bandit", 18, 5, 2));
        monsters.add(new Monster("Wolf", 12, 6, 1));
    }
    
    private void initializeItems() {
        // Weapons
        availableWeapons.add(new Weapon("Iron Sword", 3));
        availableWeapons.add(new Weapon("Steel Axe", 4));
        availableWeapons.add(new Weapon("Magic Staff", 2));
        availableWeapons.add(new Weapon("Greatsword", 5));
        
        // Armor - Use the 2-parameter constructor
        availableArmor.add(new Armor("Leather Armor", 2));
        availableArmor.add(new Armor("Chainmail", 4));
        availableArmor.add(new Armor("Plate Armor", 6));
        
        // Potions
        availablePotions.add(Potion.createHealthPotion());
        availablePotions.add(Potion.createGreaterHealthPotion());
    }
    
    public void startGame() {
        while (gameRunning && player.isAlive()) {
            displayMainMenu();
            int choice = getMenuChoice(1, 6);
            
            switch (choice) {
                case 1 -> explore();
                case 2 -> rest();
                case 3 -> player.showStats();
                case 4 -> player.useItemMenu(sc);
                case 5 -> visitShop();
                case 6 -> exitGame();
            }
        }
        
        if (!player.isAlive()) {
            System.out.println("\n=== GAME OVER ===");
            System.out.println("Your adventure has come to an end...");
            System.out.println("You reached level " + player.getLevel() + "!");
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Explore");
        System.out.println("2. Rest (Heal HP/MP)");
        System.out.println("3. Check Status");
        System.out.println("4. Use Items");
        System.out.println("5. Visit Shop");
        System.out.println("6. Exit Game");
        System.out.print("Choose your action: ");
    }
    
    private void explore() {
        System.out.println("\nYou venture into the wilderness...");
        
        // Random encounter chance: 70%
        if (random.nextInt(100) < 70) {
            encounterMonster();
        } else {
            // Other exploration events
            int event = random.nextInt(100);
            if (event < 30) {
                findTreasure();
            } else if (event < 50) {
                findSafeHaven();
            } else {
                System.out.println("The path is quiet. You find nothing of interest.");
            }
        }
    }
    
    private void encounterMonster() {
    Monster monster = getRandomMonster();
    System.out.println("A wild " + monster.getName() + " appears!");
    
    // Use fully qualified name
    dnd.combat.CombatResult result = CombatSystem.start(player, monster);
    
    if (result.isVictory()) {
        System.out.println("You emerge victorious from the battle!");
            
            // Chance for loot drop
            if (random.nextInt(100) < 60) {
                dropLoot();
            }
        } else {
            System.out.println("You have been defeated...");
        }
    }
    
    private Monster getRandomMonster() {
        // Higher level players encounter stronger monsters
        int monsterIndex;
        if (player.getLevel() >= 5) {
            monsterIndex = random.nextInt(monsters.size());
        } else if (player.getLevel() >= 3) {
            monsterIndex = random.nextInt(Math.min(4, monsters.size()));
        } else {
            monsterIndex = random.nextInt(Math.min(2, monsters.size()));
        }
        
        return monsters.get(monsterIndex);
    }
    
    private void dropLoot() {
        System.out.println("You find loot on the defeated enemy!");
        
        int lootType = random.nextInt(100);
        if (lootType < 40) {
            // Gold
            int gold = random.nextInt(20) + 10;
            player.gainGold(gold);
            System.out.println("Found " + gold + " gold!");
        } else if (lootType < 70) {
            // Potion
            Potion potion = availablePotions.get(random.nextInt(availablePotions.size()));
            player.addItem(potion);
            System.out.println("Found a " + potion.getName() + "!");
        } else if (lootType < 85) {
            // Weapon
            if (!availableWeapons.isEmpty()) {
                Weapon weapon = availableWeapons.get(random.nextInt(availableWeapons.size()));
                player.addItem(weapon);
                System.out.println("Found a " + weapon.getName() + "!");
            }
        } else {
            // Armor
            if (!availableArmor.isEmpty()) {
                Armor armor = availableArmor.get(random.nextInt(availableArmor.size()));
                player.addItem(armor);
                System.out.println("Found " + armor.getName() + "!");
            }
        }
    }
    
    private void findTreasure() {
        System.out.println("You discover a hidden treasure chest!");
        
        int treasureType = random.nextInt(100);
        if (treasureType < 50) {
            int gold = random.nextInt(50) + 25;
            player.gainGold(gold);
            System.out.println("You found " + gold + " gold!");
        } else if (treasureType < 80) {
            Potion potion = availablePotions.get(random.nextInt(availablePotions.size()));
            player.addItem(potion);
            System.out.println("You found a " + potion.getName() + "!");
        } else {
            if (random.nextBoolean() && !availableWeapons.isEmpty()) {
                Weapon weapon = availableWeapons.get(random.nextInt(availableWeapons.size()));
                player.addItem(weapon);
                System.out.println("You found a " + weapon.getName() + "!");
            } else if (!availableArmor.isEmpty()) {
                Armor armor = availableArmor.get(random.nextInt(availableArmor.size()));
                player.addItem(armor);
                System.out.println("You found " + armor.getName() + "!");
            }
        }
    }
    
    private void findSafeHaven() {
        System.out.println("You find a peaceful clearing. It's safe to rest here.");
        System.out.println("Your HP and MP are fully restored!");
        
        player.heal(player.getMaxHp() - player.getHp());
        player.restoreMana(player.getMaxMana() - player.getMana());
    }
    
    private void rest() {
        System.out.println("\nYou take time to rest and recover...");
        
        int hpHealed = player.getMaxHp() / 2;
        int mpHealed = player.getMaxMana() / 2;
        
        player.heal(hpHealed);
        player.restoreMana(mpHealed);
        
        System.out.println("Restored " + hpHealed + " HP and " + mpHealed + " MP.");
        System.out.println("Current HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("Current MP: " + player.getMana() + "/" + player.getMaxMana());
    }
    
    private void visitShop() {
        System.out.println("\n=== WELCOME TO THE SHOP ===");
        System.out.println("Your gold: " + player.getGold());
        
        boolean shopping = true;
        while (shopping) {
            System.out.println("\n1. Buy Weapons");
            System.out.println("2. Buy Armor");
            System.out.println("3. Buy Potions");
            System.out.println("4. Sell Items");
            System.out.println("5. Leave Shop");
            System.out.print("What would you like to do? ");
            
            int choice = getMenuChoice(1, 5);
            
            switch (choice) {
                case 1 -> buyWeapons();
                case 2 -> buyArmor();
                case 3 -> buyPotions();
                case 4 -> sellItems();
                case 5 -> shopping = false;
            }
        }
        System.out.println("Thank you for your business!");
    }
    
    private void buyWeapons() {
        System.out.println("\n=== WEAPONS ===");
        for (int i = 0; i < availableWeapons.size(); i++) {
            Weapon weapon = availableWeapons.get(i);
            int price = calculatePrice(weapon.getValue());
            System.out.println((i + 1) + ". " + weapon.getName() + " (+" + 
                             weapon.getAttackBonus() + " ATK) - " + 
                             price + " gold");
        }
        System.out.println((availableWeapons.size() + 1) + ". Back");
        
        int choice = getMenuChoice(1, availableWeapons.size() + 1);
        if (choice <= availableWeapons.size()) {
            Weapon weapon = availableWeapons.get(choice - 1);
            int price = calculatePrice(weapon.getValue());
            
            if (player.getGold() >= price) {
                player.addItem(weapon);
                player.gainGold(-price);
                System.out.println("Purchased " + weapon.getName() + " for " + price + " gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        }
    }
    
    private void buyArmor() {
        System.out.println("\n=== ARMOR ===");
        for (int i = 0; i < availableArmor.size(); i++) {
            Armor armor = availableArmor.get(i);
            int price = calculatePrice(armor.getValue());
            System.out.println((i + 1) + ". " + armor.getName() + " (+" + 
                             armor.getDefenseBonus() + " DEF) - " + 
                             price + " gold");
        }
        System.out.println((availableArmor.size() + 1) + ". Back");
        
        int choice = getMenuChoice(1, availableArmor.size() + 1);
        if (choice <= availableArmor.size()) {
            Armor armor = availableArmor.get(choice - 1);
            int price = calculatePrice(armor.getValue());
            
            if (player.getGold() >= price) {
                player.addItem(armor);
                player.gainGold(-price);
                System.out.println("Purchased " + armor.getName() + " for " + price + " gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        }
    }
    
    private void buyPotions() {
        System.out.println("\n=== POTIONS ===");
        System.out.println("1. Health Potion (20 HP) - 30 gold");
        System.out.println("2. Greater Health Potion (50 HP) - 70 gold");
        System.out.println("3. Back");
        
        int choice = getMenuChoice(1, 3);
        if (choice == 1) {
            if (player.getGold() >= 30) {
                player.addItem(Potion.createHealthPotion());
                player.gainGold(-30);
                System.out.println("Purchased Health Potion for 30 gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        } else if (choice == 2) {
            if (player.getGold() >= 70) {
                player.addItem(Potion.createGreaterHealthPotion());
                player.gainGold(-70);
                System.out.println("Purchased Greater Health Potion for 70 gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        }
    }
    
    private int calculatePrice(int baseValue) {
        // Shop prices are 2x base value, with minimum price
        return Math.max(10, baseValue * 2);
    }
    
    private void sellItems() {
        System.out.println("\nSelling feature coming soon!");
        System.out.println("For now, you can only acquire new equipment through exploration.");
    }
    
    private void exitGame() {
        System.out.println("\nThank you for playing D&D Anbennar!");
        System.out.println("Final Stats:");
        player.showStats();
        gameRunning = false;
    }
    
    private int getMenuChoice(int min, int max) {
        while (true) {
            try {
                int choice = sc.nextInt();
                sc.nextLine(); // Clear the buffer
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (Exception e) {
                System.out.print("Please enter a valid number: ");
                sc.next(); // Clear invalid input
            }
        }
    }
}