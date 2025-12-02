package dnd;

import dnd.characters.Monster;
import dnd.characters.MonsterFactory;
import dnd.characters.Player;
import dnd.items.Armor;
import dnd.items.Consumable;
import dnd.items.Potion;
import dnd.items.Weapon; // Changed from Potion
import dnd.skills.BasicAttack;
import dnd.skills.Fireball;
import dnd.skills.Heal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameManager {
    private Scanner sc = new Scanner(System.in);
    private Random random = new Random();
    private Player player;
    private boolean gameRunning;
    
    // Available monsters for encounters
    private List<Monster> monsters = new ArrayList<>();
    
    public void startGame() {
        initializeGame();
        gameLoop();
    }
    
    private void initializeGame() {
        System.out.println("=== WELCOME TO D&D ANBENNAR ===");
        System.out.print("Enter your character name: ");
        String name = sc.nextLine();
        
        // Create player with enhanced constructor
        player = new Player(name);
        
        // Add starting skills
        player.addSkill(new BasicAttack());
        player.addSkill(new Fireball());
        player.addSkill(new Heal());
        
        // Add starting items
        player.addItem(new Weapon("Rusty Dagger", 1));
        player.addItem(Consumable.createHealthPotion());
        
        // Equip starting gear
        player.setEquippedWeapon(new Weapon("Rusty Dagger", 1));
        player.setEquippedArmor(new Armor("Leather Vest", 1));
        
        // Initialize monsters
        initializeMonsters();
        
        gameRunning = true;
        
        System.out.println("\nWelcome, " + player.getName() + "!");
        System.out.println("You start your adventure with a Rusty Dagger and Leather Vest.");
        System.out.println("You have learned: Power Attack, Fireball, and Heal skills.");
    }
    
    private void initializeMonsters() {
    // Clear existing monsters
    monsters.clear();
    
    // ADD MONSTERS USING MonsterFactory:
    monsters.add(MonsterFactory.createGoblin());
    monsters.add(MonsterFactory.createRat());
    monsters.add(MonsterFactory.createSpider());
    monsters.add(MonsterFactory.createWolf());
    monsters.add(MonsterFactory.createOrc());
    monsters.add(MonsterFactory.createSkeleton());
    monsters.add(MonsterFactory.createDragon());
    monsters.add(MonsterFactory.createAncientDragon());
    monsters.add(MonsterFactory.createVampire());
    // Add more as needed...
    
    // Remove or comment this line unless your friend created it:
    // monsters.add(MonsterFactory.createYourMonster());
    
    System.out.println("Loaded " + monsters.size() + " different monsters!");
}

    private int determineMonsterTier(Monster monster) {
        // Determine tier based on monster HP
        if (monster.getMaxHp() <= 15) return 1; // Tier 1
        if (monster.getMaxHp() <= 30) return 2; // Tier 2  
        if (monster.getMaxHp() <= 50) return 3; // Tier 3
        return 4; // Tier 4 (Boss)
    }
    
    private void gameLoop() {
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
        
        // Random encounter roll
        int encounterRoll = random.nextInt(100);
        
        if (encounterRoll < 60) { // 60% chance: Normal monster
            encounterMonster();
        } else if (encounterRoll < 85) { // 25% chance: No encounter
            int event = random.nextInt(100);
            if (event < 40) {
                findTreasure();
            } else if (event < 60) {
                findSafeHaven();
            } else {
                System.out.println("The path is quiet. You find nothing of interest.");
            }
        } else if (encounterRoll < 95) { // 10% chance: Multiple monsters
            System.out.println("You encounter multiple enemies!");
            encounterMultipleMonsters();
        } else { // 5% chance: Rare/boss monster
            encounterSpecialMonster();
        }
    }

    private void encounterMultipleMonsters() {
        int numMonsters = 2 + random.nextInt(3); // 2-4 monsters
        System.out.println("There are " + numMonsters + " enemies!");
        
        int monstersDefeated = 0;
        int totalExp = 0;
        int totalGold = 0;
        
        for (int i = 0; i < numMonsters && player.isAlive(); i++) {
            Monster monster = getRandomMonster();
            System.out.println("\nEnemy #" + (i + 1) + ": " + monster.getName());
            
            dnd.combat.CombatResult result = dnd.combat.CombatSystem.start(player, monster);
            
            if (result.isVictory()) {
                monstersDefeated++;
                totalExp += (monster.getMaxHp() + monster.getAttack() + monster.getArmor()) * 2;
                totalGold += (monster.getMaxHp() + monster.getAttack()) / 2;
                
                // Heal a bit between fights
                if (i < numMonsters - 1 && player.isAlive()) {
                    player.heal(5);
                    System.out.println("You catch your breath and heal 5 HP before the next enemy.");
                }
            } else {
                break;
            }
        }
        
        if (monstersDefeated > 0) {
            System.out.println("\nYou defeated " + monstersDefeated + " out of " + numMonsters + " enemies!");
            if (monstersDefeated == numMonsters) {
                System.out.println("CLEARED ALL ENEMIES!");
                // Bonus for clearing all
                totalExp *= 1.5;
                totalGold *= 2;
            }
            
            player.gainExperience(totalExp);
            player.gainGold(totalGold);
            System.out.println("Total gained: " + totalExp + " exp and " + totalGold + " gold!");
            
            // Chance for loot after multiple kills
            if (random.nextInt(100) < 70) {
                dropLoot();
            }
        }
    }
    
    private void encounterMonster() {
        Monster monster = getRandomMonster();
        System.out.println("A wild " + monster.getName() + " appears!");
        
        // Start combat using the updated CombatSystem
        dnd.combat.CombatResult result = dnd.combat.CombatSystem.start(player, monster);
        
        if (result.isVictory()) {
            System.out.println("You emerge victorious from the battle!");
            
            // Calculate rewards based on monster
            int expGained = (monster.getMaxHp() + monster.getAttack() + monster.getArmor()) * 2;
            int goldGained = (monster.getMaxHp() + monster.getAttack()) / 2;
            
            player.gainExperience(expGained);
            player.gainGold(goldGained);
            
            System.out.println("You gained " + expGained + " experience and " + goldGained + " gold!");
            
            // Chance for loot drop (80% chance)
            if (random.nextInt(100) < 80) {
                dropLoot();
            }
            
            // Always get minimum gold
            int minGold = 5 + (player.getLevel() * 2);
            player.gainGold(minGold);
            System.out.println("You also find " + minGold + " gold on the corpse.");
            
        } else if (!player.isAlive()) {
            System.out.println("You have been defeated...");
        } else {
            System.out.println("You fled from battle.");
        }
    }
    
    private Monster getRandomMonster() {
    // Get monsters appropriate for player level
    List<Monster> appropriateMonsters = new ArrayList<>();
    int playerLevel = player.getLevel();
    
    for (Monster monster : monsters) {
        // Simple tier determination based on monster HP
        int monsterTier = determineMonsterTier(monster);
        int playerTier = (playerLevel - 1) / 3 + 1; // Level 1-3: Tier 1, 4-6: Tier 2, etc.
        
        // Allow monsters from same tier and one tier below
        if (monsterTier <= playerTier && monsterTier >= playerTier - 1) {
            appropriateMonsters.add(monster);
        }
    }
    
        // If no appropriate monsters found, return any monster
        if (appropriateMonsters.isEmpty()) {
            appropriateMonsters = monsters;
        }
        
        return appropriateMonsters.get(random.nextInt(appropriateMonsters.size()));
    }

        private void encounterSpecialMonster() {
    System.out.println("\nA rare creature appears!");
    
    // List of special/boss monsters - USE MonsterFactory:
    Monster[] specialMonsters = {
        MonsterFactory.createAncientDragon(),
        MonsterFactory.createLich(),
        MonsterFactory.createDemonLord(),
        MonsterFactory.createBeholder(),
        MonsterFactory.createHydra(),
        MonsterFactory.createVampire(),
        MonsterFactory.createGorgon()
    };
    
    Monster specialMonster = specialMonsters[random.nextInt(specialMonsters.length)];
    System.out.println("It's a " + specialMonster.getName() + "!");
        
        // Special monsters give better rewards
        dnd.combat.CombatResult result = dnd.combat.CombatSystem.start(player, specialMonster);
        
        if (result.isVictory()) {
            System.out.println("You defeated the rare " + specialMonster.getName() + "!");
            
            // Triple rewards for rare monsters
            int expGained = (specialMonster.getMaxHp() + specialMonster.getAttack() + specialMonster.getArmor()) * 6;
            int goldGained = (specialMonster.getMaxHp() + specialMonster.getAttack()) * 2;
            
            player.gainExperience(expGained);
            player.gainGold(goldGained);
            
            System.out.println("You gained " + expGained + " experience and " + goldGained + " gold!");
            
            // Guaranteed rare loot
            System.out.println("\n=== RARE LOOT ===");
            dropRareLoot();
            
        } else if (!player.isAlive()) {
            System.out.println("You have been defeated by the " + specialMonster.getName() + "...");
        }
    }

    private void dropRareLoot() {
        // Guaranteed multiple high-quality items
        System.out.println("The rare creature drops exceptional loot:");
        
        // Always get a lot of gold
        int gold = 100 + random.nextInt(200);
        player.gainGold(gold);
        System.out.println("  " + gold + " gold coins");
        
        // Always get a rare weapon or armor
        if (random.nextBoolean()) {
            Weapon rareWeapon = new Weapon("Rare " + getRandomWeaponName(), 6 + random.nextInt(4), 
                                        200 + random.nextInt(300), 20 + random.nextInt(20), 5 + random.nextInt(6));
            player.addItem(rareWeapon);
            System.out.println("  " + rareWeapon.getName() + " (+" + rareWeapon.getAttackBonus() + " ATK)");
        } else {
            Armor rareArmor = new Armor("Rare " + getRandomArmorName(), 4 + random.nextInt(4), 
                                    150 + random.nextInt(250));
            player.addItem(rareArmor);
            System.out.println("  " + rareArmor.getName() + " (+" + rareArmor.getDefenseBonus() + " DEF)");
        }
        
        // Potions
        for (int i = 0; i < 2 + random.nextInt(3); i++) {
            player.addItem(Consumable.createGreaterHealthPotion());
        }
        System.out.println("  Several Greater Health Potions");
    }

    private String getRandomWeaponName() {
        String[] weapons = {"Longsword", "Battle Axe", "Warhammer", "Greatsword", "Halberd", 
                        "Magic Staff", "Bow", "Crossbow", "Dagger", "Mace"};
        return weapons[random.nextInt(weapons.length)];
    }

    private String getRandomArmorName() {
        String[] armors = {"Plate Armor", "Chainmail", "Scale Mail", "Leather Armor", 
                        "Dragonhide", "Mithril Armor", "Enchanted Robes"};
        return armors[random.nextInt(armors.length)];
    }


    
    private void dropLoot() {
        System.out.println("\n=== LOOT DROPPED ===");
        
        // Multiple loot rolls
        int lootRolls = 1 + random.nextInt(3);
        
        for (int i = 0; i < lootRolls; i++) {
            int lootType = random.nextInt(100);
            
            if (lootType < 40) {
                // Gold
                int gold = 10 + random.nextInt(40);
                player.gainGold(gold);
                System.out.println("  Found " + gold + " gold!");
                
            } else if (lootType < 70) {
                // Potions
                if (random.nextBoolean()) {
                    player.addItem(Potion.createHealthPotion());
                    System.out.println("  Found a Health Potion!");
                } else {
                    player.addItem(Potion.createGreaterHealthPotion());
                    System.out.println("  Found a Greater Health Potion!");
                }
                
            } else if (lootType < 85) {
                // Weapons
                int weaponTier = Math.min(3, player.getLevel() / 3);
                Weapon weapon;
                
                if (weaponTier == 0) {
                    weapon = new Weapon("Rusty Sword", 2, 20, 0, 1);
                } else if (weaponTier == 1) {
                    weapon = new Weapon("Iron Sword", 3, 50, 0, 3);
                } else if (weaponTier == 2) {
                    weapon = new Weapon("Longsword", 4, 100, 0, 5);
                } else {
                    weapon = new Weapon("Magic Sword", 5, 200, 20, 5);
                }
                
                player.addItem(weapon);
                System.out.println("  Found a " + weapon.getName() + "!");
                
            } else {
                // Armor
                int armorTier = Math.min(2, player.getLevel() / 4);
                Armor armor;
                
                if (armorTier == 0) {
                    armor = new Armor("Leather Vest", 1, 25);
                } else if (armorTier == 1) {
                    armor = new Armor("Chain Shirt", 3, 80);
                } else {
                    armor = new Armor("Plate Armor", 5, 200);
                }
                
                player.addItem(armor);
                System.out.println("  Found " + armor.getName() + "!");
            }
        }
    }
    
    private void findTreasure() {
        System.out.println("\nYou discover a hidden treasure chest!");
        
        int chestQuality = 1 + (player.getLevel() / 3);
        int itemsInChest = 2 + random.nextInt(chestQuality);
        
        System.out.println("The chest contains " + itemsInChest + " items:");
        
        for (int i = 0; i < itemsInChest; i++) {
            int itemType = random.nextInt(100);
            
            if (itemType < 40) {
                // Gold
                int gold = (10 + random.nextInt(40)) * chestQuality;
                player.gainGold(gold);
                System.out.println("  " + gold + " gold coins");
                
            } else if (itemType < 70) {
                // Potions
                player.addItem(Potion.createHealthPotion());
                System.out.println("  Health Potion");
                
            } else if (itemType < 90) {
                // Equipment
                if (random.nextBoolean()) {
                    Weapon weapon = new Weapon("Treasure Sword", 3, 75, 10, 3);
                    player.addItem(weapon);
                    System.out.println("  " + weapon.getName());
                } else {
                    Armor armor = new Armor("Treasure Armor", 2, 50);
                    player.addItem(armor);
                    System.out.println("  " + armor.getName());
                }
                
            } else {
                // Special find
                int gold = 100 * chestQuality;
                player.gainGold(gold);
                System.out.println("  " + gold + " gold coins (special find!)");
            }
        }
    }
    
    private void findSafeHaven() {
        System.out.println("\nYou find a peaceful clearing. It's safe to rest here.");
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
        
        // Available shop items
        List<Weapon> shopWeapons = new ArrayList<>();
        List<Armor> shopArmor = new ArrayList<>();
        
        shopWeapons.add(new Weapon("Iron Sword", 3, 50, 0, 3));
        shopWeapons.add(new Weapon("Steel Axe", 4, 75, 10, 2));
        shopWeapons.add(new Weapon("Longbow", 3, 60, 0, 0));
        
        shopArmor.add(new Armor("Leather Armor", 2, 40));
        shopArmor.add(new Armor("Chainmail", 4, 120));
        shopArmor.add(new Armor("Plate Armor", 6, 250));
        
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
                case 1 -> buyWeapons(shopWeapons);
                case 2 -> buyArmor(shopArmor);
                case 3 -> buyPotions();
                case 4 -> sellItems();
                case 5 -> shopping = false;
            }
        }
        System.out.println("Thank you for your business!");
    }
    
    private void buyWeapons(List<Weapon> shopWeapons) {
        System.out.println("\n=== WEAPONS ===");
        for (int i = 0; i < shopWeapons.size(); i++) {
            Weapon weapon = shopWeapons.get(i);
            int price = calculatePrice(weapon.getValue());
            System.out.println((i + 1) + ". " + weapon.getName() + " (+" + 
                             weapon.getAttackBonus() + " ATK) - " + 
                             price + " gold");
        }
        System.out.println((shopWeapons.size() + 1) + ". Back");
        
        int choice = getMenuChoice(1, shopWeapons.size() + 1);
        if (choice <= shopWeapons.size()) {
            Weapon weapon = shopWeapons.get(choice - 1);
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
    
    private void buyArmor(List<Armor> shopArmor) {
        System.out.println("\n=== ARMOR ===");
        for (int i = 0; i < shopArmor.size(); i++) {
            Armor armor = shopArmor.get(i);
            int price = calculatePrice(armor.getValue());
            System.out.println((i + 1) + ". " + armor.getName() + " (+" + 
                             armor.getDefenseBonus() + " DEF) - " + 
                             price + " gold");
        }
        System.out.println((shopArmor.size() + 1) + ". Back");
        
        int choice = getMenuChoice(1, shopArmor.size() + 1);
        if (choice <= shopArmor.size()) {
            Armor armor = shopArmor.get(choice - 1);
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
            player.addItem(Consumable.createHealthPotion()); // Changed from Potion
            player.gainGold(-30);
            System.out.println("Purchased Health Potion for 30 gold!");
        } else {
            System.out.println("Not enough gold!");
        }
    } else if (choice == 2) {
        if (player.getGold() >= 70) {
            player.addItem(Consumable.createGreaterHealthPotion()); // Changed from Potion
            player.gainGold(-70);
            System.out.println("Purchased Greater Health Potion for 70 gold!");
        } else {
            System.out.println("Not enough gold!");
        }
    }
}
    
    private void sellItems() {
    System.out.println("\n=== SELL ITEMS ===");
    
    dnd.items.Inventory inventory = player.getInventory();
    
    // Check if inventory is empty using the new isEmpty() method
    if (inventory.isEmpty()) {
        System.out.println("Your inventory is empty!");
        return;
    }
    
    System.out.println("Your gold: " + player.getGold());
    System.out.println("\nItems in inventory:");
    
    int itemNumber = 1;
    
    // List weapons
    List<Weapon> weapons = inventory.getWeapons();
    for (int i = 0; i < weapons.size(); i++) {
        Weapon weapon = weapons.get(i);
        int sellPrice = weapon.getValue() / 2;
        System.out.println(itemNumber + ". " + weapon.getName() + 
                         " (+" + weapon.getAttackBonus() + " ATK) - " + 
                         sellPrice + " gold");
        itemNumber++;
    }
    
    // List armor
    List<Armor> armors = inventory.getArmors();
    for (int i = 0; i < armors.size(); i++) {
        Armor armor = armors.get(i);
        int sellPrice = armor.getValue() / 2;
        System.out.println(itemNumber + ". " + armor.getName() + 
                         " (+" + armor.getDefenseBonus() + " DEF) - " + 
                         sellPrice + " gold");
        itemNumber++;
    }
    
    // List consumables
    List<Consumable> consumables = inventory.getConsumables();
    for (int i = 0; i < consumables.size(); i++) {
        Consumable consumable = consumables.get(i);
        String name = consumable.getName();
        int sellPrice = name.contains("Greater") ? 25 : 10;
        System.out.println(itemNumber + ". " + name + " - " + sellPrice + " gold");
        itemNumber++;
    }
    
    int totalItems = weapons.size() + armors.size() + consumables.size();
    System.out.println((totalItems + 1) + ". Sell All");
    System.out.println((totalItems + 2) + ". Back");
    
    System.out.print("Choose item to sell: ");
    int choice = getMenuChoice(1, totalItems + 2);
    
    if (choice == totalItems + 2) {
        return; // Back
    } else if (choice == totalItems + 1) {
        // Sell all items
        int totalGold = 0;
        
        // Calculate weapon values
        for (Weapon weapon : weapons) {
            totalGold += weapon.getValue() / 2;
        }
        
        // Calculate armor values
        for (Armor armor : armors) {
            totalGold += armor.getValue() / 2;
        }
        
        // Calculate consumable values
        for (Consumable consumable : consumables) {
            String name = consumable.getName();
            totalGold += name.contains("Greater") ? 25 : 10;
        }
        
        System.out.print("Sell all items for " + totalGold + " gold? (y/n): ");
        sc.nextLine(); // Clear buffer
        String confirm = sc.nextLine().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            inventory.clear();
            player.gainGold(totalGold);
            System.out.println("Sold all items for " + totalGold + " gold!");
        } else {
            System.out.println("Sale cancelled.");
        }
        
    } else {
        // Sell single item
        int sellPrice = 0;
        String itemName = "";
        
        // Determine which item was selected
        if (choice <= weapons.size()) {
            // It's a weapon
            Weapon weapon = weapons.get(choice - 1);
            itemName = weapon.getName();
            sellPrice = weapon.getValue() / 2;
            inventory.removeWeapon(weapon);
            
        } else if (choice <= weapons.size() + armors.size()) {
            // It's armor
            int armorIndex = choice - weapons.size() - 1;
            Armor armor = armors.get(armorIndex);
            itemName = armor.getName();
            sellPrice = armor.getValue() / 2;
            inventory.removeArmor(armor);
            
        } else {
            // It's a consumable
            int consumableIndex = choice - weapons.size() - armors.size() - 1;
            Consumable consumable = consumables.get(consumableIndex);
            itemName = consumable.getName();
            sellPrice = itemName.contains("Greater") ? 25 : 10;
            inventory.removeConsumable(consumable);
        }
        
        player.gainGold(sellPrice);
        System.out.println("Sold " + itemName + " for " + sellPrice + " gold!");
    }
}
    
    private int calculatePrice(int baseValue) {
        // Shop prices are 2x base value
        return Math.max(10, baseValue * 2);
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
    
    public static void main(String[] args) {
        GameManager game = new GameManager();
        game.startGame();
    }
}