package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.*;

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

    public void display(Maze maze) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        Cell[][] grid = maze.getGrid();

        // 1. Dessiner le bord tout en haut du labyrinthe
        for (int x = 0; x < width; x++) {
            System.out.print("+---");
        }
        System.out.println("+");

        // 2. Parcourir ligne par ligne (y de 0 à height-1)
        for (int y = 0; y < height; y++) {

            // Ligne A : Le contenu des cases et les murs Est/Ouest
            System.out.print("|"); // Bordure gauche
            for (int x = 0; x < width; x++) {
                Cell cell = grid[x][y];
                System.out.print("   "); // Espace intérieur de la case
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
