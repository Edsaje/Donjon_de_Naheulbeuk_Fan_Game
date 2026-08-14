package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Moteur de combat tour par tour entre la Compagnie de Naheulbeuk et un Ennemi.
 * Gère l'alternance des tours, le choix des attaques physiques et compétences magiques,
 * ainsi que l'IA ennemie et les conditions de victoire/défaite.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class BattleController {
    private Team team;
    private List<Character> monsters;
    private Random random = new Random();
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.ICombatView menu;

    /**
     * Initialise un nouvel affrontement entre l'équipe du joueur et des monstres (avec injection de la vue IGameView).
     *
     * @param team     La compagnie des héros
     * @param monsters Le groupe de monstres affronté
     * @param menu     La vue de combat injectée (ICombatView)
     */
    public BattleController(Team team, List<Character> monsters, fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.ICombatView menu) {
        this.team = team;
        this.monsters = monsters;
        this.menu = menu;
    }

    /**
     * Vérifie si au moins un monstre du groupe a des PV > 0.
     */
    private boolean areMonstersAlive() {
        if (monsters == null || monsters.isEmpty()) return false;
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) {
                return true;
            }
        }
        return false;
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
        while (areMonstersAlive() && isTeamAlive()) {

            playRound();

            if (!areMonstersAlive()) { // Vérification si les monstres sont vaincus
                if (monsters.isEmpty()) {
                    menu.displayDialogue("\nLe combat s'arrête là... (Fuite)");
                    menu.clearMessages();
                    return true;
                }

                menu.displayVictory();

                int totalXp = 0;
                for (Character m : monsters) { //on récupère l'xp des monstres
                    totalXp += m.getXp();
                }

                int aliveCount = 0;
                for (Character c : team.getMembers()) { //on compte les héro en vie
                    if (c.getHealthPoint() > 0) aliveCount++;
                }

                if (aliveCount > 0) {
                    int xpPerHero = totalXp / aliveCount;
                    if (xpPerHero > 0) {
                        menu.displayMessage("Chaque héro en vie reçoit " + xpPerHero + " points d'expérience !");
                    }

                    for (Character c : team.getMembers()) { //on donne l'xp aux vivants
                        if (c.getHealthPoint() > 0) {
                            boolean leveledUp = c.gainXp(xpPerHero);
                            if (leveledUp) {
                                menu.displayMessage(c.getName() + " passe au niveau " + c.getLevel() + " !");
                            }
                        }
                    }

                    for (Character c : team.getMembers()) { //on revive avec 1pv les persos morts en cas de vistoire
                        if (c.getHealthPoint() <= 0) {
                            c.setHealthPoint(1);
                            menu.displayMessage(c.getName() + " n'est plus inconscient !");
                        }
                    }
                    menu.displayDialogue("\nFin du combat ! Appuyez sur Entrée pour continuer.");
                    menu.clearMessages();
                }
                return true; // Victoire
            }

            if (!isTeamAlive()) { // Vérification si l'équipe est vaincue
                menu.displayDefeat();
                return false; // Game Over
            }
        }
        return false;
    }

    /**
     * Gère un round complet de combat (Tour global).
     * Trie tous les combattants par Vitesse (Speed) et les fait agir à tour de rôle.
     */
    private void playRound() {
        menu.displayBattleStatus(monsters, team);

        // 1. Régénération d'énergie au début du round
        for (Character c : team.getMembers()) {
            if ("Energie".equals(c.getResourceName()) && c.getHealthPoint() > 0) {
                c.addResource(20); // +20 Énergie par tour
            }
        }

        // 2. Création de la file d'attente (Initiative)
        List<Character> allCombatants = new ArrayList<>();
        for (Character c : team.getMembers()) {
            if (c.getHealthPoint() > 0) allCombatants.add(c);
        }
        for (Character m : monsters) {
            if (m.getHealthPoint() > 0) allCombatants.add(m);
        }

        // 3. Tri des combattants par Vitesse (du plus rapide au plus lent)
        allCombatants.sort((c1, c2) -> Integer.compare(c2.getSpeed(), c1.getSpeed()));

        // 4. Exécution des tours individuels
        for (Character combatant : allCombatants) {
            // Si le combatant meurt pendant le round avant son tour, il passe son tour
            if (combatant.getHealthPoint() <= 0) continue;
            
            // Si un des deux camps est entièrement mort pendant le round, on arrête le round
            if (!areMonstersAlive() || !isTeamAlive()) break;

            if (team.getMembers().contains(combatant)) {
                heroTurn(combatant); // Tour d'un Héros
            } else {
                monsterTurn(combatant); // Tour d'un Monstre
            }
        }
    }

    private void heroTurn(Character attacker) {
        menu.displayTurn(attacker.getName());
        boolean actionConfirmed = false;

        while (!actionConfirmed) {
            int action = menu.askBattleAction(attacker);
            
            boolean isTutorialBoss = false;
            for (Character m : monsters) {
                if (m.isBoss()) isTutorialBoss = true;
            }
            if (isTutorialBoss && action != 4) {
                menu.displayMessage("L'Orque est beaucoup trop puissant ! Vous devez FUIR !");
                continue;
            }

            if (action == 1) {
                Character target = menu.askMonsterTarget(monsters);
                if (target == null) {
                    continue; // L'utilisateur a annulé (Retour)
                }
                int damage = Math.max(1, attacker.getAttack() - target.getDefense()); // Calcul des dégâts
                target.setHealthPoint(target.getHealthPoint() - damage); // On retire les PV
                menu.displayMessage("\n" + attacker.getName() + " tape de toutes ses forces et inflige " + damage + " dégât(s) au " + target.getName() + " !");
                
                if (target.getHealthPoint() <= 0) {
                    menu.displayMessage("à Le " + target.getName() + " s'effondre sans vie !");
                } else {
                    menu.displayMessage("Il reste " + target.getHealthPoint() + " PV au " + target.getName() + " !");
                }

                if ("Rage".equals(attacker.getResourceName())) {
                    attacker.addResource(10); //+10 de Rage quand il frappe
                }
                actionConfirmed = true;

            } else if (action == 2) {
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Skill chosenSkill = menu.askSkill(attacker, monsters);
                if (chosenSkill == null) continue;

                Character target = null;
                if (chosenSkill.isHealing()) {
                    target = menu.askItemTarget(team);
                } else {
                    target = menu.askMonsterTarget(monsters);
                }

                if (target == null) {
                    continue; // L'utilisateur a annulé (Retour)
                }

                String actionText = attacker.useSpecialSkill(chosenSkill, team, target);
                menu.displayMessage("\n" + actionText);
                if (target.getHealthPoint() <= 0) {
                    menu.displayMessage("à Le " + target.getName() + " s'effondre sans vie !");
                }
                
                // Si la compétence n'a pas été lancée (ex: pas assez de mana), on ne valide pas le tour
                if (actionText.contains("n'a pas assez")) {
                    // On ne met pas actionConfirmed = true
                } else {
                    actionConfirmed = true;
                }

            } else if (action == 3) {
                menu.displayInventory(team);
                if (team.getInventory().isEmpty()) {
                    continue; // Rien dans le sac, on recommence
                }
                int itemIndex = menu.askItemIndex();
                if (itemIndex >= 0 && itemIndex < team.getInventory().size()) {
                    Item selectedItem = team.getInventory().get(itemIndex);
                    Character target = menu.askItemTarget(team);
                    
                    if (target == null) {
                        continue; // Cible annulée, on recommence
                    }
                    
                    boolean used = selectedItem.use(target);
                    if (used) {
                        team.removeItem(selectedItem);
                        menu.displayMessage("\n" + target.getName() + " utilise " + selectedItem.getName() + " !");
                        actionConfirmed = true;
                    } else {
                        menu.displayMessage("\n" + target.getName() + " ne peut pas utiliser ça !");
                        // On ne met pas actionConfirmed = true, il peut rechoisir
                    }
                }
            } else if (action == 4) {
                menu.displayMessage("\n" + attacker.getName() + " tente de fuir lâchement !");
                int fleeChance = isTutorialBoss ? 100 : random.nextInt(100);
                if (fleeChance > 50) {
                    menu.displayMessage("Fuite réussie ! Mais vous perdez un peu de dignité.");
                    monsters.clear(); // End the BattleController
                    return; // End the round
                } else {
                    menu.displayMessage("Échec de la fuite ! Les monstres vous bloquent le passage !");
                    actionConfirmed = true; // Tour perdu
                }
            }
        }
    }

    /**
     * Gère la riposte d'un monstre spécifique.
     */
    private void monsterTurn(Character m) {
        List<Character> aliveHeroes = new ArrayList<>(); // On cherche les membres vivants de la compagnie
        for (Character c : team.getMembers()) {
            if (c.getHealthPoint() > 0) {
                aliveHeroes.add(c);
            }
        }

        if (!aliveHeroes.isEmpty()) {
            Character target = aliveHeroes.get(random.nextInt(aliveHeroes.size())); // Cible au hasard pour ce monstre
            int damage = Math.max(1, m.getAttack() - target.getDefense()); // Calcul des dégâts
            target.setHealthPoint(target.getHealthPoint() - damage); // On retire les PV
            menu.displayMessage("\n⚔à Le " + m.getName() + " attaque " + target.getName() + " et inflige " + damage + " dégâts !");
            
            if (target.getHealthPoint() <= 0) {
                menu.displayMessage("à " + target.getName() + " est K.O. !");
            }

            if ("Rage".equals(target.getResourceName())) {
                target.addResource(15); //+15 de Rage quand un héros prend un coup
            }
        }
    }

}
