package dnd;

import dnd.characters.Monster;
import dnd.characters.Player;
import dnd.combat.CombatResult;
import dnd.combat.CombatSystem;
import dnd.items.Armor;
import dnd.items.Weapon;
import dnd.skills.Fireball;
import dnd.skills.Heal;
import java.util.Random;
import java.util.Scanner;

public class GameManager {
    private static Scanner sc = new Scanner(System.in);
    private static Random random = new Random();
    private Player player;
    
    public void startGame() {
        initializePlayer();
        gameLoop();
    }
    
    private void initializePlayer() {
        System.out.print("Enter your character name: ");
        String name = sc.nextLine();
        player = new Player(name);
        
        // Give starting equipment and skills
        player.setEquippedWeapon(Weapon.createDagger());
        player.setEquippedArmor(Armor.createLeatherArmor());
        player.addSkill(new Fireball());
        player.addSkill(new Heal());
        
        System.out.println("Welcome, " + player.getName() + "!");
        System.out.println("You start with a Dagger and Leather Armor.");
    }
    
    private void gameLoop() {
        while (player.isAlive()) {
            displayMenu();
            int choice = sc.nextInt();
            
            switch (choice) {
                case 1:
                    explore();
                    break;
                case 2:
                    displayStatus();
                    break;
                case 3:
                    System.out.println("Thanks for playing!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
        
        System.out.println("Game Over! You reached level " + player.getLevel());
    }
    
    private void displayMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Explore");
        System.out.println("2. Check Status");
        System.out.println("3. Quit");
        System.out.print("Choice: ");
    }
    
    private void explore() {
        System.out.println("\nYou venture into the wilderness...");
        
        // 70% chance of encounter
        if (random.nextInt(100) < 70) {
            Monster monster = generateRandomMonster();
            CombatSystem combat = new CombatSystem();
            CombatResult result = combat.start(player, monster);
            
            if (!player.isAlive()) {
                return;
            }
        } else {
            System.out.println("The area is peaceful. No enemies found.");
        }
    }
    
    private Monster generateRandomMonster() {
        int roll = random.nextInt(100);
        if (roll < 50) {
            return Monster.createGoblin();
        } else if (roll < 85) {
            return Monster.createOrc();
        } else {
            return Monster.createDragon();
        }
    }
    
    private void displayStatus() {
        System.out.println("\n=== CHARACTER STATUS ===");
        System.out.println("Name: " + player.getName());
        System.out.println("Level: " + player.getLevel());
        System.out.println("HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("MP: " + player.getMana() + "/" + player.getMaxMana());
        System.out.println("Exp: " + player.getExperience() + "/" + (player.getLevel() * 100));
        System.out.println("Gold: " + player.getGold());
        System.out.println("Weapon: " + 
            (player.getEquippedWeapon() != null ? player.getEquippedWeapon().getName() : "None"));
        System.out.println("Armor: " + 
            (player.getEquippedArmor() != null ? player.getEquippedArmor().getName() : "None"));
    }
    
    public static void main(String[] args) {
        GameManager game = new GameManager();
        game.startGame();
    }

   
}