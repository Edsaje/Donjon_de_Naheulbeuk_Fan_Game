package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 * GÃ¨re la Compagnie de Naheulbeuk (l'Ã©quipe des hÃ©ros).
 * Maintient la liste des aventuriers ainsi que la position globale de la compagnie (X, Y) dans le donjon.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x = 0; // Position X du joueur (dÃ©part en 0)
    private int y = 0; // Position Y du joueur (dÃ©part en 0)
    private int facingDirection = 0; // 0=Sud, 1=Nord, 2=Ouest, 3=Est
    private int activeLeaderIndex = 0; // 0=Ranger, 1=Nain, etc.
    private List<Character> members = new ArrayList<>();
    private List<Item> inventory = new ArrayList<>();
    private int maxCapacity = 10;

    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider randomProvider;

    public Team() {
        this(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.DefaultRandomProvider());
    }

    /**
     * Initialise l'Ã©quipe en ajoutant tous les membres iconiques de la Compagnie de Naheulbeuk :
     * Le Ranger, le Nain, l'Ã‰lfette, le Barbare, la Magicienne, l'Ogre et le Voleur.
     */
    public Team(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider randomProvider) {
        this.randomProvider = randomProvider;
        members.add(new Ranger());
        members.add(new Dwarf());
        members.add(new Elf());
        members.add(new Barbarian());
        members.add(new Magician());
        members.add(new Ogre());
        members.add(new Thief());
        
        for (Character c : members) {
            c.setRandomProvider(randomProvider);
        }
    }

    /**
     * Ajoute un objet au sac Ã  dos de la compagnie s'il reste de la place.
     *
     * @param item L'objet Ã  ajouter dans l'inventaire
     * @return true si l'objet a Ã©tÃ© ajoutÃ©, false si le sac est plein.
     */
    public boolean addItem(Item item) {
        if (this.inventory.size() < maxCapacity) {
            this.inventory.add(item);
            return true;
        }
        return false;
    }

    /**
     * Retire un objet du sac Ã  dos de la compagnie.
     *
     * @param item L'objet Ã  retirer
     */
    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    /**
     * DÃ©place la compagnie.
     */
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * @return Position X actuelle de l'Ã©quipe
     */
    public int getX() {
        return x;
    }

    /**
     * @param x Nouvelle position X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Position Y actuelle de l'Ã©quipe
     */
    public int getY() {
        return y;
    }

    /**
     * @param y Nouvelle position Y
     */
    public void setY(int y) {
        this.y = y;
    }

    public int getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(int facingDirection) {
        this.facingDirection = facingDirection;
    }

    public int getActiveLeaderIndex() {
        return activeLeaderIndex;
    }

    public void setActiveLeaderIndex(int index) {
        if (index >= 0 && index < members.size()) {
            this.activeLeaderIndex = index;
        }
    }

    public Character getActiveLeader() {
        if (members.isEmpty()) return null;
        return members.get(activeLeaderIndex);
    }

    /**
     * @return Liste des membres de l'Ã©quipe
     */
    public List<Character> getMembers() {
        return members;
    }

    /**
     * @param members Nouvelle liste de membres
     */
    public void setMembers(List<Character> members) {
        this.members = members;
    }

    /**
     * @return La liste des objets contenus dans le sac Ã  dos
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * @param inventory Nouveau contenu du sac Ã  dos
     */
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * SÃ©lectionne alÃ©atoirement un membre vivant de la Compagnie en excluant le personnage spÃ©cifiÃ©.
     * Utile pour les attaques Ã  rebond ou les tirs alliÃ©s.
     *
     * @param excludedMember Le personnage Ã  exclure de la sÃ©lection
     * @return Un membre de l'Ã©quipe au hasard (ou null si aucun autre membre n'est vivant).
     */
    public Character getRandomMemberExcept(Character excludedMember) {
        List<Character> candidates = new ArrayList<>();
        for (Character c : members) {
            if (c != excludedMember && c.getHealthPoint() > 0) {
                candidates.add(c);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(this.randomProvider.nextInt(candidates.size()));
    }
}

