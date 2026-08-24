package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 * Représente une case individuelle dans la grille 2D du donjon.
 * Une cellule possède 4 murs (Nord, Sud, Est, Ouest), un statut de visite,
 * et peut éventuellement héberger un monstre.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    // Attributs
    private int x, y;
    private boolean visited = false; // Used by maze generator
    private boolean discovered = false; // Used for Fog of War
    private int roomId = 0; // 0 = corridor, >0 = room
    private boolean wall = true; // Par défaut : Bloc de mur massif infranchissable
    private boolean wallNorth = true;
    private boolean wallSouth = true;
    private boolean wallWest = true;
    private boolean wallEast = true;

    
    private Item item = null;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.ICellEvent event = null;
    private boolean stairs = false;
    private boolean hasDoor = false;
    private boolean doorOpen = false;
    /**
     * Initialise une cellule avec ses coordonnées (X, Y).
     * Par défaut, tous les murs sont fermés et la case est non visitée.
     *
     * @param x Coordonnée X (Colonne)
     * @param y Coordonnée Y (Ligne)
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;

        // Par défaut lors de la création :
        this.visited = false;     // Pas encore visitée
        this.wallNorth = true;    // Mur Nord présent
        this.wallSouth = true;    // Mur Sud présent
        this.wallEast = true;     // Mur Est présent
        this.wallWest = true;     // Mur Ouest présent
    }

    /**
     * Abat le mur séparateur entre cette cellule et une cellule voisine adjacente.
     * Détermine automatiquement la direction (Nord, Sud, Est, Ouest) selon les coordonnées.
     *
     * @param neighbor Cellule voisine adjacente
     */
    public void removeWallBetween(Cell neighbor) {
        int dx = neighbor.x - this.x; // Différence horizontale
        int dy = neighbor.y - this.y; // Différence verticale

        if (dx == 1) { // Voisin de droite (Est)
            this.wallEast = false;
            neighbor.wallWest = false;
        }

        if (dx == -1) { // Voisin de gauche (Ouest)
            this.wallWest = false;
            neighbor.wallEast = false;
        }

        if (dy == 1) { // Voisin du bas (Sud)
            this.wallSouth = false;
            neighbor.wallNorth = false;
        }

        if (dy == -1) { // Voisin du haut (Nord)
            this.wallNorth = false;
            neighbor.wallSouth = false;
        }
    }



    /**
     * @return Coordonnée X de la case
     */
    public int getX() {
        return x;
    }

    /**
     * @param x Nouvelle coordonnée X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Coordonnée Y de la case
     */
    public int getY() {
        return y;
    }

    /**
     * @param y Nouvelle coordonnée Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return true si la case a été visitée par le générateur
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited Nouveau statut de visite
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return true si le mur Nord est présent
     */
    public boolean isWallNorth() {
        return wallNorth;
    }

    /**
     * @param wallNorth Présence du mur Nord
     */
    public void setWallNorth(boolean wallNorth) {
        this.wallNorth = wallNorth;
    }

    /**
     * @return true si le mur Sud est présent
     */
    public boolean isWallSouth() {
        return wallSouth;
    }

    /**
     * @param wallSouth Présence du mur Sud
     */
    public void setWallSouth(boolean wallSouth) {
        this.wallSouth = wallSouth;
    }

    /**
     * @return true si le mur Ouest est présent
     */
    public boolean isWallWest() {
        return wallWest;
    }

    /**
     * @param wallWest Présence du mur Ouest
     */
    public void setWallWest(boolean wallWest) {
        this.wallWest = wallWest;
    }

    /**
     * @return true si le mur Est est présent
     */
    public boolean isWallEast() {
        return wallEast;
    }

    /**
     * @param wallEast Présence du mur Est
     */
    public void setWallEast(boolean wallEast) {
        this.wallEast = wallEast;
    }



    /**
     * Indique si un coffre ou un objet est présent sur cette case.
     *
     * @return true si la case contient un objet, false sinon.
     */
    public boolean hasItem() {
        return item != null;
    }

    /**
     * @return L'objet présent sur la case (ou null si aucun)
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item Objet à placer sur la case (ou null pour retirer)
     */
    public void setItem(Item item) {
        this.item = item;
    }

    public boolean hasStairs() {
        return stairs;
    }

    public void setStairs(boolean stairs) {
        this.stairs = stairs;
    }
    
    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.ICellEvent getEvent() {
        return event;
    }

    public void setEvent(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.ICellEvent event) {
        this.event = event;
    }

    public boolean hasBlockingEvent() {
        return event != null;
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isWalkable() {
        if (wall) return false;
        return true;
    }
    public boolean hasDoor() { return hasDoor; }
    public void setHasDoor(boolean b) { hasDoor = b; }
    public boolean isDoorOpen() { return doorOpen; }
    public void setDoorOpen(boolean b) { doorOpen = b; }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}



