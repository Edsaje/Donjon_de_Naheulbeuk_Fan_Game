package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.playerClasses.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère la Compagnie de Naheulbeuk (l'équipe des héros).
 * Maintient la liste des aventuriers ainsi que la position globale de la compagnie (X, Y) dans le donjon.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Team {
    private int x = 0; // Position X du joueur (départ en 0)
    private int y = 0; // Position Y du joueur (départ en 0)
    private List<Character> members = new ArrayList<>();
    private List<Item> inventory = new ArrayList<>();
    private int maxCapacity = 10;

    /**
     * Initialise l'équipe en ajoutant tous les membres iconiques de la Compagnie de Naheulbeuk :
     * Le Ranger, le Nain, l'Élfette, le Barbare, la Magicienne, l'Ogre et le Voleur.
     */
    public Team() {
        members.add(new Ranger());
        members.add(new Dwarf());
        members.add(new Elf());
        members.add(new Barbarian());
        members.add(new Magician());
        members.add(new Ogre());
        members.add(new Thief());
    }

    /**
     * Ajoute un objet au sac à dos de la compagnie s'il reste de la place.
     *
     * @param item L'objet à ajouter dans l'inventaire
     * @return true si l'objet a été ajouté, false si le sac est plein.
     */
    public boolean addItem(Item item) {
        if (this.inventory.size() < maxCapacity) {
            this.inventory.add(item);
            return true;
        }
        return false;
    }

    /**
     * Retire un objet du sac à dos de la compagnie.
     *
     * @param item L'objet à retirer
     */
    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    /**
     * Déplace la compagnie d'une case vers le Nord (y diminue).
     */
    public void moveNorth() {
        this.y--;
    }

    /**
     * Déplace la compagnie d'une case vers le Sud (y augmente).
     */
    public void moveSouth() {
        this.y++;
    }

    /**
     * Déplace la compagnie d'une case vers l'Est (x augmente).
     */
    public void moveEast() {
        this.x++;
    }

    /**
     * Déplace la compagnie d'une case vers l'Ouest (x diminue).
     */
    public void moveWest() {
        this.x--;
    }

    /**
     * @return Position X actuelle de l'équipe
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
     * @return Position Y actuelle de l'équipe
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

    /**
     * @return Liste des membres de l'équipe
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
     * @return La liste des objets contenus dans le sac à dos
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * @param inventory Nouveau contenu du sac à dos
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
     * Sélectionne aléatoirement un membre vivant de la Compagnie en excluant le personnage spécifié.
     * Utile pour les attaques à rebond ou les tirs alliés.
     *
     * @param excludedMember Le personnage à exclure de la sélection
     * @return Un membre de l'équipe au hasard (ou null si aucun autre membre n'est vivant).
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
        java.util.Random random = new java.util.Random();
        return candidates.get(random.nextInt(candidates.size()));
    }
}
