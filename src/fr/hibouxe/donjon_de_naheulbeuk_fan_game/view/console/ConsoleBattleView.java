package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.boss.Golem;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.console.ConsoleMenu;

import java.util.List;

/**
 * Sous-vue responsable de l'affichage des combats au tour par tour et des compétences contextuelles.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class ConsoleBattleView {

    public void displayBattleStatus(List<Character> monsters, Team team, ConsoleMenu menu) {
        menu.displayMessage("\n--- MONSTRES ---");
        for (int i = 0; i < monsters.size(); i++) {
            Character m = monsters.get(i);
            if (m.getHealthPoint() > 0) {
                menu.displayMessage(i + ". " + m.getName().toUpperCase() + " (PV: " + Math.max(0, m.getHealthPoint())
                        + " | Attaque: " + m.getAttack()
                        + " | Magie: " + m.getMagicAttack()
                        + " | Defense: " + m.getDefense()
                        + " | Def.Mag: " + m.getMagicDefense()
                        + " | Vitesse: " + m.getSpeed() + ")");
            }
        }

        menu.displayMessage("\n--- COMPAGNIE ---");
        for (int i = 0; i < team.getMembers().size(); i++) {
            Character c = team.getMembers().get(i);
            if (c.getHealthPoint() > 0) {
                menu.displayMessage(i + ". " + c.getName() + " (PV: " + c.getHealthPoint()
                        + " | " + c.getResourceName() + ": " + c.getResourcePoint()
                        + " | Attaque: " + c.getAttack()
                        + " | Magie: " + c.getMagicAttack()
                        + " | Defense: " + c.getDefense()
                        + " | Def.Mag: " + c.getMagicDefense()
                        + " | Vitesse: " + c.getSpeed() + ")");
            }
        }
    }

    public int askBattleAction(Character attacker, ConsoleMenu menu) {
        int choice = 0;
        while (choice != 1 && choice != 2 && choice != 3) {
            menu.displayMessage("\n" + attacker.getName() + " réfléchit à sa prochaine action...");
            menu.displayMessage("1. Attaque Physique");
            menu.displayMessage("2. Compétence Spéciale / Magie");
            menu.displayMessage("3. Fouiller dans le Sac (Inventaire)");
            choice = menu.askPlayerInt();

            if (choice != 1 && choice != 2 && choice != 3) {
                menu.displayMessage("[Erreur] Choix invalide. Veuillez entrer 1, 2 ou 3.");
            }
        }
        return choice;
    }

    public Skill askSkill(Character attacker, ConsoleMenu menu) {
        return askSkill(attacker, null, menu);
    }

    public Skill askSkill(Character attacker, List<Character> monsters, ConsoleMenu menu) {
        List<Skill> skills = attacker.getSkills();
        if (skills.isEmpty()) {
            menu.displayMessage("  Aucune compétence apprise !");
            return null;
        }
        menu.displayMessage("\n--- Compétences de " + attacker.getName() + " (0. Retour) ---");
        boolean isGolemBossPresent = isGolemPresent(monsters);

        for (int i = 0; i < skills.size(); i++) {
            Skill s = skills.get(i);
            String desc = s.getDescription();
            if (s.getName().equals("Tir Précis (ou presque)") && isGolemBossPresent) {
                desc = "Vise la fente d'assemblage du Golem de Fer pour briser sa carapace d'acier ! Risque de planter la flèche dans le derrière du Nain !";
            }
            menu.displayMessage((i + 1) + ". " + s.getName() + " (Coût : " + s.getCost() + " " + attacker.getResourceName() + ") - " + desc);
        }
        while (true) {
            int choice = menu.askPlayerInt();
            if (choice == 0) return null;
            if (choice > 0 && choice <= skills.size()) {
                return skills.get(choice - 1);
            }
            menu.displayMessage("[Erreur] Choix invalide.");
        }
    }

    private boolean isGolemPresent(List<Character> monsters) {
        if (monsters == null) return false;
        for (Character m : monsters) {
            if (m instanceof Golem && m.getHealthPoint() > 0) {
                return true;
            }
        }
        return false;
    }

    public Character askMonsterTarget(List<Character> monsters, ConsoleMenu menu) {
        int aliveCount = 0;
        Character lastAlive = null;
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) {
                aliveCount++;
                lastAlive = m;
            }
        }
        if (aliveCount == 1) {
            return lastAlive;
        }

        menu.displayMessage("\nLequel voulez-vous cibler ? (0. Retour)");
        for (int i = 0; i < monsters.size(); i++) {
            Character m = monsters.get(i);
            if (m.getHealthPoint() > 0) {
                menu.displayMessage((i + 1) + ". " + m.getName().toUpperCase() + " | PV: " + Math.max(0, m.getHealthPoint()));
            }
        }
        while (true) {
            int choice = menu.askPlayerInt();
            if (choice == 0) return null;
            if (choice > 0 && choice <= monsters.size() && monsters.get(choice - 1).getHealthPoint() > 0) {
                return monsters.get(choice - 1);
            }
            menu.displayMessage("[Erreur] Cible invalide. Veuillez entrer un numéro valide (ou 0 pour annuler).");
        }
    }

    }
