package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Team team;
    private Character monster;
    private Scanner keyboard = new Scanner(System.in);
    private Random random = new Random();

    public Battle(Team team, Character monster) {
        this.team = team;
        this.monster = monster;
    }

}
