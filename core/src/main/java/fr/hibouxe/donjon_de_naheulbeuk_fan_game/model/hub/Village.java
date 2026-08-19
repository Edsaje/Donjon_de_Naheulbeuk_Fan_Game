package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.hub;

import java.io.Serializable;

/**
 * Modele de donnees pour le Village (Hub).
 * Gere l'evolution des batiments (Base Building).
 */
public class Village implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tavernLevel = 0; // 0 = Feu de camp, 1 = Tente, 2 = Taverne
    private int blacksmithLevel = 0; // 0 = Rien, 1 = Enclume
    private int merchantLevel = 0;

    public Village() {}

    public int getTavernLevel() { return tavernLevel; }
    public void setTavernLevel(int tavernLevel) { this.tavernLevel = tavernLevel; }

    public int getBlacksmithLevel() { return blacksmithLevel; }
    public void setBlacksmithLevel(int blacksmithLevel) { this.blacksmithLevel = blacksmithLevel; }

    public int getMerchantLevel() { return merchantLevel; }
    public void setMerchantLevel(int merchantLevel) { this.merchantLevel = merchantLevel; }
}
