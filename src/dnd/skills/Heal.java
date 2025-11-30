package dnd.skills;

import dnd.characters.Player;
import dnd.characters.Monster;

public class Heal extends Skill {
    public Heal() {
        super("Heal", "Restore your health", 4);
    }
    
    @Override
    public void use(Player caster, Monster target) {
        int healAmount = 10;
        caster.heal(healAmount);
        System.out.println("You heal yourself for " + healAmount + " HP!");
    }
}