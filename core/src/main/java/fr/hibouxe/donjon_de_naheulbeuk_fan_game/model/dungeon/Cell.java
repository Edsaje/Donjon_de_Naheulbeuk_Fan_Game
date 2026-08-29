package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 * Reprsente une case individuelle dans la grille 2D du donjon.
 * Une cellule possde 4 murs (Nord, Sud, Est, Ouest), un statut de visite,
 * et peut ventuellement hberger un monstre.
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
    private boolean wall = true; // Par dfaut : Bloc de mur massif infranchissable
    private boolean wallNorth = true;
    private boolean wallSouth = true;
    private boolean wallWest = true;
    private boolean wallEast = true;

    
    private Item item = null;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.ICellEvent event = null;
    private boolean stairs = false;
    private boolean hasDoor = false;
    private boolean doorOpen = false;
    private float doorOpenProgress = 0f;
    private boolean hasChest = false;
    private boolean chestOpen = false;
    private float chestOpenProgress = 0f;
    /**
     * Initialise une cellule avec ses coordonnes (X, Y).
     * Par dfaut, tous les murs sont ferms et la case est non visite.
     *
     * @param x Coordonne X (Colonne)
     * @param y Coordonne Y (Ligne)
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;

        // Par dfaut lors de la cration :
        this.visited = false;     // Pas encore visite
        this.wallNorth = true;    // Mur Nord prsent
        this.wallSouth = true;    // Mur Sud prsent
        this.wallEast = true;     // Mur Est prsent
        this.wallWest = true;     // Mur Ouest prsent
    }

    /**
     * Abat le mur sparateur entre cette cellule et une cellule voisine adjacente.
     * Dtermine automatiquement la direction (Nord, Sud, Est, Ouest) selon les coordonnes.
     *
     * @param neighbor Cellule voisine adjacente
     */
    public void removeWallBetween(Cell neighbor) {
        int dx = neighbor.x - this.x; // Diffrence horizontale
        int dy = neighbor.y - this.y; // Diffrence verticale

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
     * @return Coordonne X de la case
     */
    public int getX() {
        return x;
    }

    /**
     * @param x Nouvelle coordonne X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Coordonne Y de la case
     */
    public int getY() {
        return y;
    }

    /**
     * @param y Nouvelle coordonne Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return true si la case a t visite par le gnrateur
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
     * @return true si le mur Nord est prsent
     */
    public boolean isWallNorth() {
        return wallNorth;
    }

    /**
     * @param wallNorth Prsence du mur Nord
     */
    public void setWallNorth(boolean wallNorth) {
        this.wallNorth = wallNorth;
    }

    /**
     * @return true si le mur Sud est prsent
     */
    public boolean isWallSouth() {
        return wallSouth;
    }

    /**
     * @param wallSouth Prsence du mur Sud
     */
    public void setWallSouth(boolean wallSouth) {
        this.wallSouth = wallSouth;
    }

    /**
     * @return true si le mur Ouest est prsent
     */
    public boolean isWallWest() {
        return wallWest;
    }

    /**
     * @param wallWest Prsence du mur Ouest
     */
    public void setWallWest(boolean wallWest) {
        this.wallWest = wallWest;
    }

    /**
     * @return true si le mur Est est prsent
     */
    public boolean isWallEast() {
        return wallEast;
    }

    /**
     * @param wallEast Prsence du mur Est
     */
    public void setWallEast(boolean wallEast) {
        this.wallEast = wallEast;
    }



    /**
     * Indique si un coffre ou un objet est prsent sur cette case.
     *
     * @return true si la case contient un objet, false sinon.
     */
    public boolean hasItem() {
        return item != null;
    }

    /**
     * @return L'objet prsent sur la case (ou null si aucun)
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item Objet  placer sur la case (ou null pour retirer)
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
    public float getDoorOpenProgress() { return doorOpenProgress; }
    public void setDoorOpenProgress(float p) { doorOpenProgress = p; }

    public boolean hasChest() { return hasChest; }
    public void setHasChest(boolean b) { hasChest = b; }
    public boolean isChestOpen() { return chestOpen; }
    public void setChestOpen(boolean b) { chestOpen = b; }
    public float getChestOpenProgress() { return chestOpenProgress; }
    public void setChestOpenProgress(float p) { chestOpenProgress = p; }

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



