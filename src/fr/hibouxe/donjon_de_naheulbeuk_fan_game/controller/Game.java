package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.SaveData;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.SaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.*;

/**
 * Super Contrôleur Orchestrateur.
 * Gère la machine à états de l'application (Écran-titre -> QuickSave -> ConsoleMenu Principal -> Multi-Slots -> Tutoriel / HubController <-> Donjon).
 * Garantit l'indépendance totale du modèle (Agnostique) et le support du système Multi-Slots (1, 2, 3).
 *
 * @author Hibouxe
 * @version 4.0
 */
public class Game {
    private IGameView menu;
    private Team team;
    private int currentSlot = 1; // Slot actif par défaut (1, 2 ou 3)

    public Game(IGameView menu) {
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

            // 2. Détection immédiate d'une Sauvegarde Rapide sur l'un des slots (1, 2 ou 3)
            int quickSlot = getFirstQuickSaveSlot();
            boolean resumedFromQuickSave = false;

            if (quickSlot != -1) {
                boolean loadQuick = menu.askLoadQuickSavePrompt(quickSlot, SaveManager.getSlotSummary(quickSlot));
                boolean shouldLoad = loadQuick;

                if (!loadQuick) {
                    boolean confirmAbandon = menu.askConfirmAbandonQuickSave();
                    if (confirmAbandon) {
                        SaveManager.deleteQuickSave(quickSlot);
                        menu.displayMessage("\n[Information] La Sauvegarde Rapide du Slot " + quickSlot + " a été supprimée.");
                        shouldLoad = false;
                    } else {
                        shouldLoad = true;
                    }
                }

                if (shouldLoad) {
                    this.currentSlot = quickSlot;
                    SaveData saveData = SaveManager.loadQuickSave(currentSlot);
                    if (saveData != null && saveData.getTeam() != null && saveData.getDungeon() != null) {
                        this.team = saveData.getTeam();
                        menu.displayMessage("\n[Chargement] Reprise de l'exploration à l'Étage " + saveData.getCurrentFloor() + " (Slot " + currentSlot + ") !");
                        menu.displayMessage("[Rappel] N'oubliez pas d'effectuer une nouvelle Sauvegarde Rapide (Touche K) avant de quitter !");

                        // Suppression de la quicksave chargée (consommation unique)
                        SaveManager.deleteQuickSave(currentSlot);

                        ExplorationController explo = new ExplorationController(saveData.getDungeon(), this.team, menu, false, currentSlot);
                        explo.setCurrentFloor(saveData.getCurrentFloor());
                        explo.start();

                        resumedFromQuickSave = true;
                    }
                }
            }

            // 3. Si aucune reprise de QuickSave n'a eu lieu, ouvrir le ConsoleMenu PRINCIPAL !
            if (!resumedFromQuickSave) {
                boolean inMainMenu = true;

                while (inMainMenu && applicationRunning) {
                    int choice = menu.askMainMenuChoice();

                    switch (choice) {
                        case 1: // Nouvelle Partie
                            handleNewGameChoice();
                            if (SaveManager.hasQuickSave(currentSlot)) {
                                inMainMenu = false;
                            }
                            break;
                        case 2: // Charger Partie
                            handleLoadGameChoice();
                            if (SaveManager.hasQuickSave(currentSlot)) {
                                inMainMenu = false;
                            }
                            break;
                        case 3: // Gérer les Emplacements de Sauvegarde
                            handleSlotManagementChoice();
                            break;
                        case 4: // Quitter
                            menu.displayMessage("\nMerci d'avoir joué au Donjon de Naheulbeuk ! Tchoss !");
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.exit(0);
                            break;
                    }
                }
            }
        }
    }

    private void handleNewGameChoice() {
        String[] summaries = getSlotSummaries();
        int slot = menu.askSlotChoice("NOUVELLE PARTIE - CHOISIR UN EMPLACEMENT", summaries);
        if (slot == 0) return;

        if (SaveManager.hasAnySave(slot)) {
            menu.displayMessage("\n[Avertissement] L'emplacement " + slot + " contient déjà des données !");
            menu.displayMessage("1. Écraser et recommencer une Nouvelle Partie");
            menu.displayMessage("2. Annuler");
            int confirm = menu.askPlayerInt();
            if (confirm != 1) return;
            SaveManager.deleteSlot(slot);
        }

        this.currentSlot = slot;
        runNewGame();
    }

    private void handleLoadGameChoice() {
        String[] summaries = getSlotSummaries();
        int slot = menu.askSlotChoice("CHARGER UNE PARTIE - SÉLECTIONNER UN EMPLACEMENT", summaries);
        if (slot == 0) return;

        if (!SaveManager.hasAnySave(slot)) {
            menu.displayMessage("\n[Information] L'emplacement " + slot + " est vide.");
            return;
        }

        this.currentSlot = slot;

        if (SaveManager.hasQuickSave(slot)) {
            SaveData data = SaveManager.loadQuickSave(slot);
            if (data != null && data.getTeam() != null && data.getDungeon() != null) {
                this.team = data.getTeam();
                menu.displayMessage("\n[Chargement] Reprise de l'exploration à l'Étage " + data.getCurrentFloor() + " (Slot " + slot + ") !");
                SaveManager.deleteQuickSave(slot);
                ExplorationController explo = new ExplorationController(data.getDungeon(), this.team, menu, false, slot);
                explo.setCurrentFloor(data.getCurrentFloor());
                explo.start();
                return;
            }
        }

        if (SaveManager.hasHubSave(slot)) {
            SaveData data = SaveManager.loadHubSave(slot);
            if (data != null && data.getTeam() != null) {
                this.team = data.getTeam();
                menu.displayMessage("\n[Chargement] Vous retrouvez votre Compagnie au Campement (Slot " + slot + ") !");
                runHubLoop();
            }
        }
    }

    private void handleSlotManagementChoice() {
        boolean managing = true;
        while (managing) {
            int action = menu.askSlotManagementAction();
            String[] summaries = getSlotSummaries();

            if (action == 1) { // Copier
                int src = menu.askSlotChoice("SÉLECTIONNER LE SLOT À COPIER", summaries);
                if (src != 0 && SaveManager.hasAnySave(src)) {
                    int dst = menu.askTargetCopySlot(src, summaries);
                    if (dst != 0) {
                        boolean copied = SaveManager.copySlot(src, dst);
                        if (copied) {
                            menu.displayMessage("\n[Succès] L'emplacement " + src + " a été copié vers l'emplacement " + dst + " !");
                        } else {
                            menu.displayMessage("\n[Erreur] Échec de la copie.");
                        }
                    }
                } else if (src != 0) {
                    menu.displayMessage("\n[Erreur] Cet emplacement est vide.");
                }
            } else if (action == 2) { // Supprimer
                int delSlot = menu.askSlotChoice("SÉLECTIONNER LE SLOT À SUPPRIMER", summaries);
                if (delSlot != 0 && SaveManager.hasAnySave(delSlot)) {
                    menu.displayMessage("\n[Confirmation] Supprimer définitivement l'emplacement " + delSlot + " ? (1. Oui / 2. Annuler)");
                    int confirm = menu.askPlayerInt();
                    if (confirm == 1) {
                        SaveManager.deleteSlot(delSlot);
                        menu.displayMessage("\n[Succès] L'emplacement " + delSlot + " a été supprimé.");
                    }
                } else if (delSlot != 0) {
                    menu.displayMessage("\n[Information] Cet emplacement est déjà vide.");
                }
            } else if (action == 0) {
                managing = false;
            }
        }
    }

    private void runNewGame() {
        runTutorial();
        if (!SaveManager.hasQuickSave(currentSlot)) {
            runHubLoop();
        }
    }

    private void runHubLoop() {
        boolean playing = true;
        while (playing && !SaveManager.hasQuickSave(currentSlot)) {
            HubController hubController = new HubController(team, menu, currentSlot);
            boolean goDungeon = hubController.enter();

            if (goDungeon) {
                runNaheulbeuk();
                if (SaveManager.hasQuickSave(currentSlot)) {
                    playing = false;
                }
            } else {
                playing = false;
            }
        }
    }

    private void runTutorial() {
        this.team = new Team();
        this.team.getMembers().clear();
        this.team.getMembers().add(new Ranger());

        TutorialDungeon tutorialMaze = new TutorialDungeon();
        tutorialMaze.prepareFloor(1, team);

        ExplorationController explo = new ExplorationController(tutorialMaze, team, menu, true, currentSlot);
        explo.start();

        if (SaveManager.hasQuickSave(currentSlot)) {
            return;
        }

        menu.displayMessage("\nLa compagnie, enfin réunie au complet, trouve la sortie et fuit vers la forêt !");
    }

    private void runNaheulbeuk() {
        menu.displayMessage("\nVous pénétrez dans les sombres couloirs du Donjon de Naheulbeuk...");

        NaheulbeukDungeon naheulbeukMaze = new NaheulbeukDungeon();
        naheulbeukMaze.prepareFloor(1, team);

        ExplorationController explo = new ExplorationController(naheulbeukMaze, team, menu, false, currentSlot);
        explo.start();
    }

    private int getFirstQuickSaveSlot() {
        for (int slot = 1; slot <= 3; slot++) {
            if (SaveManager.hasQuickSave(slot)) {
                return slot;
            }
        }
        return -1;
    }

    private String[] getSlotSummaries() {
        String[] summaries = new String[3];
        for (int i = 0; i < 3; i++) {
            summaries[i] = SaveManager.getSlotSummary(i + 1);
        }
        return summaries;
    }
}
