package dnd.skills;

import dnd.characters.Monster;
import dnd.characters.Player;

public abstract class Skill {
    protected String name;
    protected String description;
    protected int manaCost;
    
    public Skill(String name, String description, int manaCost) {
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
    }
    
    public abstract void use(Player caster, Monster target);
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getManaCost() { return manaCost; }
}