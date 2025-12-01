package dnd.skills;

import dnd.characters.Player;
import dnd.characters.Monster;

public class ShatterArmor extends Skill {
    private int resistanceReduction;
    
    public ShatterArmor() {
        super("Shatter Armor", "Reduces enemy damage resistance", 3);
        this.resistanceReduction = 5;
    }
    
    @Override
    public void use(Player caster, Monster target) {
        caster.applyResistanceReduction(target, resistanceReduction);
        System.out.println(target.getName() + "'s defenses are shattered!");
    }
}