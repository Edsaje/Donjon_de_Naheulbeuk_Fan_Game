package fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.enemy.Goblin;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;

public class Maze {
    //attributs
    private int width;
    private int height;
    private Cell[][] grid;
    private Random random = new Random();

    //méthode
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height]; //aloue la grille

        //boucle for
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                grid[x][y] = new Cell(x,y);
            }
        }
    }

    public void generateMaze(){
        Cell startCell = grid[0][0]; //case de départ
        startCell.setVisited(true); //marquer comme visité
        Deque<Cell> stack = new ArrayDeque<>(); // création pile
        stack.push(startCell); //pose la cellule au sommet de la pile

        while (!stack.isEmpty()) {
            Cell current = stack.peek(); //regarde la cellule qui se trouve au sommet
            List<Cell> neighbors = getUnvisitedNeighbors(current);

            if (!neighbors.isEmpty()){
                //choisir un voisin au hasard
                Cell chosen = neighbors.get(random.nextInt(neighbors.size()));

                //enlever le mur
                current.removeWallBetween(chosen);

                //marquer comme visité et mettre au sommet
                chosen.setVisited(true);
                stack.push(chosen);
            } else {
                //sinon on retire de la pile
                stack.pop();
            }
        }
    }

    public void createRoom(int startX, int startY, int roomWidth, int roomHeight){
        for (int x = startX; x < startX + roomWidth; x++){
            for (int y = startY; y < startY + roomHeight; y++){
                if (x + 1 < startX + roomWidth && x + 1 < width){
                    grid[x][y].removeWallBetween(grid[x + 1][y]);
                }
                if (y + 1 < startY + roomHeight && y +1 < height){
                    grid[x][y].removeWallBetween(grid[x][y + 1]);
                }
            }
        }
    }

    public void generateRandomRooms(int numberOfRooms, int minSize, int maxSize) {
        for (int i = 0; i < numberOfRooms; i++) {
            // 1. Taille aléatoire pour cette salle
            int roomWidth = random.nextInt(maxSize - minSize + 1) + minSize;
            int roomHeight = random.nextInt(maxSize - minSize + 1) + minSize;

            // 2. Position aléatoire (en s'assurant que la salle ne déborde pas de la grille)
            int startX = random.nextInt(width - roomWidth);
            int startY = random.nextInt(height - roomHeight);

            // 3. Creuser la salle !
            createRoom(startX, startY, roomWidth, roomHeight);
        }
    }

    private List<Cell> getUnvisitedNeighbors(Cell current){ //savoir quels voisins ne sont pas visités
        int x = current.getX();
        int y = current.getY();
        List<Cell> neighbors = new ArrayList<>();

        if (y - 1 >= 0 && grid[x][y - 1].isVisited() == false){
            neighbors.add(grid[x][y - 1]);
        }
        if (y + 1 < height && grid[x][y + 1].isVisited() == false){
            neighbors.add(grid[x][y + 1]);
        }
        if (x - 1 >= 0 && grid[x - 1][y].isVisited() == false){
            neighbors.add(grid[x - 1][y]);
        }
        if (x + 1 < width && grid[x + 1][y].isVisited() == false){
            neighbors.add(grid[x + 1][y]);
        }
        return neighbors;
    }

    public void generateMonsters(int count) {
        for (int i = 0; i < count; i++){
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x != 0 || y !=0) { //pas de monstre sur 0
                grid[x][y].setMonster(new Goblin()); //je place un monstre (défini pour le moment)
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }
}
