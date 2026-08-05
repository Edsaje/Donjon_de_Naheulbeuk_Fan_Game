package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Skill;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;

import java.util.List;

/**
 * Sous-vue responsable de l'affichage des combats au tour par tour.
 */
public class BattleView {

    public void displayBattleStatus(List<Character> monsters, Team team, Menu menu) {
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

    public int askBattleAction(Character attacker, Menu menu) {
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

    public Skill askSkill(Character attacker, Menu menu) {
        List<Skill> skills = attacker.getSkills();
        if (skills.isEmpty()) {
            menu.displayMessage("  Aucune compétence apprise !");
            return null;
        }
        menu.displayMessage("\n--- Compétences de " + attacker.getName() + " (0. Retour) ---");
        for (int i = 0; i < skills.size(); i++) {
            Skill s = skills.get(i);
            menu.displayMessage((i + 1) + ". " + s.getName() + " (Coût : " + s.getCost() + " " + attacker.getResourceName() + ") - " + s.getDescription());
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

    public Character askMonsterTarget(List<Character> monsters, Menu menu) {
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

    public Character askAllyToHeal(Team team, Menu menu) {
        menu.displayMessage("\nChoisissez le coéquipier à soigner (0. Retour) :");
        for (int i = 0; i < team.getMembers().size(); i++) {
            Character c = team.getMembers().get(i);
            menu.displayMessage((i + 1) + ". " + c.getName() + " | PV: " + Math.max(0, c.getHealthPoint()));
        }
        while (true) {
            int choice = menu.askPlayerInt();
            if (choice == 0) return null;
            if (choice > 0 && choice <= team.getMembers().size()) {
                return team.getMembers().get(choice - 1);
            }
            menu.displayMessage("[Erreur] Coéquipier invalide (ou 0 pour annuler).");
        }
    }
}
