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
        return null;
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        return false;
    }

    @Override
    public java.util.List<String> getFloorIntroDialogues(int floorNumber) {
        java.util.List<String> list = new ArrayList<>();
        list.add("HUB_INTRO");
        return list;
    }

    @Override
    public boolean prepareFloor(int floorNumber, Team team, IMonsterRepository repository) {
        this.setWidth(30);
        this.setHeight(30);
        this.setGrid(new Cell[30][30]);

        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setWall(true);
            }
        }

        for (int x = 5; x < 25; x++) {
            for (int y = 5; y < 25; y++) {
                this.getGrid()[x][y].setWall(false);
                this.getGrid()[x][y].setRoomId(1);
            }
        }
        
        this.setPrefabRooms(new ArrayList<>());

        // Point de depart vers les donjons (en bas)
        this.getGrid()[15][5].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("ENTER_DUNGEON");
            return res;
        });
        this.getGrid()[15][5].setStairs(true);

        // Feu de camp (Base) au centre [15][15]
        this.getGrid()[15][15].setEvent(t -> {
            EventResult res = new EventResult(true);
            res.setActionTrigger("OPEN_TAVERN"); // Ancien menu
            return res;
        });
        this.getGrid()[15][15].setStairs(true); // Indique visuellement qu'il y a un truc

        // 1. TAVERNE (En Haut - [15][20])
        int tavernLevel = team.getHubUpgradeLevel("TAVERNE");
        if (tavernLevel == 0) {
            this.getGrid()[15][20].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_TAVERNE");
                return res;
            });
            this.getGrid()[15][20].setStairs(true);
        } else {
            Room tavernRoom = new Room(13, 20, 5, 5);
            tavernRoom.prefabModel = "assets/models/dungeon/naheulbeuk/rooms/test_room_5x5.obj"; 
            this.getPrefabRooms().add(tavernRoom);
            
            this.getGrid()[15][20].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("ENTER_TAVERNE");
                return res;
            });
            this.getGrid()[15][20].setStairs(true);
        }

        // 2. MARCHAND (A Gauche - [10][15])
        int merchantLevel = team.getHubUpgradeLevel("MARCHAND");
        if (merchantLevel == 0) {
            this.getGrid()[10][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_MARCHAND");
                return res;
            });
            this.getGrid()[10][15].setStairs(true);
        } else {
            Room marchantRoom = new Room(9, 14, 3, 3);
            marchantRoom.prefabModel = "assets/models/dungeon/naheulbeuk/rooms/spawn_room_3x3.obj";
            this.getPrefabRooms().add(marchantRoom);
            
            this.getGrid()[10][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("OPEN_SHOP");
                return res;
            });
            this.getGrid()[10][15].setStairs(true);
        }

        // 3. AUBERGE (A Droite - [20][15])
        int innLevel = team.getHubUpgradeLevel("AUBERGE");
        if (innLevel == 0) {
            this.getGrid()[20][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("BUILD_AUBERGE");
                return res;
            });
            this.getGrid()[20][15].setStairs(true);
        } else {
            Room innRoom = new Room(18, 13, 5, 5);
            innRoom.prefabModel = "assets/models/dungeon/naheulbeuk/rooms/test_room_5x5.obj";
            this.getPrefabRooms().add(innRoom);
            
            this.getGrid()[20][15].setEvent(t -> {
                EventResult res = new EventResult(true);
                res.setActionTrigger("OPEN_INN");
                return res;
            });
            this.getGrid()[20][15].setStairs(true);
        }

        // IMPORTANT : Fixer le point de spawn de l'equipe !
        team.setX(15);
        team.setY(10);
        team.setPlayerX(15f);
        team.setPlayerZ(10f);

        return true;
    }
}