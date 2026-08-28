import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup;
package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;

public interface IRoamingBehavior {
    void onAlert(RoamingMonsterGroup mg);
    float getChaseSpeed();
}