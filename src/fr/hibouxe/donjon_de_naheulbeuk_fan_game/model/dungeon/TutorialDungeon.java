package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Goblin;
import java.util.ArrayList;
import java.util.List;

public class TutorialDungeon extends Dungeon {

    public TutorialDungeon() {
        super(3, 1);
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team) {
        switch (floorNumber) {
            case 1:
                return prepareFloor1(team);
            case 2:
                return prepareFloor2(team);
            default:
                return false;
        }
    }

    private boolean prepareFloor1(Team team) {
        // Taille pour un petit couloir en "U"
        this.setWidth(5);
        this.setHeight(5);
        this.setGrid(new Cell[5][5]);

        // Initialiser tout en mur
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setRoomId(1); // On met dans une même "salle" pour le brouillard de guerre
            }
        }

        // Creuser le couloir en forme de U
        // Ligne du haut (Est)
        this.getGrid()[1][1].setWall(false);
        this.getGrid()[2][1].setWall(false);
        this.getGrid()[3][1].setWall(false);

        // Descente (Sud)
        this.getGrid()[3][2].setWall(false);

        // Ligne du bas (Ouest)
        this.getGrid()[3][3].setWall(false);
        this.getGrid()[2][3].setWall(false);
        this.getGrid()[1][3].setWall(false);

        // Retrait des murs séparateurs
        this.getGrid()[1][1].removeWallBetween(this.getGrid()[2][1]);
        this.getGrid()[2][1].removeWallBetween(this.getGrid()[3][1]);
        this.getGrid()[3][1].removeWallBetween(this.getGrid()[3][2]);
        this.getGrid()[3][2].removeWallBetween(this.getGrid()[3][3]);
        this.getGrid()[3][3].removeWallBetween(this.getGrid()[2][3]);
        this.getGrid()[2][3].removeWallBetween(this.getGrid()[1][3]);

        // 3. Placer l'équipe au début
        team.setX(1);
        team.setY(1);
        
        // Donner la Potion
        if (team.getInventory().isEmpty()) {
            team.getInventory().add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion("Potion de Soin", "Restaure 50 PV", 50));
        }

        // 4. Ajouter l'escalier
        this.getGrid()[1][3].setStairs(true);

        return false;
    }

    private boolean prepareFloor2(Team team) {
        this.setWidth(3);
        this.setHeight(5);
        this.setGrid(new Cell[3][5]);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setRoomId(2); 
            }
        }
        
        // Ligne droite sur X=1
        for (int y = 0; y < 5; y++) {
            this.getGrid()[1][y].setWall(false);
            if (y < 4) {
                this.getGrid()[1][y].removeWallBetween(this.getGrid()[1][y+1]);
            }
        }

        // Joueur commence en bas
        team.setX(1);
        team.setY(4);

        // Escalier tout en haut
        this.getGrid()[1][0].setStairs(true);
        
        return false;
    }

    @Override
    public java.util.List<String> getIntroDialogues(int floorNumber) {
        java.util.List<String> dialogues = new java.util.ArrayList<>();
        switch (floorNumber) {
            case 1:
                dialogues.add("\n=== CHAPITRE 1 : LA FUITE ===");
                dialogues.add("Ranger : Aïe... un mal de crâne effroyable...");
                dialogues.add("Ranger : La taverne a été attaquée. Je dois fuir par le cellier et retrouver les autres !");
                dialogues.add("[UI Tuto] : Utilisez Z, Q, S, D (ou les flèches) pour vous déplacer dans le couloir.");
                dialogues.add("[UI Tuto] : Atteignez l'escalier au bout du chemin pour avancer.");
                break;
            case 2:
                dialogues.add("\n=== CHAPITRE 2 : L'ELFE ET L'INVENTAIRE ===");
                dialogues.add("Ranger : C'est l'Elfe de notre groupe ! Elle est par terre...");
                dialogues.add("Ranger : Elle a dû se prendre un tonneau sur la tête.");
                break;
        }
        return dialogues;
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        return false;
    }
}
