package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

/**
 * Contrat d'interface définissant le comportement d'un objet équipable.
 *
 * @author Hibouxe
 * @version 1.0
 */
public interface Equippable {
    /**
     * @return L'emplacement d'équipement réservé à cet objet
     */
    EquipmentSlot getSlot();

    /**
     * @return La catégorie d'équipement
     */
    EquipmentCategory getCategory();

    /**
     * Vérifie si un héros donné remplit les conditions pour porter cet équipement.
     *
     * @param hero Le héros testé
     * @return true si le héros peut équiper l'objet, false sinon.
     */
    boolean canBeEquippedBy(Character hero);
}
