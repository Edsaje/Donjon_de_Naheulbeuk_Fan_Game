package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

/**
 * Contrôleur gérant la phase de repos au campement.
 * Respecte le SRP : ne fait rien d'autre que gérer le ConsoleMenu entre les donjons.
 */
public class HubController implements GameState {
    private Team team;
    private IMenuView menu;
    private int activeSlot = 1;
    private ISaveManager saveManager;

    public HubController(Team team, IMenuView menu, ISaveManager saveManager) {
        this(team, menu, 1, saveManager);
    }

    public HubController(Team team, IMenuView menu, int activeSlot, ISaveManager saveManager) {
        this.team = team;
        this.menu = menu;
        this.activeSlot = activeSlot;
        this.saveManager = saveManager;
    }

    @Override
    public void enter() {
        menu.displayMessage("\n=== LE CAMPEMENT (HubController) ===");
        menu.displayMessage("La compagnie se repose autour d'un feu de camp mal allumé par l'Elfe...");
        menu.setMenuRequest("Le Campement", new String[]{"Aller au Donjon", "Statistiques", "Sauvegarder", "Quitter"});
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void onInput(String action) {
        if ("ENTER".equals(action)) {
            int choice = menu.getMenuSelection();
            menu.resetMenuSelection();
            switch (choice) {
                case 0:
                    menu.displayMessage("Vous rangez vos affaires et vous dirigez vers l'entrée du gouffre...");
                    // Transition non implémentée (nécessite GameContext)
                    break;
                case 1:
                    menu.displayDialogue("\n[Le menu Statistiques détaillé sera disponible dans une prochaine version !]");
                    menu.setMenuRequest("Le Campement", new String[]{"Aller au Donjon", "Statistiques", "Sauvegarder", "Quitter"});
                    break;
                case 2:
                    boolean saved = saveManager.saveHubSave(activeSlot, team, 1);
                    if (saved) {
                        menu.displayMessage("\n[Sauvegarde] Progression enregistrée avec succès dans le Slot " + activeSlot + " !");
                    } else {
                        menu.displayMessage("\n[Erreur] Échec de la sauvegarde.");
                    }
                    menu.setMenuRequest("Le Campement", new String[]{"Aller au Donjon", "Statistiques", "Sauvegarder", "Quitter"});
                    break;
                case 3:
                    menu.displayMessage("Fin de l'aventure ! Le Nain pleure car il n'a pas eu son or.");
                    com.badlogic.gdx.Gdx.app.exit();
                    break;
            }
        }
    }

    @Override
    public void exit() {}
}