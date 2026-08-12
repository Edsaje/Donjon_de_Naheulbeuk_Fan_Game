package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.SaveData;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.*;

/**
 * Super Contrôleur Orchestrateur.
 * Gère la machine à états de l'application (écran-titre -> QuickSave -> ConsoleMenu Principal -> Multi-Slots -> Tutoriel / HubController <-> Donjon).
 * Garantit l'indépendance totale du modèle (Agnostique) et le support du système Multi-Slots (1, 2, 3).
 *
 * @author Hibouxe
 * @version 4.0
 */
public class Game {
    private IGameView menu;
    private Team team;
    private int currentSlot = 1; // Slot actif par défaut (1, 2 ou 3)
    private ISaveManager saveManager;

    public Game(IGameView menu, ISaveManager saveManager) {
        this.menu = menu;
        this.saveManager = saveManager;
    }

    /**
     * Lance le cycle de vie principal du jeu.
     */
    public void startGame() {
        boolean applicationRunning = true;

        while (applicationRunning) {
            // 1. écran initial : "Donjon De Naheulbeuk Fan Game" - Demande d'appuyer sur Entrée
            menu.displayTitleScreen();

            // 2. Détection immédiate d'une Sauvegarde Rapide sur l'un des slots (1, 2 ou 3)
            int quickSlot = getFirstQuickSaveSlot();
            boolean resumedFromQuickSave = false;

            if (quickSlot != -1) {
                boolean loadQuick = menu.askLoadQuickSavePrompt(quickSlot, saveManager.getSlotSummary(quickSlot));
                boolean shouldLoad = loadQuick;

                if (!loadQuick) {
                    boolean confirmAbandon = menu.askConfirmAbandonQuickSave();
                    if (confirmAbandon) {
                        saveManager.deleteQuickSave(quickSlot);
                        menu.displayMessage("\n[Information] La Sauvegarde Rapide du Slot " + quickSlot + " a été supprimée.");
                        shouldLoad = false;
                    } else {
                        shouldLoad = true;
                    }
                }

                if (shouldLoad) {
                    this.currentSlot = quickSlot;
                    SaveData saveData = saveManager.loadQuickSave(currentSlot);
                    if (saveData != null && saveData.getTeam() != null && saveData.getDungeon() != null) {
                        this.team = saveData.getTeam();
                        menu.displayMessage("\n[Chargement] Reprise de l'exploration à l'étage " + saveData.getCurrentFloor() + " (Slot " + currentSlot + ") !");
                        menu.displayMessage("[Rappel] N'oubliez pas d'effectuer une nouvelle Sauvegarde Rapide (Touche K) avant de quitter !");

                        // Suppression de la quicksave chargée (consommation unique)
                        saveManager.deleteQuickSave(currentSlot);

                        ExplorationController explo = new ExplorationController(saveData.getDungeon(), this.team, menu, menu, menu, false, currentSlot, saveManager);
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
                            if (saveManager.hasQuickSave(currentSlot)) {
                                inMainMenu = false;
                            }
                            break;
                        case 2: // Charger Partie
                            handleLoadGameChoice();
                            if (saveManager.hasQuickSave(currentSlot)) {
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

        if (saveManager.hasAnySave(slot)) {
            menu.displayMessage("\n[Avertissement] L'emplacement " + slot + " contient déjà des données !");
            menu.displayMessage("1. Écraser et recommencer une Nouvelle Partie");
            menu.displayMessage("2. Annuler");
            int confirm = menu.askPlayerInt();
            if (confirm != 1) return;
            saveManager.deleteSlot(slot);
        }

        this.currentSlot = slot;
        runNewGame();
    }

    private void handleLoadGameChoice() {
        String[] summaries = getSlotSummaries();
        int slot = menu.askSlotChoice("CHARGER UNE PARTIE - SÉLECTIONNER UN EMPLACEMENT", summaries);
        if (slot == 0) return;

        if (!saveManager.hasAnySave(slot)) {
            menu.displayMessage("\n[Information] L'emplacement " + slot + " est vide.");
            return;
        }

        this.currentSlot = slot;

        if (saveManager.hasQuickSave(slot)) {
            SaveData data = saveManager.loadQuickSave(slot);
            if (data != null && data.getTeam() != null && data.getDungeon() != null) {
                this.team = data.getTeam();
                menu.displayMessage("\n[Chargement] Reprise de l'exploration à l'étage " + data.getCurrentFloor() + " (Slot " + slot + ") !");
                saveManager.deleteQuickSave(slot);
                ExplorationController explo = new ExplorationController(data.getDungeon(), this.team, menu, menu, menu, false, slot, saveManager);
                explo.setCurrentFloor(data.getCurrentFloor());
                explo.start();
                return;
            }
        }

        if (saveManager.hasHubSave(slot)) {
            SaveData data = saveManager.loadHubSave(slot);
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
                int src = menu.askSlotChoice("SÉLECTIONNER LE SLOT à COPIER", summaries);
                if (src != 0 && saveManager.hasAnySave(src)) {
                    int dst = menu.askTargetCopySlot(src, summaries);
                    if (dst != 0) {
                        boolean copied = saveManager.copySlot(src, dst);
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
                int delSlot = menu.askSlotChoice("SÉLECTIONNER LE SLOT à SUPPRIMER", summaries);
                if (delSlot != 0 && saveManager.hasAnySave(delSlot)) {
                    menu.displayMessage("\n[Confirmation] Supprimer définitivement l'emplacement " + delSlot + " ? (1. Oui / 2. Annuler)");
                    int confirm = menu.askPlayerInt();
                    if (confirm == 1) {
                        saveManager.deleteSlot(delSlot);
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
        if (!saveManager.hasQuickSave(currentSlot)) {
            runHubLoop();
        }
    }

    private void runHubLoop() {
        boolean playing = true;
        while (playing && !saveManager.hasQuickSave(currentSlot)) {
            HubController hubController = new HubController(team, menu, currentSlot, saveManager);
            boolean goDungeon = hubController.enter();

            if (goDungeon) {
                runNaheulbeuk();
                if (saveManager.hasQuickSave(currentSlot)) {
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

        ExplorationController explo = new ExplorationController(tutorialMaze, team, menu, menu, menu, true, currentSlot, saveManager);
        explo.start();

        if (saveManager.hasQuickSave(currentSlot)) {
            return;
        }

        menu.displayMessage("\nLa compagnie, enfin réunie au complet, trouve la sortie et fuit vers la forêt !");
    }

    private void runNaheulbeuk() {
        menu.displayMessage("\nVous pénétrez dans les sombres couloirs du Donjon de Naheulbeuk...");

        NaheulbeukDungeon naheulbeukMaze = new NaheulbeukDungeon();
        naheulbeukMaze.prepareFloor(1, team);

        ExplorationController explo = new ExplorationController(naheulbeukMaze, team, menu, menu, menu, false, currentSlot, saveManager);
        explo.start();
    }

    private int getFirstQuickSaveSlot() {
        for (int slot = 1; slot <= 3; slot++) {
            if (saveManager.hasQuickSave(slot)) {
                return slot;
            }
        }
        return -1;
    }

    private String[] getSlotSummaries() {
        String[] summaries = new String[3];
        for (int i = 0; i < 3; i++) {
            summaries[i] = saveManager.getSlotSummary(i + 1);
        }
        return summaries;
    }
}

