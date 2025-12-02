package dnd.combat;

import java.util.ArrayList;
import java.util.List;

public class CombatResult {
    private boolean victory;
    private int expGained;
    private int goldGained;
    private List<String> damageLog = new ArrayList<>(); // NEW: Track damage calculations
    
    // Getters and setters
    public boolean isVictory() { return victory; }
    public void setVictory(boolean victory) { this.victory = victory; }
    public int getExpGained() { return expGained; }
    public void setExpGained(int expGained) { this.expGained = expGained; }
    public int getGoldGained() { return goldGained; }
    public void setGoldGained(int goldGained) { this.goldGained = goldGained; }
    
    // NEW: Damage log methods
    public void addDamageLog(String log) {
        damageLog.add(log);
    }
    
    public List<String> getDamageLog() {
        return damageLog;
    }
    
    public void printDamageLog() {
        if (!damageLog.isEmpty()) {
            System.out.println("\n=== DAMAGE CALCULATION LOG ===");
            for (String log : damageLog) {
                System.out.println(log);
            }
        }
    }
}