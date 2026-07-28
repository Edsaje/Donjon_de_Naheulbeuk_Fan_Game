package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Moteur de combat tour par tour entre la Compagnie de Naheulbeuk et un Ennemi.
 * Gère l'alternance des tours, le choix des attaques physiques et compétences magiques,
 * ainsi que l'IA ennemie et les conditions de victoire/défaite.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Battle {
    private Team team;
    private Character monster;
    private Scanner keyboard = new Scanner(System.in);
    private Random random = new Random();

    /**
     * Initialise un nouvel affrontement entre l'équipe du joueur et un monstre.
     *
     * @param team    La compagnie des héros
     * @param monster Le monstre affronté
     */
    public Battle(Team team, Character monster) {
        this.team = team;
        this.monster = monster;
    }

    /**
     * Vérifie si au moins un membre de la compagnie a des PV > 0.
     *
     * @return true si l'équipe est vivante, false si tous les héros sont KO.
     */
    private boolean isTeamAlive() {
        for (Character c : team.getMembers()) {
            if (c.getHealthPoint() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lance la boucle principale du combat tour par tour.
     *
     * @return true si la compagnie remporte le combat, false si c'est un Game Over.
     */
    public boolean start() {
        while (monster.getHealthPoint() > 0 && isTeamAlive()) {

            playerTurn();

            if (monster.getHealthPoint() <= 0) { // Vérification si le monstre est vaincu
                System.out.println("\n C'est trop facile");
                return true; // Victoire
            }

            monsterTurn();

            if (!isTeamAlive()) { // Vérification si l'équipe est vaincue
                System.out.println("\n Plutôt paradis des Nains ou des Aventuriers ?");
                return false; // Game Over
            }
        }
        return false;
    }

    /**
     * Gère le tour d'action de la compagnie.
     * Affiche les statistiques des héros, sélectionne l'attaquant et exécute l'action choisie
     * (Attaque physique ou Compétence spéciale/Sort).
     * Gère le gain potentiel de ressources. (Rage/Energie)
     */
    private void playerTurn() {
        System.out.println("\n" + monster.getName().toUpperCase() + " (PV : " + Math.max(0, monster.getHealthPoint()) + ") \n");
        for (int i = 0; i < team.getMembers().size(); i++) { // Affiche les stats de la team
            Character c = team.getMembers().get(i);
            if (c.getHealthPoint() > 0) {
                System.out.println(i + ". " + c.getName() + " | PV: " + c.getHealthPoint() + " | Attaque: " + c.getAttack());
            }

            if ("Energie".equals(c.getResourceName())){
                c.addResource(20); //+20 Energie par tour
            }
        }


        System.out.println("\nQui passe à l'action ?");
        System.out.print("> ");
        int choice = Integer.parseInt(keyboard.nextLine().trim());
        Character attacker = team.getMembers().get(choice); // On récupère le héros choisi

        System.out.println("\n" + attacker.getName() + " réfléchit à sa prochaine action...");
        System.out.println("1. Attaque Physique");
        System.out.println("2. Compétence Spéciale / Magie");
        System.out.print("> ");
        int action = Integer.parseInt(keyboard.nextLine().trim());

        if (action == 1) {
            if (attacker.getHealthPoint() > 0) {
                int damage = Math.max(1, attacker.getAttack() - monster.getDefense()); // Calcul des dégâts
                monster.setHealthPoint(monster.getHealthPoint() - damage); // On retire les PV
                System.out.println(attacker.getName() + " tape de toutes ses forces et inflige " + damage + " point(s) de dégât !");
                System.out.println("\nIl reste " + Math.max(0, monster.getHealthPoint()) + " PV au " + monster.getName() + " !");

                if ("Rage".equals(attacker.getResourceName())){
                    attacker.addResource(10); //+10 de Rage quand il frappe
                }

            } else {
                System.out.println("\n" + attacker.getName() + " est un peu trop mort pour faire ça");
            }
        }

        if (action == 2) {
            if (attacker.getHealthPoint() > 0) {
                attacker.useSpecialSkill(team, monster, keyboard);
            } else {
                System.out.println("\n" + attacker.getName() + " est un peu trop mort pour faire ça");
            }
        }
    }

    /**
     * Gère la riposte de l'ennemi.
     * Filtre les héros vivants et frappe une cible aléatoire parmi eux.
     * Gère l'augmentation de rage quand un coup est subit
     */
    private void monsterTurn() {
        List<Character> aliveHeroes = new ArrayList<>(); // On cherche les membres vivants de la compagnie
        for (Character c : team.getMembers()) {
            if (c.getHealthPoint() > 0) {
                aliveHeroes.add(c); // On ajoute seulement les vivants
            }
        }

        if (!aliveHeroes.isEmpty()) {
            Character target = aliveHeroes.get(random.nextInt(aliveHeroes.size())); // Cible au hasard
            int damage = Math.max(1, monster.getAttack() - target.getDefense()); // Calcul des dégâts
            target.setHealthPoint(target.getHealthPoint() - damage); // On retire les PV

            if("Rage".equals(target.getResourceName())){
                target.addResource(15); //+15 de Rage quand il prend un coup
            }
        }
    }

}
