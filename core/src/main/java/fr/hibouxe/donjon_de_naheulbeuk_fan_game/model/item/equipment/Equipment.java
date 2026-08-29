package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;

public class Equipment extends Item implements Equippable {
    protected EquipmentSlot slot;
    protected EquipmentCategory category;
    protected String requiredJob; //pour les objets exclusifs

    protected int attackBonus;
    protected int magicAttackBonus;
    protected int defenseBonus;
    protected int magicDefenseBonus;
    protected int maxHealthPointBonus;

    public Equipment(String name, String description, EquipmentSlot slot, EquipmentCategory category,
                     int attackBonus, int magicAttackBonus, int defenseBonus, int magicDefenseBonus, int maxHealthPointBonus) {
        super(name, description);
        this.slot = slot;
        this.category = category;
        this.requiredJob = null; //par dfaut
        this.attackBonus = attackBonus;
        this.magicAttackBonus = magicAttackBonus;
        this.defenseBonus = defenseBonus;
        this.magicDefenseBonus = magicDefenseBonus;
        this.maxHealthPointBonus = maxHealthPointBonus;
    }

    public Equipment(String name, String description, EquipmentSlot slot, EquipmentCategory category, String requiredJob,
                     int attackBonus, int magicAttackBonus, int defenseBonus, int magicDefenseBonus, int maxHealthPointBonus) {
        this(name, description, slot, category, attackBonus, magicAttackBonus, defenseBonus, magicDefenseBonus, maxHealthPointBonus);
        this.requiredJob = requiredJob;
    }

    public boolean canBeEquippedBy(Character hero) {
        // 1. Si l'objet est exclusif  un hros unique
        if (this.requiredJob != null) {
            return hero.getType().equalsIgnoreCase(this.requiredJob);
        }

        // 2. Sinon, vrification selon la catgorie d'quipement
        String job = hero.getType().toLowerCase();

        switch (this.category) {
            case HEAVY_ARMOR:
                return job.contains("nain") || job.contains("ranger");

            case LEATHER_ARMOR:
                return job.contains("ranger") || job.contains("voleur");

            case LIGHT_ARMOR:
                return job.contains("elfe") || job.contains("voleur");

            case WRAP_SKIRT:
                return job.contains("barbare") || job.contains("ogre");

            case CLOTH_ROBE:
                return job.contains("magicienne") || job.contains("elfe");

            case HEAVY_WEAPON:
                return job.contains("nain") || job.contains("barbare") || job.contains("ogre");

            case LIGHT_WEAPON:
                return !job.contains("ogre");

            case RANGE_WEAPON:
                return job.contains("ranger") || job.contains("voleur") || job.contains("elfe");

            case JEWELRY:
                return true;

            default:
                return true;
        }
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public int getMaxHealthPointBonus() {
        return maxHealthPointBonus;
    }

    public int getMagicDefenseBonus() {
        return magicDefenseBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getMagicAttackBonus() {
        return magicAttackBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public String getRequiredJob() {
        return requiredJob;
    }

    public EquipmentCategory getCategory() {
        return category;
    }
}
