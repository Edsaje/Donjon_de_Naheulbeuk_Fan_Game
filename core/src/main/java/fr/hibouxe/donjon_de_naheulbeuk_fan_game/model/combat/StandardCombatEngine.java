package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

public class StandardCombatEngine implements ICombatEngine {
    @Override
    public CombatResult executeAttack(Character attacker, Character target, boolean isEnemy) {
        int damage;
        if (isEnemy) {
            damage = Math.max(1, attacker.getAttack() - target.getSpeed() / 2);
        } else {
            damage = Math.max(1, attacker.getAttack());
        }
        target.setHealthPoint(target.getHealthPoint() - damage);
        
        return new CombatResult(damage, target.getHealthPoint() <= 0);
    }
}
