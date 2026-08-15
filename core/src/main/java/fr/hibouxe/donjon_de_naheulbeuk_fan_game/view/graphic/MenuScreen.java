package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import com.badlogic.gdx.ScreenAdapter;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.Game;

public class MenuScreen extends ScreenAdapter {
    private Game controller;

    public MenuScreen(Game controller) {
        this.controller = controller;
    }

    @Override
    public void render(float delta) {
        if (controller != null) {
            // controller.update(delta);
        }
    }
}

