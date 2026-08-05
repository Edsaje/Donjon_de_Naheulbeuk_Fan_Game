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
        // 1. Vérification Sauvegarde Rapide (QuickSave)
        if (fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveManager.hasQuickSave()) {
            menu.displayMessage("\n=== SAUVEGARDE RAPIDE DÉTECTÉE ===");
            menu.displayMessage("Une exploration en cours dans le Donjon a été trouvée !");
            menu.displayMessage("1. Reprendre l'exploration là où vous vous étiez arrêté");
            menu.displayMessage("2. Nouvelle Partie / Repartir du Campement");
            int choice = menu.askPlayerInt();
            if (choice == 1) {
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveData saveData = fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveManager.loadQuickSave();
                if (saveData != null && saveData.getTeam() != null && saveData.getDungeon() != null) {
                    this.team = saveData.getTeam();
                    menu.displayMessage("\n[Chargement] Reprise de l'exploration à l'Étage " + saveData.getCurrentFloor() + " !");
                    ExplorationController explo = new ExplorationController(saveData.getDungeon(), this.team, menu, false);
                    explo.start();
                    // Une fois l'exploration terminée ou en défaite, supprimer la quicksave
                    fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveManager.deleteQuickSave();
                }
            }
        }

        // 2. Vérification Sauvegarde du Hub
        if (this.team == null && fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveManager.hasHubSave()) {
            menu.displayMessage("\n=== SAUVEGARDE DU CAMPEMENT DÉTECTÉE ===");
            menu.displayMessage("1. Charger la Compagnie du Campement");
            menu.displayMessage("2. Recommencer depuis le Tutoriel");
            int choice = menu.askPlayerInt();
            if (choice == 1) {
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveData saveData = fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveManager.loadHubSave();
                if (saveData != null && saveData.getTeam() != null) {
                    this.team = saveData.getTeam();
                    menu.displayMessage("\n[Chargement] Vous retrouvez votre Compagnie au Campement !");
                }
            }
        }

        // Phase 1 : Tutoriel scripté si aucune équipe n'a été chargée
        if (this.team == null) {
            runTutorial();
        }

        // Phase 2 : Boucle principale du Hub
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
