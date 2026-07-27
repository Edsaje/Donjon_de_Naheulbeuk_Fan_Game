package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

import java.util.Scanner;

public class Menu {
    Scanner keyboard = new Scanner(System.in);

    public void introduction(){

    }

    public int askPlayerInt(){
        System.out.print("> ");
        try {
            return keyboard.nextInt();
        }
        catch (java.util.InputMismatchException e) {
        }
        return 0;
    }

    public String askPlayerString(){
        keyboard.nextLine(); //vider buffer
        System.out.print("> ");
        return keyboard.nextLine();
    }

    public String askPlayerMovement(){
        System.out.print("\nDéplacement (Z: Nord, S: Sud, Q: Ouest, D: Est | C: Fiche de la compagnie | X: Quitter) : ");
        return keyboard.nextLine().trim().toUpperCase(); //éviter la casse
    }

    public void displayWallCollision(){ //à améliorer avec de l'aléatoire
        System.out.println("\nTu vas dans un mur");
    }

    public void displayTeamStats(Team team){
        System.out.println("\n=================== Fiche de la compagnie de Naheulbeuk ===================");
        for (Character c : team.getMembers()){
            System.out.printf("🔹 %-12s | Niv %d | PV: %2d | Mana: %2d | Attaque: %2d | Défense: %2d%n",
                    c.getName(), c.getLevel(), c.getHealthPoint(), c.getManaPoint(), c.getAttack(), c.getDefense());
        }
        System.out.println("===========================================================================\n");

    }


    public void display(Maze maze, Team team){
        int width = maze.getWidth();
        int height = maze.getHeight();
        Cell[][] grid = maze.getGrid();

        // 1. Dessiner le bord tout en haut du labyrinthe
        for (int x = 0; x < width; x++){
            System.out.print("+---");
        }
        System.out.println("+");

        // 2. Parcourir ligne par ligne (y de 0 à height-1)
        for (int y = 0; y < height; y++) {

            // Ligne A : Le contenu des cases et les murs Est/Ouest
            System.out.print("|"); // Bordure gauche
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (x == team.getX() && y == team.getY()) {
                    System.out.print(" @ "); // Le symbole de la compagnie de Naheulbeuk !
                }
                else if (cell.hasMonster()){
                    System.out.print(" M "); // Le symbole des monstres
                }
                else {
                    System.out.print("   "); // Case vide
                }
                if (cell.isWallEast()) {
                    System.out.print("|");
                } else {
                    System.out.print(" "); // Passage ouvert vers l'Est
                }
            }
            System.out.println(); // Fin de la ligne des cases

            // Ligne B : Les murs du bas (Sud) et les coins '+'
            System.out.print("+");
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                if (cell.isWallSouth()) {
                    System.out.print("---");
                } else {
                    System.out.print("   "); // Passage ouvert vers le Sud
                }
                System.out.print("+");
            }
            System.out.println(); // Fin de la ligne des murs Sud
        }
    }
}
