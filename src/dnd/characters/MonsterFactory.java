package dnd.characters;

public class MonsterFactory {
    
    // Tier 1 Monsters (Easy - Level 1-3)
    public static Monster createGoblin() {
        return new Monster("Goblin", 15, 4, 0, 0);
    }
    
    public static Monster createRat() {
        return new Monster("Giant Rat", 8, 3, 5, 0);
    }

    public static Monster createSpider() {
        return new Monster("Giant Spider", 10, 4, 6, 1);
    }

    public static Monster createKobold() {
        return new Monster("Kobold", 12, 3, 8, 2);
    }

    public static Monster createBandit() {
        return new Monster("Bandit", 18, 5, 15, 3);
    }

    public static Monster createWolf() {
        return new Monster("Wolf", 12, 6, 5, 8);
    }

    // Tier 2 Monsters (Medium - Level 4-6)
    public static Monster createOrc() {
        return new Monster("Orc", 25, 6, 10, 2);
    }

    public static Monster createSkeleton() {
        return new Monster("Skeleton", 20, 5, 30, 0);
    }

    public static Monster createZombie() {
        return new Monster("Zombie", 30, 5, 10, 8);
    }

    public static Monster createOgre() {
        return new Monster("Ogre", 35, 7, 12, 8);
    }

    public static Monster createTroll() {
        return new Monster("Cave Troll", 40, 8, 10, 8);
    }

    public static Monster createHarpy() {
        return new Monster("Harpy", 22, 6, 8, 3);
    }

    // Tier 3 Monsters (Hard - Level 7-9)
    public static Monster createDragon() {
        return new Monster("Dragon", 50, 10, 40, 10);
    }

    public static Monster createMinotaur() {
        return new Monster("Minotaur", 45, 9, 25, 12);
    }

    public static Monster createElemental() {
        return new Monster("Fire Elemental", 28, 9, 10, 0);
    }

    public static Monster createWraith() {
        return new Monster("Wraith", 25, 6, 8, 15);
    }

    public static Monster createGiant() {
        return new Monster("Hill Giant", 70, 10, 20, 12);
    }

    public static Monster createBasilisk() {
        return new Monster("Basilisk", 35, 7, 18, 10);
    }

    // Tier 4 Monsters (Boss - Level 10+)
    public static Monster createAncientDragon() {
        return new Monster("Ancient Dragon", 100, 15, 50, 30);
    }

    public static Monster createLich() {
        return new Monster("Lich King", 60, 12, 35, 25);
    }

    public static Monster createDemonLord() {
        return new Monster("Demon Lord", 80, 12, 40, 25);
    }

    public static Monster createBeholder() {
        return new Monster("Beholder", 65, 10, 35, 20);
    }

    public static Monster createHydra() {
        return new Monster("Hydra", 90, 11, 30, 15);
    }

    // Special/Unique Monsters
    public static Monster createVampire() {
        return new Monster("Vampire", 40, 8, 20, 15);
    }

    public static Monster createMummy() {
        return new Monster("Ancient Mummy", 32, 6, 25, 20);
    }

    public static Monster createDoppelganger() {
        return new Monster("Doppelganger", 25, 7, 15, 5);
    }

    public static Monster createTreant() {
        return new Monster("Treant", 55, 8, 30, 18);
    }

    public static Monster createGorgon() {
        return new Monster("Gorgon", 38, 9, 28, 12);
    }
    
    // ========== FRIEND-FRIENDLY TEMPLATE SECTION ==========
    // Your friends can copy-paste from here and just change the values
    
    /*
    TEMPLATE - Copy this method and change the values:
    
    public static Monster createYourMonster() {
        return new Monster(
            "NAME_HERE",      // Monster name
            HP_HERE,          // Health points (10-100)
            ATTACK_HERE,      // Attack power (3-15)
            ARMOR_HERE,       // Armor value (0-50)
            RESISTANCE_HERE   // Damage resistance (0-30)
        );
    }
    
    BALANCING GUIDE:
    - Easy monster:      HP: 10-20,  Attack: 3-5,  Armor: 0-10,  Resistance: 0-5
    - Medium monster:    HP: 20-40,  Attack: 5-8,  Armor: 10-20, Resistance: 5-10
    - Hard monster:      HP: 40-60,  Attack: 8-12, Armor: 20-35, Resistance: 10-20
    - Boss monster:      HP: 60-100, Attack: 12-15, Armor: 35-50, Resistance: 20-30
    */
    
    // EXAMPLE - Your friend can copy this and modify:
    public static Monster createExampleMonster() {
        return new Monster(
            "EXAMPLE",  // Change name
            50,         // Change HP
            8,          // Change Attack
            20,         // Change Armor
            10          // Change Resistance
        );
    }
}