package fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.Menu;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;

import java.util.List;
import java.util.ArrayList;


/**
 * Classe parente représentant une entité vivante du jeu (Héros, Monstre ou Boss).
 * Gère les caractéristiques fondamentales (points de vie, attaque, défense, ressources).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Character {
    // Attributs
    protected String name;
    protected String type; // Classe du personnage
    protected int level;
    protected int healthPoint;
    protected int resourcePoint;
    protected int attack;
    protected int magicAttack;
    protected int defense;
    protected int magicDefense;
    protected int speed;
    protected String resourceName; // "Mana", "Rage" ou "Énergie"
    protected int currentResource; // Valeur actuelle (ex: 10)
    protected int maxResource;     // Valeur maximale (ex: 20)

    protected int xp = 0;
    protected int xpToNextLevel = 100; //palier initial

    protected Equipment headSlot = null;
    protected Equipment chestSlot = null;
    protected Equipment legsSlot = null;
    protected Equipment jewelrySlot = null;
    protected Equipment weaponSlot = null;
    protected Equipment leftHandSlot = null;

    protected List<Skill> skills = new ArrayList<>();

    /**
     * Constructeur complet d'un personnage.
     *
     * @param name          Nom du personnage (ex: "Le Nain")
     * @param type          Type ou classe (ex: "Dwarf")
     * @param level         Niveau de départ
     * @param healthPoint   Points de vie initiaux
     * @param resourcePoint Points de ressource (Mana / Énergie / Rage)
     * @param attack        Puissance d'attaque physique
     * @param magicAttack   Puissance d'attaque magique
     * @param defense       Défense physique
     * @param magicDefense  Défense magique
     */
    public Character(String name, String type, int level, int healthPoint, int resourcePoint, int attack, int magicAttack, int defense, int magicDefense, int speed) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.healthPoint = healthPoint;
        this.resourcePoint = resourcePoint;
        this.attack = attack;
        this.magicAttack = magicAttack;
        this.defense = defense;
        this.magicDefense = magicDefense;
        this.speed = speed;
    }

    /**
     * Exécute la compétence spéciale choisie.
     * Cette méthode est destinée à être redéfinie dans les sous-classes.
     */
    public String useSpecialSkill(Skill skill, Team team, Character monster) {
        return this.name + " ne sait pas comment utiliser " + skill.getName() + " !";
    }

    public List<Skill> getSkills() {
        return skills;
    }

    /**
     * Ajoute de la ressource au personnage (ex : Rage, Energie)
     * Cette méthode est destinée à être redéfinie dans les sous-classes.
     *
     * @param amount
     */
    public void addResource(int amount) {
        this.currentResource = Math.min(this.maxResource, this.currentResource + amount);
    }

    public void gainXp(int amount, Menu menu) {
        this.xp += amount;
        while (this.xp >= this.xpToNextLevel) {
            levelUp(menu);
        }
    }

    public void levelUp(Menu menu) {
        this.level++;
        this.xp -= this.xpToNextLevel; // On retire l'XP consommée
        this.xpToNextLevel = (int) (100 * Math.pow(this.level, 1.5)); // Le prochain niveau sera plus long à atteindre
        // (Les augmentations de stats se feront dans les sous-classes)
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return Nom du personnage
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Nouveau nom
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Valeur de défense magique totale (base + équipements)
     */
    public int getMagicDefense() {
        int bonus = 0;
        if (headSlot != null) bonus += headSlot.getMagicDefenseBonus();
        if (chestSlot != null) bonus += chestSlot.getMagicDefenseBonus();
        if (legsSlot != null) bonus += legsSlot.getMagicDefenseBonus();
        if (jewelrySlot != null) bonus += jewelrySlot.getMagicDefenseBonus();
        if (weaponSlot != null) bonus += weaponSlot.getMagicDefenseBonus();
        if (leftHandSlot != null) bonus += leftHandSlot.getMagicDefenseBonus();
        return magicDefense + bonus;
    }

    /**
     * @param magicDefense Nouvelle valeur de défense magique
     */
    public void setMagicDefense(int magicDefense) {
        this.magicDefense = magicDefense;
    }

    /**
     * @return Valeur d'attaque magique totale (base + équipements)
     */
    public int getMagicAttack() {
        int bonus = 0;
        if (headSlot != null) bonus += headSlot.getMagicAttackBonus();
        if (chestSlot != null) bonus += chestSlot.getMagicAttackBonus();
        if (legsSlot != null) bonus += legsSlot.getMagicAttackBonus();
        if (jewelrySlot != null) bonus += jewelrySlot.getMagicAttackBonus();
        if (weaponSlot != null) bonus += weaponSlot.getMagicAttackBonus();
        if (leftHandSlot != null) bonus += leftHandSlot.getMagicAttackBonus();
        return magicAttack + bonus;
    }

    /**
     * @param magicAttack Nouvelle valeur d'attaque magique
     */
    public void setMagicAttack(int magicAttack) {
        this.magicAttack = magicAttack;
    }

    /**
     * @return Valeur de défense physique totale (base + équipements)
     */
    public int getDefense() {
        int bonus = 0;
        if (headSlot != null) bonus += headSlot.getDefenseBonus();
        if (chestSlot != null) bonus += chestSlot.getDefenseBonus();
        if (legsSlot != null) bonus += legsSlot.getDefenseBonus();
        if (jewelrySlot != null) bonus += jewelrySlot.getDefenseBonus();
        if (weaponSlot != null) bonus += weaponSlot.getDefenseBonus();
        if (leftHandSlot != null) bonus += leftHandSlot.getDefenseBonus();
        return defense + bonus;
    }

    /**
     * @param defense Nouvelle valeur de défense physique
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * @return Valeur d'attaque physique totale (base + équipements)
     */
    public int getAttack() {
        int bonus = 0;
        if (headSlot != null) bonus += headSlot.getAttackBonus();
        if (chestSlot != null) bonus += chestSlot.getAttackBonus();
        if (legsSlot != null) bonus += legsSlot.getAttackBonus();
        if (jewelrySlot != null) bonus += jewelrySlot.getAttackBonus();
        if (weaponSlot != null) bonus += weaponSlot.getAttackBonus();
        if (leftHandSlot != null) bonus += leftHandSlot.getAttackBonus();
        return attack + bonus;
    }

    /**
     * Tente d'équiper un objet dans l'emplacement dédié du personnage.
     *
     * @param equipment L'équipement à porter
     * @return true si l'objet a été équipé avec succès, false sinon.
     */
    public boolean equip(Equipment equipment) {
        if (equipment == null) return false;

        if (!equipment.canBeEquippedBy(this)) {
            return false;
        }

        switch (equipment.getSlot()) {
            case HEAD:
                this.headSlot = equipment;
                break;
            case CHEST:
                this.chestSlot = equipment;
                break;
            case LEGS:
                this.legsSlot = equipment;
                break;
            case JEWELRY:
                this.jewelrySlot = equipment;
                break;
            case WEAPON:
                this.weaponSlot = equipment;
                break;
            case LEFT_HAND:
                this.leftHandSlot = equipment;
                break;
        }

        return true;
    }

    public String getEquippedSummary(){
        List<String> items = new ArrayList<>();

        if (weaponSlot != null) items.add("Arme: " + weaponSlot.getName());
        if (leftHandSlot != null) items.add("Main gauche: " + leftHandSlot.getName());
        if (headSlot != null) items.add("Tête: " + headSlot.getName());
        if (chestSlot != null) items.add("Torse: " + chestSlot.getName());
        if (legsSlot != null) items.add("Jambes: " + legsSlot.getName());
        if (jewelrySlot != null) items.add("Bijou: " + jewelrySlot.getName());

        if (items.isEmpty()){
            return "Aucun équipement. EXHIBITIONNISTE !";
        }
        return String.join(", ", items);
    }

    /**
     * Retire un équipement porté par le personnage et le remet dans le sac à dos de la compagnie.
     *
     * @param slot L'emplacement à déséquiper
     * @param team L'équipe de la compagnie
     * @param menu La vue principale (Injectée)
     * @return true si l'équipement a été retiré et remis dans le sac, false sinon.
     */
    public boolean unequip(EquipmentSlot slot, Team team, Menu menu){
        Equipment tmp = null;

        switch (slot){

            case HEAD:
                tmp = this.headSlot;
                this.headSlot = null;
                break;
            case CHEST:
                tmp = this.chestSlot;
                this.chestSlot = null;
                break;
            case LEGS:
                tmp = this.legsSlot;
                this.legsSlot = null;
                break;
            case JEWELRY:
                tmp = this.jewelrySlot;
                this.jewelrySlot = null;
                break;
            case WEAPON:
                tmp = this.weaponSlot;
                this.weaponSlot = null;
                break;
            case LEFT_HAND:
                tmp = this.leftHandSlot;
                this.leftHandSlot = null;
                break;
        }

        if (tmp == null){
            menu.displayMessage("Aucun équipement d'équipé à cet endroit !");
            return false;
        } else {
            if (team.addItem(tmp)){
                menu.displayMessage(getName() + " retire " + tmp.getName() + "et le remet dans le sac Saldur de la compagnie !");
                return true;
            } else {
                equip(tmp);
                menu.displayMessage("Le sac est plein ! Impossible de déséquiper " + tmp.getName());
                return false;
            }
        }
    }


    /**
     * @param attack Nouvelle valeur d'attaque physique
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * @return Points de ressource (Générique)
     */
    public int getResourcePoint() {
        return resourcePoint;
    }

    /**
     * @param resourcePoint Nouveau nombre de ressource
     */
    public void setResourcePoint(int resourcePoint) {
        this.resourcePoint = resourcePoint;
    }

    /**
     * @return Points de mana (Alias pour compatibilité)
     */
    public int getManaPoint() {
        return resourcePoint;
    }

    /**
     * @param resourcePoint Nouveaux points de mana / ressource
     */
    public void setManaPoint(int resourcePoint) {
        this.resourcePoint = resourcePoint;
    }

    /**
     * @return Points de vie actuels
     */
    public int getHealthPoint() {
        return healthPoint;
    }

    /**
     * @param healthPoint Nouveaux points de vie
     */
    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    /**
     * @return Type / Classe du personnage
     */
    public String getType() {
        return type;
    }

    /**
     * @param type Nouveau type / classe
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Niveau actuel
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level Nouveau niveau
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return Nom de la ressource
     */
    public String getResourceName() {
        return resourceName;
    }


    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getMaxResource() {
        return maxResource;
    }

    public void setMaxResource(int maxResource) {
        this.maxResource = maxResource;
    }

    public int getCurrentResource() {
        return currentResource;
    }

    public void setCurrentResource(int currentResource) {
        this.currentResource = currentResource;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public void setXpToNextLevel(int xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    /**
     * Retourne l'état sous forme textuelle de la ressource (ex: "Mana: 10/10").
     *
     * @return Statut formate de la ressource
     */
    public String getResourceStatus() {
        return resourceName + ": " + currentResource + "/" + maxResource;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                ", healthPoint=" + healthPoint +
                ", manaPoint=" + resourcePoint +
                ", attack=" + attack +
                ", magicAttack=" + magicAttack +
                ", defense=" + defense +
                ", magicDefense=" + magicDefense +
                '}';
    }
}
