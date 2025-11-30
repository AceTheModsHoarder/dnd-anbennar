package dnd.skills;

import dnd.characters.Player;
import dnd.characters.Monster;
import java.util.Random;

public class BasicAttack extends Skill {
    private Random random;
    
    public BasicAttack() {
        super("Power Attack", "A stronger than normal physical attack", 2);
        this.random = new Random();
    }
    
    @Override
    public void use(Player caster, Monster target) {
        int damage = caster.getAttack() + random.nextInt(4) + 1; // 1-4 bonus damage
        target.takeDamage(damage);
        System.out.println("You use Power Attack dealing " + damage + " damage to " + target.getName() + "!");
    }
}