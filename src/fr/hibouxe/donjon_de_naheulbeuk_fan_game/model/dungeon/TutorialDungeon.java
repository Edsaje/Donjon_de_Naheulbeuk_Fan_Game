package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IGameView;
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
        Character orc = new Character("Patrouille Orque", "Boss", 99, 9999, 0, 999, 0, 999, 999, -1);
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
        
        // Murs sauf pour le couloir central (x = 2)
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x != 2) this.getGrid()[x][y].setWall(true);
            }
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
        guards.add(new Character("Chef Orque", "Boss", 3, 100, 0, 15, 0, 5, 5, 2));
        guards.add(new Character("Gobelin", "Monster", 2, 30, 0, 8, 0, 2, 2, 4));
        guards.add(new Character("Gobelin", "Monster", 2, 30, 0, 8, 0, 2, 2, 4));
        this.getGrid()[2][3].setMonsters(guards);

        // Escalier de sortie (Liberté !)
        this.getGrid()[2][4].setStairs(true);

        return false;
    }

    @Override
    public java.util.List<String> getIntroDialogues(int floorNumber) {
        java.util.List<String> dialogues = new java.util.ArrayList<>();
        switch (floorNumber) {
            case 1:
                dialogues.add("\n=== CHAPITRE 0 : Fuite de la taverne ===");
                dialogues.add("Ranger : Aïe... J'ai un mal de crâne effroyable...");
                dialogues.add("Ranger : Où est-ce que je suis ? J'étais tellement occupé à courir que je me suis perdu ! Je dois retrouver les autres !");
                dialogues.add("Utilisez Z, Q, S, D (ou les flèches) pour vous déplacer dans le couloir.");
                dialogues.add("Atteignez l'escalier au bout du chemin pour avancer.");
                break;
            case 2:
                dialogues.add("Ranger : Hey mais c'est l'Elfe ! Elle est par terre...");
                dialogues.add("Ranger : Je ferai mieux d'aller voir !");
                break;
            case 3:
                dialogues.add("Voleur : Chuuut ! Restez dans l'ombre ! On ne voit rien avec ce brouillard...");
                dialogues.add("Voleur : Il y a une patrouille d'Orques Géants juste devant. Ils sont trop nombreux !");
                dialogues.add("Ranger : Comment on passe alors ?");
                dialogues.add("Voleur : On va utiliser la Ruse !");
                dialogues.add("[UI Tuto] : Avancez pour dissiper le brouillard de guerre.");
                dialogues.add("[UI Tuto] : La Minimap en haut à droite affiche les ennemis en rouge. Évitez-les !");
                dialogues.add("[UI Tuto] : Si un combat inévitable se déclenche, utilisez la commande [Fuir] !");
                break;
            case 4:
                dialogues.add("Ogre : Chprouk ! Grrrumph !");
                dialogues.add("Magicienne : Non, tu ne peux pas le manger ! Écoutez-moi, bande de rustres...");
                dialogues.add("Magicienne : Mes réserves d'énergie astrale sont complètement épuisées et ma robe est pleine de poussière. Il nous faut faire une pause !");
                dialogues.add("Ranger : Il faut qu'on fasse le point sur notre situation stratégique, on ne sait pas ce qui nous attend au bout de ce couloir.");
                dialogues.add("Elfe : C'est quoi la stratélique ?");
                dialogues.add("Ranger : Misère...");
                dialogues.add("[UI Tuto] : Regardez le panneau sur la droite de l'écran pour suivre l'état de la compagnie.");
                dialogues.add("[UI Tuto] : Vous pouvez y voir les Points de Vie (PV) et le Mana (PM) de chaque héros en temps réel.");
                break;
            case 5:
                dialogues.add("[Bruits métalliques et cris de guerre depuis la salle suivante...]");
                dialogues.add("Nain : Prends ça dans les rotules, face de pet !");
                dialogues.add("Barbare : CROM ! Taper la porte ! Taper les gardes !");
                dialogues.add("Ranger : Ils ont trouvé la sortie ! Mais ils sont encerclés, il faut qu'on les aide !");
                dialogues.add("[UI Tuto] : Utilisez les compétences spécifiques de chaque héros pour prendre l'avantage en combat.");
                break;
        }
        return dialogues;
    }

    @Override
    public boolean isExpeditionComplete(int floorNumber) {
        return false;
    }
}
