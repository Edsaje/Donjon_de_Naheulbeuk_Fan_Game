package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.components.StatComponent;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.components.EquipmentComponent;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.components.ProgressionComponent;
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
    
    protected StatComponent statComponent;
    protected EquipmentComponent equipmentComponent;
    protected ProgressionComponent progressionComponent;

    protected List<Skill> skills = new ArrayList<>();

    protected fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider randomProvider = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.DefaultRandomProvider();

    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider getRandomProvider() {
        return randomProvider;
    }

    public void setRandomProvider(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.random.IRandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    protected fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai.IMonsterTactics tactics;

    public fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai.IMonsterTactics getTactics() {
        return tactics;
    }

    public void setTactics(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.ai.IMonsterTactics tactics) {
        this.tactics = tactics;
    }

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
        this.statComponent = new StatComponent(healthPoint, healthPoint, attack, magicAttack, defense, magicDefense, speed, resourcePoint, resourcePoint, "");
        this.equipmentComponent = new EquipmentComponent();
        this.progressionComponent = new ProgressionComponent(level);
    }

    public StatComponent getStatComponent() { return statComponent; }
    public EquipmentComponent getEquipmentComponent() { return equipmentComponent; }
    public ProgressionComponent getProgressionComponent() { return progressionComponent; }

    /**
     * Exécute la compétence spéciale choisie.
     * Cette méthode est destinée à être redéfinie dans les sous-classes.
     */
    public SkillResult useSpecialSkill(Skill skill, Team team, Character monster) {
        return new SkillResult(false, 0, "ERROR");
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
        this.statComponent.setCurrentResource(Math.min(this.statComponent.getMaxResource(), this.statComponent.getCurrentResource() + amount));
    }

    public int gainXp(int amount) {
        return this.progressionComponent.gainXp(amount);
    }

    public int levelUp() {
        return this.progressionComponent.levelUp();
    }
    
    protected void increaseStat(StatType statName, int min, int max) {
        int gain = min + this.randomProvider.nextInt(max - min + 1);
        if (gain > 0) {
            if (statName == StatType.PV_MAX) { this.statComponent.setMaxHealthPoint(this.statComponent.getMaxHealthPoint() + gain); this.statComponent.setHealthPoint(this.statComponent.getHealthPoint() + gain); }
            else if (statName == StatType.PM_MAX) { this.statComponent.setMaxResource(this.statComponent.getMaxResource() + gain); this.statComponent.setCurrentResource(this.statComponent.getCurrentResource() + gain); }
            else if (statName == StatType.ATTAQUE) this.statComponent.setAttack(this.statComponent.getAttack() + gain);
            else if (statName == StatType.DEFENSE) this.statComponent.setDefense(this.statComponent.getDefense() + gain);
            else if (statName == StatType.ATTAQUE_MAGIQUE) this.statComponent.setMagicAttack(this.statComponent.getMagicAttack() + gain);
            else if (statName == StatType.DEFENSE_MAGIQUE) this.statComponent.setMagicDefense(this.statComponent.getMagicDefense() + gain);
            else if (statName == StatType.VITESSE) this.statComponent.setSpeed(this.statComponent.getSpeed() + gain);
        }
    }

    public boolean isBoss() {
        return "Boss".equalsIgnoreCase(this.type);
    }

    /**
     * @return Vitesse d'initiative en combat
     */
    public int getSpeed() {
        return this.statComponent.getSpeed();
    }

    /**
     * @param speed Nouvelle vitesse d'initiative
     */
    public void setSpeed(int speed) {
        this.statComponent.setSpeed(speed);
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
        for (Equipment eq : this.equipmentComponent.getEquipmentSlots().values()) {
            if (eq != null) bonus += eq.getMagicDefenseBonus();
        }
        return this.statComponent.getMagicDefense() + bonus;
    }

    /**
     * @param magicDefense Nouvelle valeur de défense magique
     */
    public void setMagicDefense(int magicDefense) {
        this.statComponent.setMagicDefense(magicDefense);
    }

    /**
     * @return Valeur d'attaque magique totale (base + équipements)
     */
    public int getMagicAttack() {
        int bonus = 0;
        for (Equipment eq : this.equipmentComponent.getEquipmentSlots().values()) {
            if (eq != null) bonus += eq.getMagicAttackBonus();
        }
        return this.statComponent.getMagicAttack() + bonus;
    }

    /**
     * @param magicAttack Nouvelle valeur d'attaque magique
     */
    public void setMagicAttack(int magicAttack) {
        this.statComponent.setMagicAttack(magicAttack);
    }

    /**
     * @return Valeur de défense physique totale (base + équipements)
     */
    public int getDefense() {
        int bonus = 0;
        for (Equipment eq : this.equipmentComponent.getEquipmentSlots().values()) {
            if (eq != null) bonus += eq.getDefenseBonus();
        }
        return this.statComponent.getDefense() + bonus;
    }

    /**
     * @param defense Nouvelle valeur de défense physique
     */
    public void setDefense(int defense) {
        this.statComponent.setDefense(defense);
    }

    /**
     * @return Valeur d'attaque physique totale (base + équipements)
     */
    public int getAttack() {
        int bonus = 0;
        for (Equipment eq : this.equipmentComponent.getEquipmentSlots().values()) {
            if (eq != null) bonus += eq.getAttackBonus();
        }
        return this.statComponent.getAttack() + bonus;
    }

    /**
     * Tente d'équiper un objet dans l'emplacement dédié du personnage.
     *
     * @param equipment L'équipement à porter
     * @return true si l'objet a été équipé avec succès, false sinon.
     */
    public boolean equip(Equipment equipment) {
        return this.equipmentComponent.equip(equipment, this);
    }

    public Map<EquipmentSlot, Equipment> getEquipments(){
        return this.equipmentComponent.getEquipmentSlots();
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
        return this.equipmentComponent.unequip(slot, team);
    }


    /**
     * @param attack Nouvelle valeur d'attaque physique
     */
    public void setAttack(int attack) {
        this.statComponent.setAttack(attack);
    }

    /**
     * @return Points de ressource (Générique)
     */
    public int getResourcePoint() {
        return this.statComponent.getCurrentResource();
    }

    /**
     * @param resourcePoint Nouveau nombre de ressource
     */
    public void setResourcePoint(int resourcePoint) {
        this.statComponent.setCurrentResource(resourcePoint);
    }

    /**
     * @return Points de mana (Alias pour compatibilité)
     */
    public int getManaPoint() {
        return this.statComponent.getCurrentResource();
    }

    /**
     * @param resourcePoint Nouveaux points de mana / ressource
     */
    public void setManaPoint(int resourcePoint) {
        this.statComponent.setCurrentResource(resourcePoint);
    }

    /**
     * @return Points de vie actuels
     */
    public int getHealthPoint() {
        return this.statComponent.getHealthPoint();
    }

    /**
     * @return Points de vie max
     */
    public int getMaxHealthPoint() {
        return this.statComponent.getMaxHealthPoint() > 0 ? this.statComponent.getMaxHealthPoint() : this.statComponent.getHealthPoint();
    }

    /**
     * @param healthPoint Nouveaux points de vie
     */
    public void setHealthPoint(int healthPoint) {
        this.statComponent.setHealthPoint(healthPoint);
        if (this.statComponent.getHealthPoint() < 0) {
            this.statComponent.setHealthPoint(0);
        }
        if (this.statComponent.getMaxHealthPoint() > 0 && this.statComponent.getHealthPoint() > this.statComponent.getMaxHealthPoint()) {
            this.statComponent.setHealthPoint(this.statComponent.getMaxHealthPoint());
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
        return this.progressionComponent.getLevel();
    }

    /**
     * @param level Nouveau niveau
     */
    public void setLevel(int level) {
        this.progressionComponent.setLevel(level);
    }

    /**
     * @return Nom de la ressource
     */
    public String getResourceName() {
        return this.statComponent.getResourceName();
    }


    public void setResourceName(String resourceName) {
        this.statComponent.setResourceName(resourceName);
    }

    public int getMaxResource() {
        return this.statComponent.getMaxResource();
    }

    public void setMaxResource(int maxResource) {
        this.statComponent.setMaxResource(maxResource);
    }

    /**
     * @return Quantite actuelle de ressource (Mana/Rage/Energie)
     */
    public int getCurrentResource() {
        return this.statComponent.getCurrentResource();
    }

    /**
     * @param currentResource Nouvelle quantite de ressource
     */
    public void setCurrentResource(int currentResource) {
        this.statComponent.setCurrentResource(currentResource);
    }

    /**
     * @return Points d'experience accumules
     */
    public int getXp() {
        return this.progressionComponent.getXp();
    }

    /**
     * @param xp Nouveaux points d'experience
     */
    public void setXp(int xp) {
        this.progressionComponent.setXp(xp);
    }

    /**
     * @return Points d'experience requis pour le prochain niveau
     */
    public int getXpToNextLevel() {
        return this.progressionComponent.getXpToNextLevel();
    }

    /**
     * @param xpToNextLevel Seuil d'experience requis
     */
    public void setXpToNextLevel(int xpToNextLevel) {
        this.progressionComponent.setXpToNextLevel(xpToNextLevel);
    }

    /**
     * Retourne l'état sous forme textuelle de la ressource (ex: "Mana: 10/10").
     *
     * @return Statut formate de la ressource
     */
    public String getResourceStatus() {
        return this.statComponent.getResourceName() + ": " + this.statComponent.getCurrentResource() + "/" + this.statComponent.getMaxResource();
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", level=" + this.progressionComponent.getLevel() +
                ", healthPoint=" + this.statComponent.getHealthPoint() +
                ", manaPoint=" + this.statComponent.getCurrentResource() +
                ", attack=" + this.statComponent.getAttack() +
                ", magicAttack=" + this.statComponent.getMagicAttack() +
                ", defense=" + this.statComponent.getDefense() +
                ", magicDefense=" + this.statComponent.getMagicDefense() +
                '}';
    }

    public TacticalRow getPreferredTacticalRow() {
        return TacticalRow.FRONTLINE;
    }
}
