import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Game;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

/**
 * Point d'entrée principal de l'application Donjon de Naheulbeuk Fan Game.
 * Instancie la Vue principale (Menu), l'injecte dans le contrôleur Game et lance la partie.
 *
 * @author Hibouxe
 * @version 1.0
 */
void main() {
    Menu menu = new Menu();
    menu.displayMessage("=== Bienvenue dans le Donjon de Naheulbeuk ===");
    Game game = new Game(menu);
    game.startGame();
}
