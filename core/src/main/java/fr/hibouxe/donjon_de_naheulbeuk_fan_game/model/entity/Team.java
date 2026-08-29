package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.*;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 * Gre la Compagnie de Naheulbeuk (l'quipe des hros).
 * Maintient la liste des aventuriers ainsi que la position globale de la compagnie (X, Y) dans le donjon.
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x = 0; // Position X du joueur (dpart en 0)
    private int y = 0; // Position Y du joueur (dpart en 0)
    private float playerX = 0f;
    private float playerZ = 0f;
    private float moveSpeed = 1.5f;
    private int facingDirection = 0; // 0=Sud, 1=Nord, 2=Ouest, 3=Est
    private int activeLeaderIndex = 0; // 0=Ranger, 1=Nain, etc.
    private List<Character> members = new ArrayList<>();
    private List<Item> inventory = new ArrayList<>();
    private int maxCapacity = 10;
    private int gold = 0;
    private java.util.Map<String, Integer> hubUpgrades = new java.util.HashMap<>();
    private java.util.Map<String, Integer> hubChest = new java.util.HashMap<>();

    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider randomProvider;

    public Team() {
        this(new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.DefaultRandomProvider());
    }

    /**
     * Initialise l'quipe en ajoutant tous les membres iconiques de la Compagnie de Naheulbeuk :
     * Le Ranger, le Nain, l'lfette, le Barbare, la Magicienne, l'Ogre et le Voleur.
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
        
        // Equipement de depart selon le lore
        members.get(0).equip((fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment) fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.ItemFactory.createItem("epee_base"));
        members.get(1).equip((fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment) fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.ItemFactory.createItem("hache_deux_mains"));
        members.get(2).equip((fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment) fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.ItemFactory.createItem("arc_bois"));
        members.get(3).equip((fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment) fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.ItemFactory.createItem("grosse_epee"));
        members.get(4).equip((fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment) fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.ItemFactory.createItem("baton_magique"));
        // L'Ogre (index 5) n'a pas d'arme
        members.get(6).equip((fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment) fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.ItemFactory.createItem("dague_emoussee"));
        
        for (Character c : members) {
            c.setRandomProvider(randomProvider);
        }
    }

    /**
     * Ajoute un objet au sac  dos de la compagnie s'il reste de la place.
     *
     * @param item L'objet  ajouter dans l'inventaire
     * @return true si l'objet a t ajout, false si le sac est plein.
     */
    public boolean addItem(Item item) {
        if (this.inventory.size() < maxCapacity) {
            this.inventory.add(item);
            return true;
        }
        return false;
    }

    /**
     * Retire un objet du sac  dos de la compagnie.
     *
     * @param item L'objet  retirer
     */
    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    /**
     * Dplace la compagnie.
     */
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * @return Position X actuelle de l'quipe
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
     * @return Position Y actuelle de l'quipe
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

    public float getPlayerX() {
        return playerX;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    public float getPlayerZ() {
        return playerZ;
    }

    public void setPlayerZ(float playerZ) {
        this.playerZ = playerZ;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public int getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(int dir) { this.facingDirection = dir; }
    
    public int getGold() { return this.gold; }
    public void setGold(int gold) { this.gold = gold; }
    
    public java.util.Map<String, Integer> getHubUpgrades() { return hubUpgrades; }
    public void setHubUpgrades(java.util.Map<String, Integer> upgrades) { this.hubUpgrades = upgrades; }
    public int getHubUpgradeLevel(String key) { return hubUpgrades.getOrDefault(key, 0); }
    public void setHubUpgradeLevel(String key, int level) { hubUpgrades.put(key, level); }
    
    public java.util.Map<String, Integer> getHubChest() { return hubChest; }
    public void setHubChest(java.util.Map<String, Integer> chest) { this.hubChest = chest; }

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
     * @return Liste des membres de l'quipe
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
     * @return La liste des objets contenus dans le sac  dos
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * @param inventory Nouveau contenu du sac  dos
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
     * Slectionne alatoirement un membre vivant de la Compagnie en excluant le personnage spcifi.
     * Utile pour les attaques  rebond ou les tirs allis.
     *
     * @param excludedMember Le personnage  exclure de la slection
     * @return Un membre de l'quipe au hasard (ou null si aucun autre membre n'est vivant).
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

