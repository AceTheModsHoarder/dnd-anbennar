package dnd.skills;

import dnd.characters.Player;
import dnd.characters.Monster;
import java.util.Random;

public class Fireball extends Skill {
    private Random random;
    
    public Fireball() {
        super("Fireball", "Launch a fiery projectile at your enemy", 5);
        this.random = new Random();
    }
    
    @Override
    public void use(Player caster, Monster target) {
        int damage = 8 + random.nextInt(5); // 8-12 damage
        target.takeDamage(damage);
        System.out.println("You cast Fireball dealing " + damage + " damage to " + target.getName() + "!");
    }
}