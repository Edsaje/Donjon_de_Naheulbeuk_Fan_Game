package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Lanceur de la fenêtre graphique HD-2D OpenGL (LWJGL 3).
 * Ouvre une fenêtre de jeu de résolution 1280x720 cadencée à 60 FPS avec anti-aliasing.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class HD2DLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Donjon de Naheulbeuk - HD-2D Engine (OpenGL 60 FPS)");
        config.setWindowedMode(1280, 720);
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4); // Anti-Aliasing MSAA x4

        new Lwjgl3Application(new HD2DGameApp(), config);
    }
}
