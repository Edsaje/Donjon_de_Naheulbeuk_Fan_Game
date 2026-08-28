package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.combat;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup;

public class OrcBehavior implements IRoamingBehavior {
    @Override
    public void onAlert(RoamingMonsterGroup mg) {
        mg.setState(RoamingMonsterGroup.AIState.CHARGE);
        mg.setStateTimer(1.5f);
    }
    @Override
    public float getChaseSpeed() { return 1.3f; }
}