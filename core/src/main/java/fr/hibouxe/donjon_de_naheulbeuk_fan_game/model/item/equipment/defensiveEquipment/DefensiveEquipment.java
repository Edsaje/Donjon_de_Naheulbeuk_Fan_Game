package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.defensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

/**
 * Modle pour les quipements dfensifs (Armures, Boucliers, Casques, Robes, Pagnes).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DefensiveEquipment extends Equipment {

    /**
     * Constructeur d'quipement dfensif universel.
     *
     * @param name              Nom de l'quipement
     * @param description       Description
     * @param slot              Emplacement d'quipement
     * @param category          Catgorie d'quipement
     * @param defenseBonus      Bonus de dfense physique
     * @param magicDefenseBonus Bonus de dfense magique
     */
    public DefensiveEquipment(String name, String description, EquipmentSlot slot, EquipmentCategory category, int defenseBonus, int magicDefenseBonus) {
        super(name, description, slot, category, 0, 0, defenseBonus, magicDefenseBonus, 0);
    }

    /**
     * Constructeur d'quipement dfensif restreint  une classe de hros.
     *
     * @param name              Nom de l'quipement
     * @param description       Description
     * @param slot              Emplacement d'quipement
     * @param category          Catgorie d'quipement
     * @param requiredJob       Classe requise pour quiper
     * @param defenseBonus      Bonus de dfense physique
     * @param magicDefenseBonus Bonus de dfense magique
     */
    public DefensiveEquipment(String name, String description, EquipmentSlot slot, EquipmentCategory category, String requiredJob, int defenseBonus, int magicDefenseBonus) {
        super(name, description, slot, category, requiredJob, 0, 0, defenseBonus, magicDefenseBonus, 0);
    }

    @Override
    public boolean use(Character target) {
        return target.equip(this);
    }
}