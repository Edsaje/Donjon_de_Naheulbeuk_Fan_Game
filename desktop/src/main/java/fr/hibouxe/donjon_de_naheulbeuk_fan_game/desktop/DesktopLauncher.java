package fr.hibouxe.donjon_de_naheulbeuk_fan_game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.input.InputManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.Game;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.FileSaveManager;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Donjon de Naheulbeuk - HD-2D Edition");
        config.setWindowedMode(1280, 720);
        config.setResizable(true);
        config.useVsync(true);
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
        config.setForegroundFPS(60);

        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.ISettingsRepository repo = 
            new fr.hibouxe.donjon_de_naheulbeuk_fan_game.desktop.LibGDXSettingsRepository("NaheulbeukFanGameSettings");
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager settingsManager = 
            new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager(repo);

        HD2DGameApp app = new HD2DGameApp(settingsManager);
        
        InputManager inputManager = new InputManager();
        FileSaveManager saveManager = new FileSaveManager();
        Game game = new Game(app, saveManager, inputManager);
        inputManager.setListener(game);
        app.setDependencies(inputManager, game);

        new Lwjgl3Application(app, config);
    }
}
