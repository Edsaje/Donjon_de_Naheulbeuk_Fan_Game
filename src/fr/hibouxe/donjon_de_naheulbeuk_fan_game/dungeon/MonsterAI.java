package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy.Goblin;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy.Skeleton;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy.Undead;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Contrôleur d'Intelligence Artificielle et de Pathfinding (BFS, Line of Sight & Archétypes Tactiques).
 * Responsable du déplacement synchrone et intelligent des monstres selon leur personnalité (Poltron, Guerrier, Embusqué).
 * Respecte à 100% le principe MVC et SOLID (SRP et OCP).
 *
 * @author Hibouxe
 * @version 2.0
 */
public class MonsterAI {
    private Random random = new Random();

    /**
     * Déplace tous les monstres du donjon de 1 case au tour par tour (Style Pokémon Donjon Mystère).
     * Évalue le comportement par archétype (Guerrier, Poltron, Embusqué).
     * Deux groupes de monstres ne peuvent jamais se superposer.
     *
     * @param dungeon Le donjon contenant la grille
     * @param team    L'équipe de la compagnie
     * @param menu    La vue principale (Injectée)
     */
    public void moveMonsters(Dungeon dungeon, Team team, Menu menu) {
        List<Character> movedMonsters = new ArrayList<>();
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell currentCell = grid[x][y];

                if (currentCell.hasMonster()) {
                    List<Character> monstersOnCell = new ArrayList<>(currentCell.getMonsters());
                    int groupSize = monstersOnCell.size();

                    for (Character monster : monstersOnCell) {
                        if (movedMonsters.contains(monster)) {
                            continue; // Déjà déplacé pendant ce tour
                        }

                        int[] targetStep = determineTargetStep(dungeon, monster, groupSize, x, y, team);
                        int targetX = targetStep[0];
                        int targetY = targetStep[1];

                        // Effectuer le déplacement si la case de destination est libre et différente
                        if (targetX != x || targetY != y) {
                            currentCell.getMonsters().remove(monster);
                            grid[targetX][targetY].getMonsters().add(monster);
                            movedMonsters.add(monster);
                        }
                    }
                }
            }
        }
    }

    /**
     * Détermine la case cible du monstre en fonction de son archétype tactique.
     *
     * @param dungeon   Le donjon
     * @param monster   Le monstre qui réfléchit
     * @param groupSize La taille du groupe sur cette case
     * @param x         X actuel du monstre
     * @param y         Y actuel du monstre
     * @param team      L'équipe de la compagnie
     * @return Les coordonnées [targetX, targetY] de la case voulue.
     */
    public int[] determineTargetStep(Dungeon dungeon, Character monster, int groupSize, int x, int y, Team team) {
        int teamX = team.getX();
        int teamY = team.getY();
        int distance = Math.abs(x - teamX) + Math.abs(y - teamY);
        boolean hasSight = hasLineOfSight(dungeon, x, y, teamX, teamY);

        // 1. Archétype Poltron (Gobelin seul) : Fuite loin du joueur
        if (monster instanceof Goblin && groupSize < 2) {
            if (distance <= 3 && hasSight) {
                return getFleeStep(dungeon, x, y, teamX, teamY);
            }
        }

        // 2. Archétype Embusqué (Squelette / Undead) : Sommeil puis attaque si périmètre <= 2
        if (monster instanceof Skeleton || monster instanceof Undead) {
            if (distance <= 2 && hasSight) {
                return getNextStepBFS(dungeon, x, y, teamX, teamY);
            } else {
                return new int[]{x, y}; // Rest en embuscade
            }
        }

        // 3. Archétype Guerrier (Orc, Troll, Spider, Gobelins en groupe) : BFS Agressif
        if (distance <= 4 && hasSight) {
            return getNextStepBFS(dungeon, x, y, teamX, teamY);
        }

        // 4. Mode Balade par défaut
        return getWanderStep(dungeon, x, y);
    }

    /**
     * Calcule la case de fuite qui s'éloigne le plus du joueur (BFS Inversé / Fuite Poltronne).
     *
     * @param dungeon Le donjon
     * @param x       X du monstre
     * @param y       Y du monstre
     * @param teamX   X du joueur
     * @param teamY   Y du joueur
     * @return Case voisine la plus éloignée du joueur
     */
    public int[] getFleeStep(Dungeon dungeon, int x, int y, int teamX, int teamY) {
        Cell currentCell = dungeon.getGrid()[x][y];
        List<int[]> validNeighbors = getValidUnoccupiedNeighbors(dungeon, currentCell, x, y);

        if (validNeighbors.isEmpty()) return new int[]{x, y};

        int bestDistance = -1;
        int[] bestStep = new int[]{x, y};

        for (int[] pos : validNeighbors) {
            int dist = Math.abs(pos[0] - teamX) + Math.abs(pos[1] - teamY);
            if (dist > bestDistance) {
                bestDistance = dist;
                bestStep = pos;
            }
        }
        return bestStep;
    }

    /**
     * Calcule une case de balade aléatoire non occupée par un autre groupe de monstres.
     *
     * @param dungeon Le donjon
     * @param x       X du monstre
     * @param y       Y du monstre
     * @return Case voisine aléatoire libre ou case actuelle
     */
    public int[] getWanderStep(Dungeon dungeon, int x, int y) {
        Cell currentCell = dungeon.getGrid()[x][y];
        List<int[]> validNeighbors = getValidUnoccupiedNeighbors(dungeon, currentCell, x, y);

        if (validNeighbors.isEmpty()) return new int[]{x, y};

        return validNeighbors.get(random.nextInt(validNeighbors.size()));
    }

    /**
     * Calcule la prochaine case pour se rapprocher du joueur en contournant les murs et les monstres (Algorithme BFS).
     *
     * @param dungeon Le donjon
     * @param startX  X du monstre
     * @param startY  Y du monstre
     * @param targetX X du joueur
     * @param targetY Y du joueur
     * @return Un tableau int[]{nextX, nextY} représentant la case suivante.
     */
    public int[] getNextStepBFS(Dungeon dungeon, int startX, int startY, int targetX, int targetY) {
        if (startX == targetX && startY == targetY) return new int[]{startX, startY};

        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        Cell[][] grid = dungeon.getGrid();

        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[width][height];
        int[][][] parent = new int[width][height][2];

        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        boolean found = false;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cx = curr[0];
            int cy = curr[1];

            if (cx == targetX && cy == targetY) {
                found = true;
                break;
            }

            Cell cell = grid[cx][cy];

            // Voisin Nord
            if (!cell.isWallNorth() && cy > 0 && !visited[cx][cy - 1]) {
                if (!grid[cx][cy - 1].hasMonster() || (cx == targetX && cy - 1 == targetY)) {
                    visited[cx][cy - 1] = true;
                    parent[cx][cy - 1] = new int[]{cx, cy};
                    queue.add(new int[]{cx, cy - 1});
                }
            }

            // Voisin Sud
            if (!cell.isWallSouth() && cy < height - 1 && !visited[cx][cy + 1]) {
                if (!grid[cx][cy + 1].hasMonster() || (cx == targetX && cy + 1 == targetY)) {
                    visited[cx][cy + 1] = true;
                    parent[cx][cy + 1] = new int[]{cx, cy};
                    queue.add(new int[]{cx, cy + 1});
                }
            }

            // Voisin Ouest
            if (!cell.isWallWest() && cx > 0 && !visited[cx - 1][cy]) {
                if (!grid[cx - 1][cy].hasMonster() || (cx - 1 == targetX && cy == targetY)) {
                    visited[cx - 1][cy] = true;
                    parent[cx - 1][cy] = new int[]{cx, cy};
                    queue.add(new int[]{cx - 1, cy});
                }
            }

            // Voisin Est
            if (!cell.isWallEast() && cx < width - 1 && !visited[cx + 1][cy]) {
                if (!grid[cx + 1][cy].hasMonster() || (cx + 1 == targetX && cy == targetY)) {
                    visited[cx + 1][cy] = true;
                    parent[cx + 1][cy] = new int[]{cx, cy};
                    queue.add(new int[]{cx + 1, cy});
                }
            }
        }

        if (!found) return new int[]{startX, startY}; // Chemin bloqué par un mur ou d'autres monstres

        // Reconstruire le premier pas en remontant le tableau parent
        int currX = targetX;
        int currY = targetY;

        while (true) {
            int px = parent[currX][currY][0];
            int py = parent[currX][currY][1];

            if (px == startX && py == startY) {
                return new int[]{currX, currY}; // Première case du parcours !
            }
            currX = px;
            currY = py;
        }
    }

    /**
     * Vérifie si un monstre en (x1, y1) a une ligne de vue dégagée vers le joueur en (x2, y2).
     * Raycasting par vérification des murs le long du rayon visuel.
     *
     * @param dungeon Le donjon
     * @param x1      X du monstre
     * @param y1      Y du monstre
     * @param x2      X du joueur
     * @param y2      Y du joueur
     * @return true si aucun mur fermé n'intercepte la ligne de vue, false sinon.
     */
    public boolean hasLineOfSight(Dungeon dungeon, int x1, int y1, int x2, int y2) {
        Cell[][] grid = dungeon.getGrid();
        int currX = x1;
        int currY = y1;

        int stepX = (x1 < x2) ? 1 : (x1 > x2) ? -1 : 0;
        int stepY = (y1 < y2) ? 1 : (y1 > y2) ? -1 : 0;

        while (currX != x2 || currY != y2) {
            Cell cell = grid[currX][currY];

            if (currX != x2) {
                if (stepX > 0 && cell.isWallEast()) return false;
                if (stepX < 0 && cell.isWallWest()) return false;
                currX += stepX;
            } else if (currY != y2) {
                if (stepY > 0 && cell.isWallSouth()) return false;
                if (stepY < 0 && cell.isWallNorth()) return false;
                currY += stepY;
            }
        }

        return true;
    }

    private List<int[]> getValidUnoccupiedNeighbors(Dungeon dungeon, Cell cell, int x, int y) {
        List<int[]> neighbors = new ArrayList<>();
        Cell[][] grid = dungeon.getGrid();
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();

        if (!cell.isWallNorth() && y > 0 && !grid[x][y - 1].hasMonster()) {
            neighbors.add(new int[]{x, y - 1});
        }
        if (!cell.isWallSouth() && y < height - 1 && !grid[x][y + 1].hasMonster()) {
            neighbors.add(new int[]{x, y + 1});
        }
        if (!cell.isWallWest() && x > 0 && !grid[x - 1][y].hasMonster()) {
            neighbors.add(new int[]{x - 1, y});
        }
        if (!cell.isWallEast() && x < width - 1 && !grid[x + 1][y].hasMonster()) {
            neighbors.add(new int[]{x + 1, y});
        }
        return neighbors;
    }
}
