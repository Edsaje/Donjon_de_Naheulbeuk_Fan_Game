import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Game;

/**
 * Point d'entrée principal de l'application Donjon de Naheulbeuk Fan Game.
 * Instancie le contrôleur Game et lance la partie.
 *
 * @author Hibouxe
 * @version 1.0
 */
void main() {
    System.out.println("=== Bienvenue dans le Donjon de Naheulbeuk ===");
    Game game = new Game();
    game.startGame();
}
