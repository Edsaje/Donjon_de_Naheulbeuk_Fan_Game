package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveData;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.save.SaveManager;

/**
 * Super Contrôleur Orchestrateur.
 * Gère la machine à états de l'application (Écran-titre -> Détection QuickSave -> Menu Principal -> Tutoriel / Hub <-> Donjon).
 * Garantit le retour immédiat à l'écran-titre lors d'une QuickSave.
 * Respecte à 100% l'architecture MVC et SOLID.
 *
 * @author Hibouxe
 * @version 3.1
 */
public class Game {
    private Menu menu;
    private Team team;

    public Game(Menu menu) {
        this.menu = menu;
    }

    /**
     * Lance le cycle de vie principal du jeu.
     */
    public void startGame() {
        boolean applicationRunning = true;

        while (applicationRunning) {
            // 1. Écran initial : "Donjon De Naheulbeuk Fan Game" - Demande d'appuyer sur Entrée
            menu.displayTitleScreen();

            // 2. Détection immédiate de la Sauvegarde Rapide après l'appui sur Entrée !
            boolean resumedFromQuickSave = false;
            if (SaveManager.hasQuickSave()) {
                boolean loadQuick = menu.askLoadQuickSavePrompt();
                boolean shouldLoad = loadQuick;

                if (!loadQuick) {
                    // Demande de confirmation avec message d'avertissement !
                    boolean confirmAbandon = menu.askConfirmAbandonQuickSave();
                    if (confirmAbandon) {
                        SaveManager.deleteQuickSave();
                        menu.displayMessage("\n[Information] La Sauvegarde Rapide a été supprimée.");
                        shouldLoad = false;
                    } else {
                        shouldLoad = true; // L'utilisateur annule et reprend sa partie !
                    }
                }

                if (shouldLoad) {
                    SaveData saveData = SaveManager.loadQuickSave();
                    if (saveData != null && saveData.getTeam() != null && saveData.getDungeon() != null) {
                        this.team = saveData.getTeam();
                        menu.displayMessage("\n[Chargement] Reprise de l'exploration à l'Étage " + saveData.getCurrentFloor() + " !");
                        menu.displayMessage("[Rappel] Votre Sauvegarde Rapide a été chargée. N'oubliez pas d'en refaire une (Touche K) si vous souhaitez sauvegarder à nouveau avant de quitter le donjon !");

                        // Suppression immédiate du fichier quicksave.sav après chargement (consommation de la sauvegarde temporaire)
                        SaveManager.deleteQuickSave();

                        ExplorationController explo = new ExplorationController(saveData.getDungeon(), this.team, menu, false);
                        explo.start();

                        resumedFromQuickSave = true;
                    }
                }
            }

            // 3. Si aucune reprise de QuickSave n'a eu lieu (ou si déclinée), afficher le MENU PRINCIPAL !
            if (!resumedFromQuickSave) {
                boolean inMainMenu = true;

                while (inMainMenu && applicationRunning) {
                    int choice = menu.askMainMenuChoice();

                    switch (choice) {
                        case 1: // Nouvelle Partie
                            runNewGame();
                            // Si l'utilisateur a fait une QuickSave en cours de route, quitter le menu principal pour retourner à l'écran-titre !
                            if (SaveManager.hasQuickSave()) {
                                inMainMenu = false;
                            }
                            break;
                        case 2: // Charger Partie
                            loadHubSaveGame();
                            if (SaveManager.hasQuickSave()) {
                                inMainMenu = false;
                            }
                            break;
                        case 3: // Quitter
                            menu.displayMessage("\nMerci d'avoir joué au Donjon de Naheulbeuk ! Tchoss !");
                            inMainMenu = false;
                            applicationRunning = false;
                            break;
                    }
                }
            }
        }
    }

    private void runNewGame() {
        // Phase 1 : Tutoriel scripté (Ranger seul)
        runTutorial();

        // Ne poursuivre vers le Hub que si aucune QuickSave n'a été effectuée pendant le tutoriel
        if (!SaveManager.hasQuickSave()) {
            runHubLoop();
        }
    }

    private void loadHubSaveGame() {
        if (SaveManager.hasHubSave()) {
            SaveData saveData = SaveManager.loadHubSave();
            if (saveData != null && saveData.getTeam() != null) {
                this.team = saveData.getTeam();
                menu.displayMessage("\n[Chargement] Vous retrouvez votre Compagnie au Campement !");
                runHubLoop();
                return;
            }
        }
        menu.displayMessage("\n[Information] Aucune sauvegarde de campement (savegame.sav) trouvée.");
    }

    private void runHubLoop() {
        boolean playing = true;
        while (playing && !SaveManager.hasQuickSave()) {
            Hub hub = new Hub(team, menu);
            boolean goDungeon = hub.enter();

            if (goDungeon) {
                runNaheulbeuk();
                // Si la quicksave a été effectuée durant le donjon, interrompre immédiatement la boucle du Hub !
                if (SaveManager.hasQuickSave()) {
                    playing = false;
                }
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
        this.team.getMembers().clear();
        this.team.getMembers().add(new Ranger());

        // Labyrinthe scripté 3x1
        TutorialDungeon tutorialMaze = new TutorialDungeon();
        tutorialMaze.generate();

        // Lancement de l'exploration en mode Tutoriel
        ExplorationController explo = new ExplorationController(tutorialMaze, team, menu, true);
        explo.start();

        // Si l'exploration s'est arrêtée suite à une Sauvegarde Rapide, interrompre le tutoriel immédiatement !
        if (SaveManager.hasQuickSave()) {
            return;
        }

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
