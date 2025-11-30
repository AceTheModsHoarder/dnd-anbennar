package dnd.characters;

import dnd.items.Weapon;
import dnd.items.Armor;
import dnd.items.Potion;
import dnd.skills.Skill;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String name;
    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int attack;
    private int defense;
    private int dexterity;
    private int armorClass;
    private int level;
    private int experience;
    private int gold;
    private boolean isAlive;
    private boolean isDefending;
    private Weapon equippedWeapon;
    private Armor equippedArmor;
    private List<Object> inventory;
    private List<Skill> skills; // Add this line
    
    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.gold = 0;
        this.maxHp = 20;
        this.hp = maxHp;
        this.maxMana = 10;
        this.mana = maxMana;
        this.attack = 5;
        this.defense = 3;
        this.dexterity = 10;
        this.armorClass = 12;
        this.isAlive = true;
        this.isDefending = false;
        this.inventory = new ArrayList<>();
        this.skills = new ArrayList<>(); // Initialize skills list
        initializeSkills(); // Add basic skills
    }
    
    private void initializeSkills() {
        // Add some basic skills - you can create these classes later
        // skills.add(new BasicAttack());
        // skills.add(new Fireball());
        // skills.add(new Heal());
    }
    
    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getAttack() { 
        int baseAttack = attack;
        if (equippedWeapon != null) {
            baseAttack += equippedWeapon.getAttackBonus();
        }
        return baseAttack;
    }
    public int getDefense() { 
        int baseDefense = defense;
        if (equippedArmor != null) {
            baseDefense += equippedArmor.getDefenseBonus();
        }
        return baseDefense;
    }
    public int getDexterity() { return dexterity; }
    public int getArmorClass() { return armorClass; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getGold() { return gold; }
    public boolean isAlive() { return isAlive; }
    public boolean isDefending() { return isDefending; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public List<Object> getInventory() { return inventory; }
    public List<Skill> getSkills() { return skills; } // Add this getter
    
    // Setters
    public void setDefending(boolean defending) { this.isDefending = defending; }
    public void setEquippedWeapon(Weapon weapon) { this.equippedWeapon = weapon; }
    public void setEquippedArmor(Armor armor) { this.equippedArmor = armor; }
    
    // Combat methods
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        if (hp == 0) {
            isAlive = false;
        }
    }
    
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }
    
    public void reduceMana(int amount) {
        mana = Math.max(0, mana - amount);
    }
    
    public void restoreMana(int amount) {
        mana = Math.min(maxMana, mana + amount);
    }
    
    // Inventory methods
    public void addItem(Object item) {
        inventory.add(item);
    }
    
    public void useItemMenu(Scanner scanner) {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty!");
            return;
        }
        
        System.out.println("\n=== INVENTORY ===");
        for (int i = 0; i < inventory.size(); i++) {
            Object item = inventory.get(i);
            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                System.out.println((i + 1) + ". " + potion.getName() + " - " + potion.getDescription());
            } else if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                System.out.println((i + 1) + ". " + weapon.getName() + " (+" + weapon.getAttackBonus() + " ATK)");
            } else if (item instanceof Armor) {
                Armor armor = (Armor) item;
                System.out.println((i + 1) + ". " + armor.getName() + " (+" + armor.getDefenseBonus() + " DEF)");
            }
        }
        System.out.println((inventory.size() + 1) + ". Back");
        System.out.print("Choose item to use: ");
        
        int choice = scanner.nextInt() - 1;
        if (choice >= 0 && choice < inventory.size()) {
            Object item = inventory.get(choice);
            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                potion.use(this);
                inventory.remove(choice);
                System.out.println("Used " + potion.getName());
            } else if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                if (equippedWeapon != null) {
                    inventory.add(equippedWeapon);
                }
                equippedWeapon = weapon;
                inventory.remove(weapon);
                System.out.println("Equipped " + weapon.getName());
            } else if (item instanceof Armor) {
                Armor armor = (Armor) item;
                if (equippedArmor != null) {
                    inventory.add(equippedArmor);
                }
                equippedArmor = armor;
                inventory.remove(armor);
                System.out.println("Equipped " + armor.getName());
            }
        }
    }
    
    // Display methods
    public void showStats() {
        System.out.println("\n=== CHARACTER STATUS ===");
        System.out.println("Name: " + name);
        System.out.println("Level: " + level);
        System.out.println("HP: " + hp + "/" + maxHp);
        System.out.println("MP: " + mana + "/" + maxMana);
        System.out.println("Exp: " + experience + "/" + (level * 100));
        System.out.println("Gold: " + gold);
        System.out.println("Weapon: " + (equippedWeapon != null ? equippedWeapon.getName() : "None"));
        System.out.println("Armor: " + (equippedArmor != null ? equippedArmor.getName() : "None"));
        System.out.println("Attack: " + getAttack());
        System.out.println("Defense: " + getDefense());
        System.out.println("Dexterity: " + dexterity);
        System.out.println("Armor Class: " + armorClass);
        
        // Show skills
        if (!skills.isEmpty()) {
            System.out.println("\nSkills:");
            for (Skill skill : skills) {
                System.out.println("  - " + skill.getName() + " (" + skill.getManaCost() + " MP)");
            }
        }
    }
    
    // Skill methods
    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    
    public boolean hasSkills() {
        return !skills.isEmpty();
    }
    
    // Progression methods
    public void gainExperience(int exp) {
        experience += exp;
        checkLevelUp();
    }
    
    public void gainGold(int amount) {
        gold += amount;
    }
    
    private void checkLevelUp() {
        int expNeeded = level * 100;
        if (experience >= expNeeded) {
            levelUp();
        }
    }
    
    private void levelUp() {
        level++;
        maxHp += 5;
        hp = maxHp;
        maxMana += 3;
        mana = maxMana;
        attack += 2;
        defense += 1;
        dexterity += 1;
        
        System.out.println("Level up! You are now level " + level + "!");
        System.out.println("HP: " + maxHp + ", MP: " + maxMana + ", Attack: " + attack);
    }
}