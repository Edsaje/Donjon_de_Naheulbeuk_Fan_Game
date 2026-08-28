package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup;

public interface IRoamingBehavior {
    void onAlert(RoamingMonsterGroup mg);
    float getChaseSpeed();
}