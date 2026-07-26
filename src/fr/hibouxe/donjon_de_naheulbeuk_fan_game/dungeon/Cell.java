package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

public class Cell {
    //attributs
    private int x, y;
    private boolean visited = false;
    private boolean wallNorth = true;
    private boolean wallSouth = true;
    private boolean wallWest = true;
    private boolean wallEast = true;

    private Character monster = null; //pas de monstre par défaut

    //constructeur
    public Cell(int x, int y){
        this.x = x;
        this.y = y;

        // Par défaut lors de la création :
        this.visited = false;     // Pas encore visitée
        this.wallNorth = true;    // Mur Nord présent
        this.wallSouth = true;    // Mur Sud présent
        this.wallEast = true;     // Mur Est présent
        this.wallWest = true;     // Mur Ouest présent
    }

    //méthode

    public void removeWallBetween(Cell neighbor){
        int dx = neighbor.x - this.x; //différence horizontale
        int dy = neighbor.y - this.y; //différence verticale

        if (dx == 1){ //voisin de droite
            this.wallEast = false;
            neighbor.wallWest = false;
        }

        if (dx == -1){
            this.wallWest = false; //voisin de gauche
            neighbor.wallEast = false;
        }

        if (dy == 1){ //voisin du bas
            this.wallSouth = false;
            neighbor.wallNorth = false;
        }

        if (dy == -1){ //voisin du haut
            this.wallNorth = false;
            neighbor.wallSouth = false;
        }

    }

    public boolean hasMonster() {
        return monster != null;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isWallNorth() {
        return wallNorth;
    }

    public void setWallNorth(boolean wallNorth) {
        this.wallNorth = wallNorth;
    }

    public boolean isWallSouth() {
        return wallSouth;
    }

    public void setWallSouth(boolean wallSouth) {
        this.wallSouth = wallSouth;
    }

    public boolean isWallWest() {
        return wallWest;
    }

    public void setWallWest(boolean wallWest) {
        this.wallWest = wallWest;
    }

    public boolean isWallEast() {
        return wallEast;
    }

    public void setWallEast(boolean wallEast) {
        this.wallEast = wallEast;
    }

    public Character getMonster() {
        return monster;
    }

    public void setMonster(Character monster) {
        this.monster = monster;
    }
}
