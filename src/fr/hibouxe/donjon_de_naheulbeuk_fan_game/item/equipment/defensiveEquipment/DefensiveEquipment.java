package fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.defensiveEquipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.Equipment;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentCategory;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.item.equipment.EquipmentSlot;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.entity.Character;

/**
 * Modèle pour les équipements défensifs (Armures, Boucliers, Casques, Robes, Pagnes).
 *
 * @author Hibouxe
 * @version 1.0
 */
public class DefensiveEquipment extends Equipment {

    /**
     * Constructeur d'équipement défensif universel.
     *
     * @param name              Nom de l'équipement
     * @param description       Description
     * @param slot              Emplacement d'équipement
     * @param category          Catégorie d'équipement
     * @param defenseBonus      Bonus de défense physique
     * @param magicDefenseBonus Bonus de défense magique
     */
    public DefensiveEquipment(String name, String description, EquipmentSlot slot, EquipmentCategory category, int defenseBonus, int magicDefenseBonus) {
        super(name, description, slot, category, 0, 0, defenseBonus, magicDefenseBonus, 0);
    }

    /**
     * Constructeur d'équipement défensif restreint à une classe de héros.
     *
     * @param name              Nom de l'équipement
     * @param description       Description
     * @param slot              Emplacement d'équipement
     * @param category          Catégorie d'équipement
     * @param requiredJob       Classe requise pour équiper
     * @param defenseBonus      Bonus de défense physique
     * @param magicDefenseBonus Bonus de défense magique
     */
    public DefensiveEquipment(String name, String description, EquipmentSlot slot, EquipmentCategory category, String requiredJob, int defenseBonus, int magicDefenseBonus) {
        super(name, description, slot, category, requiredJob, 0, 0, defenseBonus, magicDefenseBonus, 0);
    }

    @Override
    public boolean use(Character target) {
        return target.equip(this);
    }
}