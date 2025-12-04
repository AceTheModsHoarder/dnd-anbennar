package dnd.combat;

import dnd.characters.Monster;
import dnd.characters.Player;
import dnd.skills.Skill;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CombatSystem {
    private static final Scanner sc = new Scanner(System.in);
    private static final Random random = new Random();
    private static final List<String> combatLog = new ArrayList<>();
    
    public static CombatResult start(Player p, Monster m) {
        System.out.println("\nA wild " + m.getName() + " appears!");
        combatLog.clear();
        combatLog.add("Combat started between " + p.getName() + " and " + m.getName());
        
        CombatResult result = new CombatResult(); // Create result early for logging
        
        // Initiative roll
        boolean playerFirst = initiativeRoll(p, m);
        
        while (p.isAlive() && m.isAlive()) {
            displayCombatStatus(p, m);
            
            if (playerFirst) {
                playerTurn(p, m, result);
                if (!m.isAlive()) break;
                monsterTurn(p, m, result);
            } else {
                monsterTurn(p, m, result);
                if (!p.isAlive()) break;
                playerTurn(p, m, result);
            }
        }
        
        return concludeCombat(p, m, result);
    }
    
    private static boolean initiativeRoll(Player p, Monster m) {
        int playerInitiative = p.getDexterity() + random.nextInt(20) + 1;
        int monsterInitiative = m.getDexterity() + random.nextInt(20) + 1;
        
        System.out.println("\n--- Initiative Roll ---");
        System.out.println(p.getName() + " rolls: " + playerInitiative);
        System.out.println(m.getName() + " rolls: " + monsterInitiative);
        
        if (playerInitiative >= monsterInitiative) {
            System.out.println(p.getName() + " goes first!");
            return true;
        } else {
            System.out.println(m.getName() + " goes first!");
            return false;
        }
    }
    
    private static void playerTurn(Player p, Monster m, CombatResult result) {
        System.out.println("\n=== YOUR TURN ===");
        System.out.println("1. Attack");
        System.out.println("2. Use Skill");
        System.out.println("3. Use Item");
        System.out.println("4. Defend");
        System.out.println("5. Run");
        System.out.print("Choice: ");
        
        int choice = sc.nextInt();
        
        switch (choice) {
            case 1 -> performAttack(p, m, result, true);
            case 2 -> useSkill(p, m);
            case 3 -> useItem(p);
            case 4 -> defend(p);
            case 5 -> attemptEscape(p, m);
            default -> System.out.println("Invalid choice. You hesitate and lose your turn!");
        }
    }
    
    private static void performAttack(Player p, Monster m, CombatResult result, boolean isPlayerAttack) {
        int attackRoll = random.nextInt(20) + 1;
        boolean isCritical = (attackRoll == 20);
        
        // GUARANTEED HIT WITH EVASION CHANCE
        // Evasion = Dexterity / 2 (rounded down)
        int attackerDex = isPlayerAttack ? p.getDexterity() : m.getDexterity();
        int defenderDex = isPlayerAttack ? m.getDexterity() : p.getDexterity();
        int evasionChance = Math.max(5, defenderDex / 2); // Minimum 5% evasion
        
        // Check for evasion (miss)
        boolean isEvaded = random.nextInt(100) < evasionChance;
        
        StringBuilder damageLog = new StringBuilder();
        String attackerName = isPlayerAttack ? p.getName() : m.getName();
        String defenderName = isPlayerAttack ? m.getName() : p.getName();
        
        damageLog.append(attackerName).append(" attacks ").append(defenderName).append("\n");
        damageLog.append("Attack Roll: ").append(attackRoll);
        if (isCritical) damageLog.append(" (CRITICAL!)");
        damageLog.append("\nDefender Evasion: ").append(evasionChance).append("%");
        
        if (isEvaded && !isCritical) {
            // Critical hits cannot be evaded
            damageLog.append("\n").append(defenderName).append(" EVADED the attack!");
            System.out.println(defenderName + " evaded the attack!");
            combatLog.add(attackerName + " missed " + defenderName + " (evaded)");
            
        } else {
            // HIT - Calculate damage
            int baseDamage = isPlayerAttack ? p.getAttack() : m.getAttack();
            if (isCritical) {
                baseDamage *= 2;
                damageLog.append("\nCritical multiplier: x2 = ").append(baseDamage);
            }
            
            if (isPlayerAttack) {
                // Player attacking monster
                int effectiveArmor = p.calculateEffectiveArmor(m);
                int monsterResistance = m.getDamageResistance();
                
                double armorReducedDamage = baseDamage * (100.0 / (100 + effectiveArmor));
                damageLog.append("\nAfter Armor (").append(effectiveArmor).append("): ");
                damageLog.append(baseDamage).append(" * (100 / (100 + ").append(effectiveArmor).append(")) = ");
                damageLog.append(String.format("%.1f", armorReducedDamage));
                
                // Apply resistance (flat subtraction)
                double afterResistance = armorReducedDamage - monsterResistance;
                damageLog.append("\nAfter Resistance (").append(monsterResistance).append("): ");
                damageLog.append(String.format("%.1f", armorReducedDamage)).append(" - ").append(monsterResistance).append(" = ");
                damageLog.append(String.format("%.1f", afterResistance));
                
                int finalDamage = (int) Math.max(1, Math.round(afterResistance));
                damageLog.append("\nFinal Damage: ").append(finalDamage);
                
                int hpBefore = m.getHp();
                m.takeDamage(baseDamage);
                int hpAfter = m.getHp();
                int actualDamage = hpBefore - hpAfter;
                
                damageLog.append("\n").append(defenderName).append(" HP: ").append(hpBefore).append(" -> ").append(hpAfter);
                
                System.out.println(attackerName + " hits " + defenderName + " for " + actualDamage + " damage!");
                combatLog.add(attackerName + " hits " + defenderName + " for " + actualDamage + " damage!");
                
            } else {
                // Monster attacking player
                if (p.isDefending()) {
                    baseDamage = Math.max(1, baseDamage / 2);
                    damageLog.append("\nPlayer defending! Damage halved: ").append(baseDamage);
                }
                
                int playerDefense = p.getDefense();
                int finalDamage = Math.max(1, baseDamage - playerDefense);
                damageLog.append("\nPlayer Defense: ").append(playerDefense);
                damageLog.append("\nFinal Damage: ").append(baseDamage).append(" - ").append(playerDefense).append(" = ").append(finalDamage);
                
                int hpBefore = p.getHp();
                p.takeDamage(finalDamage);
                int hpAfter = p.getHp();
                
                damageLog.append("\n").append(defenderName).append(" HP: ").append(hpBefore).append(" -> ").append(hpAfter);
                
                System.out.println(attackerName + " hits " + defenderName + " for " + finalDamage + " damage!");
                combatLog.add(attackerName + " hits " + defenderName + " for " + finalDamage + " damage!");
            }
        }
        
        // Add to damage log
        result.addDamageLog(damageLog.toString());
    }
    
    private static void useSkill(Player p, Monster m) {
        List<Skill> skills = p.getSkills();
        if (skills.isEmpty()) {
            System.out.println("You have no skills available!");
            return;
        }
        
        System.out.println("\nAvailable Skills:");
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            System.out.println((i + 1) + ". " + skill.getName() + 
                             " (Cost: " + skill.getManaCost() + " MP) - " + skill.getDescription());
        }
        System.out.print("Choose skill: ");
        
        int choice = sc.nextInt() - 1;
        if (choice >= 0 && choice < skills.size()) {
            Skill skill = skills.get(choice);
            if (p.getMana() >= skill.getManaCost()) {
                skill.use(p, m);
                p.reduceMana(skill.getManaCost());
                combatLog.add(p.getName() + " used " + skill.getName() + " on " + m.getName());
            } else {
                System.out.println("Not enough mana!");
            }
        } else {
            System.out.println("Invalid skill choice!");
        }
    }
    
    private static void useItem(Player p) {
        p.useItemMenu(sc);
        combatLog.add(p.getName() + " used an item");
    }
    
    private static void defend(Player p) {
        p.setDefending(true);
        System.out.println("You take a defensive stance! Damage reduced next turn.");
        combatLog.add(p.getName() + " is defending");
    }
    
    private static void attemptEscape(Player p, Monster m) {
        int escapeChance = 40 + p.getDexterity() - m.getDexterity();
        if (random.nextInt(100) < escapeChance) {
            System.out.println("You successfully escaped!");
            combatLog.add(p.getName() + " escaped from combat");
        } else {
            System.out.println("Escape failed!");
            combatLog.add(p.getName() + " failed to escape");
        }
    }
    
    private static void monsterTurn(Player p, Monster m, CombatResult result) {
        System.out.println("\n=== " + m.getName().toUpperCase() + "'S TURN ===");
        
        if (m.getHp() < m.getMaxHp() * 0.3 && random.nextInt(100) < 30) {
            useMonsterAbility(m, p, result);
        } else {
            performAttack(p, m, result, false);
        }
        
        p.setDefending(false);
    }
    
    private static void useMonsterAbility(Monster m, Player p, CombatResult result) {
        System.out.println(m.getName() + " uses a special attack!");
        
        StringBuilder damageLog = new StringBuilder();
        damageLog.append(m.getName()).append(" uses special attack on ").append(p.getName()).append("\n");
        
        int specialDamage = m.getAttack() + 5;
        damageLog.append("Special Damage: ").append(m.getAttack()).append(" + 5 = ").append(specialDamage);
        
        if (p.isDefending()) {
            specialDamage = Math.max(1, specialDamage / 2);
            damageLog.append("\nPlayer defending! Damage halved: ").append(specialDamage);
        }
        
        int playerDefense = p.getDefense();
        int finalDamage = Math.max(1, specialDamage - playerDefense);
        damageLog.append("\nPlayer Defense: ").append(playerDefense);
        damageLog.append("\nFinal Damage: ").append(specialDamage).append(" - ").append(playerDefense).append(" = ").append(finalDamage);
        
        int hpBefore = p.getHp();
        p.takeDamage(finalDamage);
        int hpAfter = p.getHp();
        
        damageLog.append("\n").append(p.getName()).append(" HP: ").append(hpBefore).append(" -> ").append(hpAfter);
        
        result.addDamageLog(damageLog.toString());
        
        System.out.println("It deals " + finalDamage + " damage!");
        combatLog.add(m.getName() + " used special attack on " + p.getName());
    }
    
    private static void displayCombatStatus(Player p, Monster m) {
        System.out.println("\n=== COMBAT STATUS ===");
        System.out.println(p.getName() + ": " + p.getHp() + "/" + p.getMaxHp() + " HP | " + 
                          p.getMana() + "/" + p.getMaxMana() + " MP");
        System.out.println(m.getName() + ": " + m.getHp() + "/" + m.getMaxHp() + " HP");
        System.out.println("---------------------");
    }
    
    private static CombatResult concludeCombat(Player p, Monster m, CombatResult result) {
        if (p.isAlive()) {
            int expGained = m.getExpReward();
            int goldGained = m.getGoldReward();
            
            p.gainExperience(expGained);
            p.gainGold(goldGained);
            
            System.out.println("\n=== VICTORY! ===");
            System.out.println("You gained " + expGained + " experience and " + goldGained + " gold!");
            
            result.setVictory(true);
            result.setExpGained(expGained);
            result.setGoldGained(goldGained);
        } else {
            System.out.println("\n=== DEFEAT ===");
            System.out.println("You have fallen in battle...");
            result.setVictory(false);
        }
        
        // Display combat log
        System.out.println("\n--- Combat Log ---");
        for (String log : combatLog) {
            System.out.println(log);
        }
        
        // Display damage calculation log
        result.printDamageLog();
        
        return result;
    }
}