package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.combat;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

public interface ICombatEngine {
    CombatResult executeAttack(Character attacker, Character target, boolean isEnemy);
}
