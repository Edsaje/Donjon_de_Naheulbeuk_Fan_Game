package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.EnumMap;
import java.io.Serializable;

/**
 * Classe parente représentant une entité vivante du jeu (Héros, Monstre ou Boss).
 * Gère les caractéristiques fondamentales (points de vie, attaque, défense, ressources).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class Character implements Serializable {
    private static final long serialVersionUID = 1L;
    // Attributs
    protected String name;
    protected String type; // Classe du personnage
    protected int level;
    protected int maxHealthPoint;
    protected int healthPoint;
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

    protected Map<EquipmentSlot, Equipment> equipmentSlots = new EnumMap<>(EquipmentSlot.class);

    protected List<Skill> skills = new ArrayList<>();

    /**
     * Constructeur complet d'un personnage.
     *
     * @param name          Nom du personnage (ex: "Le Nain")
     * @param type          Type ou classe (ex: "Dwarf")
     * @param level         Niveau de départ
     * @param maxHealthPoint   Points de vie initiaux
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
        this.maxHealthPoint = healthPoint;
        this.healthPoint = healthPoint;
        this.maxResource = resourcePoint;
        this.currentResource = resourcePoint;
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

    public boolean gainXp(int amount) {
        this.xp += amount;
        boolean leveledUp = false;
        while (this.xp >= this.xpToNextLevel) {
            levelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    public void levelUp() {
        this.level++;
        this.xp -= this.xpToNextLevel; // On retire l'XP consommée
        this.xpToNextLevel = (int) (100 * Math.pow(this.level, 1.5)); // Le prochain niveau sera plus long à atteindre
        // (Les augmentations de stats se feront dans les sous-classes)
    }

    public boolean isBoss() {
        return "Boss".equalsIgnoreCase(this.type);
    }

    /**
     * @return Vitesse d'initiative en combat
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed Nouvelle vitesse d'initiative
     */
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
        for (Equipment eq : equipmentSlots.values()) {
            if (eq != null) bonus += eq.getMagicDefenseBonus();
        }
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
        for (Equipment eq : equipmentSlots.values()) {
            if (eq != null) bonus += eq.getMagicAttackBonus();
        }
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
        for (Equipment eq : equipmentSlots.values()) {
            if (eq != null) bonus += eq.getDefenseBonus();
        }
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
        for (Equipment eq : equipmentSlots.values()) {
            if (eq != null) bonus += eq.getAttackBonus();
        }
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

        equipmentSlots.put(equipment.getSlot(), equipment);

        return true;
    }

    public Map<EquipmentSlot, Equipment> getEquipments(){
        return equipmentSlots;
    }

    /**
     * Retire un équipement porté par le personnage et le remet dans le sac à dos de la compagnie.
     *
     * @param slot L'emplacement à déséquiper
     * @param team L'équipe de la compagnie
     * @param ConsoleMenu La vue principale (Injectée)
     * @return true si l'équipement a été retiré et remis dans le sac, false sinon.
     */
    public boolean unequip(EquipmentSlot slot, Team team) {
        Equipment tmp = equipmentSlots.get(slot);

        if (tmp == null) {
            return false;
        } else {
            if (team.addItem(tmp)) {
                equipmentSlots.remove(slot);
                return true;
            } else {
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
        return currentResource;
    }

    /**
     * @param resourcePoint Nouveau nombre de ressource
     */
    public void setResourcePoint(int resourcePoint) {
        this.currentResource = resourcePoint;
    }

    /**
     * @return Points de mana (Alias pour compatibilité)
     */
    public int getManaPoint() {
        return currentResource;
    }

    /**
     * @param resourcePoint Nouveaux points de mana / ressource
     */
    public void setManaPoint(int resourcePoint) {
        this.currentResource = resourcePoint;
    }

    /**
     * @return Points de vie actuels
     */
    public int getHealthPoint() {
        return healthPoint;
    }

    /**
     * @return Points de vie max
     */
    public int getMaxHealthPoint() {
        return maxHealthPoint > 0 ? maxHealthPoint : healthPoint;
    }

    /**
     * @param healthPoint Nouveaux points de vie
     */
    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
        if (this.healthPoint < 0) {
            this.healthPoint = 0;
        }
        if (this.maxHealthPoint > 0 && this.healthPoint > this.maxHealthPoint) {
            this.healthPoint = this.maxHealthPoint;
        }
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

    /**
     * @return Quantite actuelle de ressource (Mana/Rage/Energie)
     */
    public int getCurrentResource() {
        return currentResource;
    }

    /**
     * @param currentResource Nouvelle quantite de ressource
     */
    public void setCurrentResource(int currentResource) {
        this.currentResource = currentResource;
    }

    /**
     * @return Points d'experience accumules
     */
    public int getXp() {
        return xp;
    }

    /**
     * @param xp Nouveaux points d'experience
     */
    public void setXp(int xp) {
        this.xp = xp;
    }

    /**
     * @return Points d'experience requis pour le prochain niveau
     */
    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    /**
     * @param xpToNextLevel Seuil d'experience requis
     */
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
                ", manaPoint=" + currentResource +
                ", attack=" + attack +
                ", magicAttack=" + magicAttack +
                ", defense=" + defense +
                ", magicDefense=" + magicDefense +
                '}';
    }
}
