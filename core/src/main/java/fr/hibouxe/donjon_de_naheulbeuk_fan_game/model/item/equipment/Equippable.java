package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

/**
 * Contrat d'interface dfinissant le comportement d'un objet quipable.
 *
 * @author Hibouxe
 * @version 1.0
 */
public interface Equippable {
    /**
     * @return L'emplacement d'quipement rserv  cet objet
     */
    EquipmentSlot getSlot();

    /**
     * @return La catgorie d'quipement
     */
    EquipmentCategory getCategory();

    /**
     * Vrifie si un hros donn remplit les conditions pour porter cet quipement.
     *
     * @param hero Le hros test
     * @return true si le hros peut quiper l'objet, false sinon.
     */
    boolean canBeEquippedBy(Character hero);
}
