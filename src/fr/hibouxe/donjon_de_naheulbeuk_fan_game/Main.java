import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Maze;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

void main() {
    System.out.println("=== Génération du Donjon ===");

    //créer un labyrinthe
    Maze maze = new Maze(20,20); //choisir taille
    //générer les couloirs
    maze.generateMaze();

    //creuser une salle (nombre, taillemin, taillemax)
    maze.generateRandomRooms(6,2,4);

    //afficher dans la console
    Menu menu = new Menu();
    menu.display(maze);
}
