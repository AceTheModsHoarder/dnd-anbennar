package dnd.combat;

public class CombatResult {  // Add 'public' here
    private boolean victory;
    private int expGained;
    private int goldGained;
    
    // Getters and setters
    public boolean isVictory() { return victory; }
    public void setVictory(boolean victory) { this.victory = victory; }
    public int getExpGained() { return expGained; }
    public void setExpGained(int expGained) { this.expGained = expGained; }
    public int getGoldGained() { return goldGained; }
    public void setGoldGained(int goldGained) { this.goldGained = goldGained; }
}