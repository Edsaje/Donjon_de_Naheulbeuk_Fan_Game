package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon;

import java.io.Serializable;

/**
 * Gre la logique de brouillard de guerre (Fog of War) pour le Donjon.
 * Applique le principe de Responsabilit Unique (SRP) en dlguant 
 * l'algorithme de visibilit en dehors de la classe Dungeon.
 *
 * @author Hibouxe
 */
public class FogOfWarManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Rvle les cases autour d'une position donne dans la grille du donjon.
     *
     * @param grid La matrice 2D de cellules
     * @param width Largeur du donjon
     * @param height Hauteur du donjon
     * @param playerX Coordonne X du centre de vision
     * @param playerY Coordonne Y du centre de vision
     * @param radius Rayon de vision dans les couloirs
     */
    public void updateVisibility(Cell[][] grid, int width, int height, int playerX, int playerY, int radius) {
        if (grid == null || playerX < 0 || playerX >= width || playerY < 0 || playerY >= height) {
            return; // Scurit out-of-bounds
        }

        int currentRoomId = grid[playerX][playerY].getRoomId();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // 1. Rvlation radiale (couloirs et vision proche)
                if (Math.abs(x - playerX) + Math.abs(y - playerY) <= radius + 1) { 
                    if (x >= Math.max(0, playerX - radius) && x <= Math.min(width - 1, playerX + radius) &&
                        y >= Math.max(0, playerY - radius) && y <= Math.min(height - 1, playerY + radius)) {
                        grid[x][y].setDiscovered(true);
                    }
                }

                // 2. Rvlation complte de la salle si le joueur est dedans
                if (currentRoomId > 0) {
                    if (grid[x][y].getRoomId() == currentRoomId) {
                        grid[x][y].setDiscovered(true);
                        // Rvler aussi les murs adjacents  la salle pour les voir sur la minimap
                        for (int nx = Math.max(0, x - 1); nx <= Math.min(width - 1, x + 1); nx++) {
                            for (int ny = Math.max(0, y - 1); ny <= Math.min(height - 1, y + 1); ny++) {
                                grid[nx][ny].setDiscovered(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
