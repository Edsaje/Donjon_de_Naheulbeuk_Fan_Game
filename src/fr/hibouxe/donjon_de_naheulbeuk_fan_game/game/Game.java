package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses.*;

/**
 * Super Contrôleur Orchestrateur.
 * Gère la machine à états de l'application (Tutoriel -> Hub <-> Donjon)
 */
public class Game {
    private Menu menu;
    private Team team;

    public Game(Menu menu) {
        this.menu = menu;
    }

    public void startGame() {
        // Phase 1 : Tutoriel scripté (Ranger seul)
        runTutorial();

        // Phase 2 : Boucle principale
        boolean playing = true;
        while (playing) {
            Hub hub = new Hub(team, menu);
            boolean goDungeon = hub.enter();

            if (goDungeon) {
                runNaheulbeuk();
            } else {
                playing = false;
            }
        }
    }

    private void runTutorial() {
        menu.displayMessage("\n=== CHAPITRE 1 : LA FUITE ===");
        menu.displayMessage("Le Ranger se réveille avec un mal de crâne effroyable...");
        menu.displayMessage("La taverne a été attaquée. Il doit fuir par le cellier et retrouver les autres !");

        // Équipe composée uniquement du Ranger
        this.team = new Team();
        this.team.getMembers().clear(); // On vide l'équipe de départ
        this.team.getMembers().add(new Ranger());

        // Labyrinthe scripté 3x1
        TutorialDungeon tutorialMaze = new TutorialDungeon();
        tutorialMaze.generate();

        // Lancement de l'exploration en mode Tutoriel
        ExplorationController explo = new ExplorationController(tutorialMaze, team, menu, true);
        explo.start();

        menu.displayMessage("\nVous trouvez la sortie et fuyez vers la forêt !");
        
        // Après le tuto, les autres héros rejoignent la compagnie pour le Hub !
        team.getMembers().add(new Dwarf());
        team.getMembers().add(new Elf());
        team.getMembers().add(new Barbarian());
        team.getMembers().add(new Magician());
        team.getMembers().add(new Ogre());
        team.getMembers().add(new Thief());
    }

    private void runNaheulbeuk() {
        menu.displayMessage("\nVous pénétrez dans les sombres couloirs du Donjon de Naheulbeuk...");
        this.team.setX(0);
        this.team.setY(0);

        NaheulbeukDungeon naheulbeukMaze = new NaheulbeukDungeon();
        naheulbeukMaze.generate();

        // Lancement de l'exploration classique (isTutorial = false)
        ExplorationController explo = new ExplorationController(naheulbeukMaze, team, menu, false);
        explo.start();
    }
}
