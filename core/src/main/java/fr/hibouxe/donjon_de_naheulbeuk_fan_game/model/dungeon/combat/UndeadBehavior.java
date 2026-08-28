package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup;

public class UndeadBehavior implements IRoamingBehavior {
    @Override
    public void onAlert(RoamingMonsterGroup mg) {
        mg.setState(RoamingMonsterGroup.AIState.CHASE);
    }
    @Override
    public float getChaseSpeed() { return 0.5f; }
}