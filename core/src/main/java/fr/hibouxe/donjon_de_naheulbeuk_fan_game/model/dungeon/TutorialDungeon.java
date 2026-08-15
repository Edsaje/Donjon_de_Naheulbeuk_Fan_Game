package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Goblin;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.enemy.Orc;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Thief;
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
            case 3:
                return prepareFloor3(team);
            case 4:
                return prepareFloor4(team);
            case 5:
                return prepareFloor5(team);
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

        // Ajouter l'événement de l'Elfe
        this.getGrid()[1][2].setEvent(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.WoundedElfEvent());
        
        return false;
    }

    private boolean prepareFloor3(Team team) {
        this.setWidth(7);
        this.setHeight(7);
        this.setGrid(new Cell[7][7]);

        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                // this.getGrid()[x][y].setRoomId(3); 
            }
        }
        
        // Creuser un petit labyrinthe
        // Ligne de départ
        for (int x = 1; x < 6; x++) {
            this.getGrid()[x][1].setWall(false);
            if (x < 5) this.getGrid()[x][1].removeWallBetween(this.getGrid()[x+1][1]);
        }
        // Couloir central
        for (int y = 1; y < 6; y++) {
            this.getGrid()[3][y].setWall(false);
            if (y < 5) this.getGrid()[3][y].removeWallBetween(this.getGrid()[3][y+1]);
        }
        // Ligne de fin
        for (int x = 1; x < 6; x++) {
            this.getGrid()[x][5].setWall(false);
            if (x < 5) this.getGrid()[x][5].removeWallBetween(this.getGrid()[x+1][5]);
        }

        // Joueur commence au début (en bas à gauche)
        team.setX(1);
        team.setY(1);
        
        // Le Voleur rejoint le groupe
        boolean hasThief = false;
        for (Character c : team.getMembers()) {
            if (c instanceof Thief) hasThief = true;
        }
        if (!hasThief) {
            team.getMembers().add(new Thief());
        }

        // Escalier à la fin (en haut à droite)
        this.getGrid()[5][5].setStairs(true);

        // L'Orque de patrouille devant l'escalier
        List<Character> patrol = new ArrayList<>();
        Character orc = new Character("Patrouille Orque", "Boss", 101, 9999, 0, 999, 0, 999, 999, -1);
        patrol.add(orc);
        this.getGrid()[4][5].setMonsters(patrol);
        
        return false;
    }

    private boolean prepareFloor4(Team team) {
        // Couloir simple pour le tuto
        this.setWidth(5);
        this.setHeight(5);
        this.setGrid(new Cell[5][5]);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setRoomId(4);
            }
        }
        
        this.getGrid()[1][2].setWall(false);
        this.getGrid()[2][2].setWall(false);
        this.getGrid()[3][2].setWall(false);
        this.getGrid()[3][2].setStairs(true);

        team.setX(1);
        team.setY(2);
        
        boolean hasOgre = false;
        boolean hasMage = false;
        for (Character c : team.getMembers()) {
            if (c.getClass().getSimpleName().equals("Ogre")) hasOgre = true;
            if (c.getClass().getSimpleName().equals("Magician")) hasMage = true;
        }
        if (!hasOgre) team.getMembers().add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Ogre());
        if (!hasMage) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Magician mage = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Magician();
            mage.setManaPoint(0);
            mage.setCurrentResource(0);
            team.getMembers().add(mage);
        }

        return false;
    }

    private boolean prepareFloor5(Team team) {
        // Couloir de la sortie
        this.setWidth(5);
        this.setHeight(5);
        this.setGrid(new Cell[5][5]);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                this.getGrid()[x][y] = new Cell(x, y);
                this.getGrid()[x][y].setRoomId(5);
            }
        }
        
        // Creuser un couloir vertical sur la colonne x=2
        for (int y = 0; y < 5; y++) {
            this.getGrid()[2][y].setWall(false);
            if (y < 4) this.getGrid()[2][y].removeWallBetween(this.getGrid()[2][y+1]);
        }
        
        // Joueur en bas
        team.setX(2);
        team.setY(0);

        // Le Nain et le Barbare rejoignent le groupe
        boolean hasDwarf = false;
        boolean hasBarbarian = false;
        for (Character c : team.getMembers()) {
            if (c.getClass().getSimpleName().equals("Dwarf")) hasDwarf = true;
            if (c.getClass().getSimpleName().equals("Barbarian")) hasBarbarian = true;
        }
        if (!hasDwarf) team.getMembers().add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Dwarf());
        if (!hasBarbarian) team.getMembers().add(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Barbarian());

        // Gardes devant l'escalier
        List<Character> guards = new ArrayList<>();
        Character boss = new Character("Chef Orque", "Boss", 1, 45, 0, 6, 0, 2, 2, 2);
        boss.setXp(800);
        guards.add(boss);
        guards.add(new Character("Gobelin", "Monster", 1, 15, 0, 3, 0, 0, 0, 4));
        guards.add(new Character("Gobelin", "Monster", 1, 15, 0, 3, 0, 0, 0, 4));
        this.getGrid()[2][3].setMonsters(guards);

        // Escalier de sortie (Liberté !)
        this.getGrid()[2][4].setStairs(true);

        return false;
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        return false;
    }
}
