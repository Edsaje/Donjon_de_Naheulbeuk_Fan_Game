package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;

/**
 * Représente une case individuelle dans la grille 2D du donjon.
 * Une cellule possède 4 murs (Nord, Sud, Est, Ouest), un statut de visite,
 * et peut éventuellement héberger un monstre.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Cell {
    // Attributs
    private int x, y;
    private boolean visited = false;
    private boolean wallNorth = true;
    private boolean wallSouth = true;
    private boolean wallWest = true;
    private boolean wallEast = true;

    private Character monster = null; // Pas de monstre par défaut
    private Item item = null;
    private boolean stairs = false;
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
     * Indique si un monstre est présent sur cette case.
     *
     * @return true si la case contient un monstre, false sinon.
     */
    public boolean hasMonster() {
        return monster != null;
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
     * @return Le monstre présent sur la case (ou null si aucun)
     */
    public Character getMonster() {
        return monster;
    }

    /**
     * @param monster Monstre à placer sur la case (ou null pour retirer)
     */
    public void setMonster(Character monster) {
        this.monster = monster;
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
}
