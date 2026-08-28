package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.DungeonGenerator.Room;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.ICellEvent;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult;
import java.util.ArrayList;

public class HubDungeon extends Dungeon {

    public HubDungeon() {
        super(30, 30);
        this.setDrawWalls(false);
    }

    @Override
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character getRandomMonster(int floorNumber, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider random, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.data.IMonsterRepository repository) {
        return null; // Pas de monstres dans le Hub
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        return false;
    }

    @Override
    public java.util.List<String> getFloorIntroDialogues(int floorNumber) {
        return new java.util.ArrayList<>();
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team, IMonsterRepository repository) {
        this.setWidth(30);
        this.setHeight(30);
        this.setGrid(new Cell[30][30]);

        // Initialisation avec des murs (foret dense autour)
        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setWall(true);
            }
        }

        // Zone centrale degagee
        for (int x = 5; x < 25; x++) {
            for (int y = 5; y < 25; y++) {
                this.getGrid()[x][y].setWall(false);
                this.getGrid()[x][y].setRoomId(1);
            }
        }
        
        this.setPrefabRooms(new ArrayList<>());

        // Feu de camp (Base) au centre [15][15]
        this.getGrid()[15][15].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("OPEN_TAVERN"); // Garder l'ancien nom de trigger pour le menu de repos
            return res;
        });

        // ======================================
        // SYSTEME DE CONSTRUCTION / UPGRADES
        // ======================================

        // 1. TAVERNE (En Haut - [15][20])
        int tavernLevel = team.getHubUpgradeLevel("TAVERNE");
        if (tavernLevel == 0) {
            this.getGrid()[15][20].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_TAVERNE");
                return res;
            });
        } else {
            // Ajouter le modele 3D de la Taverne
            Room tavernRoom = new Room(12, 18, 6, 6);
            tavernRoom.prefabModel = "assets/models/dungeon/naheulbeuk/rooms/test_room_5x5.obj"; // Placeholder
            this.getPrefabRooms().add(tavernRoom);
            
            this.getGrid()[15][20].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("ENTER_TAVERNE");
                return res;
            });
        }

        // 2. MARCHAND (A Gauche - [10][15])
        int merchantLevel = team.getHubUpgradeLevel("MARCHAND");
        if (merchantLevel == 0) {
            this.getGrid()[10][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_MARCHAND");
                return res;
            });
        } else {
            // Ajouter le modele 3D du Stand
            Room marchantRoom = new Room(8, 14, 4, 3);
            marchantRoom.prefabModel = "assets/models/dungeon/naheulbeuk/wall.obj"; // Placeholder
            this.getPrefabRooms().add(marchantRoom);
            
            this.getGrid()[10][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("OPEN_SHOP");
                return res;
            });
        }

        // 3. AUBERGE (A Droite - [20][15])
        int innLevel = team.getHubUpgradeLevel("AUBERGE");
        if (innLevel == 0) {
            this.getGrid()[20][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_AUBERGE");
                return res;
            });
        } else {
            // Ajouter le modele 3D de l'auberge
            Room innRoom = new Room(18, 12, 6, 6);
            innRoom.prefabModel = "assets/models/dungeon/naheulbeuk/rooms/spawn_room_3x3.obj"; // Placeholder
            this.getPrefabRooms().add(innRoom);
            
            this.getGrid()[20][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("OPEN_INN");
                return res;
            });
        }

        return true;
    }
}