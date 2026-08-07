package fr.hibouxe.donjon_de_naheulbeuk_fan_game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.Game;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleMenu;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.GraphicHD2DView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.*;

/**
 * Point d'entrée principal de l'application Donjon de Naheulbeuk Fan Game.
 * Instancie la Vue principale (menu), l'injecte dans le contrôleur Game et lance la partie.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        //IGameView view = new ConsoleMenu(); // Mode Console
        IGameView view = new GraphicHD2DView(); //HD-2D

        view.displayMessage("=== Bienvenue dans le Donjon de Naheulbeuk ===");
        Game game = new Game(view);
        game.startGame();
    }
}
