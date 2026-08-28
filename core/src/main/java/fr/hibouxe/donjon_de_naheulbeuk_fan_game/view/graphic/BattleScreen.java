package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;


import com.badlogic.gdx.ScreenAdapter;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.BattleController;

public class BattleScreen extends ScreenAdapter {
    private BattleController controller;

    public BattleScreen(BattleController controller) {
        this.controller = controller;
    }

    @Override
    public void render(float delta) {
        if (controller != null) {
            // controller.update(delta);
        }
    }
}