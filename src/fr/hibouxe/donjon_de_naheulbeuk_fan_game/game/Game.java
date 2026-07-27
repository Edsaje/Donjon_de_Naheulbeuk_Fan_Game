package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Maze;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

public class Game {
    //Attributs
    private Maze maze;
    private Team team;
    private boolean running;
    Menu menu = new Menu();

    public void startGame(){
        //génération du Maze
        this.maze = new Maze(10, 10);
        this.maze.generateMaze();
        this.maze.generateRandomRooms(6, 2,4);

        this.maze.generateMonsters(5);

        this.running = true;
        //création de la Team
        this.team = new Team();

        while (running) {
            menu.display(maze, team);
            playerMovement();
        }
    }

    public void playerMovement(){
        String choice = menu.askPlayerMovement();
        boolean moved = false;

        switch (choice){
            case "Z": moved = tryMoveNorth();
            break;

            case "S": moved = tryMoveSouth();
            break;

            case "Q": moved = tryMoveWest();
            break;

            case "D": moved = tryMoveEast();
            break;

            case "X":
                running= false; //stop le jeu
                System.out.println("Tchoss Nulloss");
                break;

            case "C":
                menu.displayTeamStats(team);
                break;

            default:
                System.out.println("Commande inconnue");
        }

        if (!moved && !choice.equals("X") && "ZSQD".contains(choice)) {
            menu.displayWallCollision();
        }

        if (moved) {
            Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
            if (currentCell.hasMonster()) {
                Character monster = currentCell.getMonster();
                System.out.println("\nUN " + monster.getName().toUpperCase() + " ! BASTOOON ! ");

                Battle battle = new Battle(team, monster);
                boolean victory = battle.start();

                if (victory) {
                    currentCell.setMonster(null); //on retire le monstre
                } else {
                    running = false; //on ferme le jeu
                }

                currentCell.setMonster(null); //on supprime le monstre si on gagne
            }
        }
    }

    public boolean tryMoveNorth() {
        //this demande à Maze la cellule de ma team
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        // il vérifie si le mur est là ou non
        if (!currentCell.isWallNorth()){
            team.moveNorth();
            return true;
        }
        return false; //il y a un mur
    }

    public boolean tryMoveSouth() {
        //this demande à Maze la cellule de ma team
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        // il vérifie si le mur est là ou non
        if (!currentCell.isWallSouth()){
            team.moveSouth();
            return true;
        }
        return false; //il y a un mur
    }

    public boolean tryMoveWest() {
        //this demande à Maze la cellule de ma team
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        // il vérifie si le mur est là ou non
        if (!currentCell.isWallWest()){
            team.moveWest();
            return true;
        }
        return false; //il y a un mur
    }

    public boolean tryMoveEast() {
        //this demande à Maze la cellule de ma team
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        // il vérifie si le mur est là ou non
        if (!currentCell.isWallEast()){
            team.moveEast();
            return true;
        }
        return false; //il y a un mur
    }
}
