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

    private boolean isTeamAlive(){
        for (Character c : team.getMembers()){
            if (c.getHealthPoint() > 0){
                return true;
            }
        }
        return false;
    }

    public boolean start(){
        while(monster.getHealthPoint() > 0 && isTeamAlive()){

            playerTurn();

            if (monster.getHealthPoint() <= 0) { //vérification si le monstre est dcd
                System.out.println("\n C'est trop facile");
                return true; //Victoire
            }

            monsterTurn();

            if (!isTeamAlive()) { //vérification si la team est dcd
                System.out.println("\n Plutôt paradis des Nains ou des Aventuriers ?");
                return false; //GameOver
            }
        }
        return false;
    }

    private void playerTurn(){
        System.out.println("\n" + monster.getName().toUpperCase() + " (PV : " + Math.max(0, monster.getHealthPoint()) + ") \n");
        for (int i = 0; i < team.getMembers().size(); i++) { //Affiche les stats de la team
            Character c = team.getMembers().get(i);
            if (c.getHealthPoint() > 0) {
                System.out.println(i + ". " + c.getName() + " | PV: " + c.getHealthPoint() + " | Attaque: " + c.getAttack());
            }
        }

        System.out.println("Qui passe à l'action ?");
        System.out.print("> ");
        int choice = Integer.parseInt(keyboard.nextLine().trim());
        Character attacker = team.getMembers().get(choice); //on récupère le héro choisi

        if (attacker.getHealthPoint() > 0) {
            int damage = Math.max(1, attacker.getAttack() - monster.getDefense()); //Calcule des dégats
            monster.setHealthPoint(monster.getHealthPoint() - damage); //On retire les pdv
            System.out.println(attacker.getName() + " tape de toute ses forces");
        } else {
            System.out.println(attacker.getName() +" est un peu trop mort pour faire ça");
        }

    }

    private void monsterTurn(){
        List<Character> aliveHeroes = new ArrayList<>(); //on cherche les membres vivant de la compagnie
        for (Character c : team.getMembers()) {
            if (c.getHealthPoint() > 0) {
                aliveHeroes.add(c); //on ajoute seulement les vivants
            }
        }

        if (!aliveHeroes.isEmpty()){
            Character target = aliveHeroes.get(random.nextInt(aliveHeroes.size())); //cible au hasard
            int damage = Math.max(1, monster.getAttack() - target.getDefense()); //Calcule des dégats
            target.setHealthPoint((target.getHealthPoint()) - damage); //On retir les pdv
        }
    }
}
