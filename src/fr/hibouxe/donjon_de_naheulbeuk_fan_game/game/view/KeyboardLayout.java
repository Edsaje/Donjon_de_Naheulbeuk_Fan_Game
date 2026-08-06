package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import com.badlogic.gdx.Input;
import java.util.Locale;

/**
 * Gestionnaire des dispositions de claviers pour l'exploration 3D HD-2D.
 * Adapte les codes de touches physiques GLFW/LWJGL3 pour les claviers AZERTY et QWERTY.
 *
 * @author Hibouxe
 * @version 2.0
 */
public enum KeyboardLayout {
    AZERTY,
    QWERTY;

    /**
     * Renvoie le code de touche LWJGL3 pour le déplacement vers le Nord.
     * Sur clavier AZERTY, la touche avec la lettre 'Z' imprimée correspond au code physique Input.Keys.W dans GLFW.
     *
     * @return Code de touche LWJGL3
     */
    public int getUpKey() {
        return (this == AZERTY) ? Input.Keys.W : Input.Keys.W;
    }

    /**
     * Renvoie le code de touche LWJGL3 pour le déplacement vers l'Ouest.
     * Sur clavier AZERTY, la touche avec la lettre 'Q' imprimée correspond au code physique Input.Keys.A dans GLFW.
     *
     * @return Code de touche LWJGL3
     */
    public int getLeftKey() {
        return (this == AZERTY) ? Input.Keys.A : Input.Keys.A;
    }

    /**
     * Détecte automatiquement la disposition recommandée selon la configuration OS de l'utilisateur.
     *
     * @return AZERTY pour les systèmes francophones (FR, BE), QWERTY pour les autres.
     */
    public static KeyboardLayout detectSystemLayout() {
        String lang = Locale.getDefault().getLanguage().toLowerCase();
        String country = Locale.getDefault().getCountry().toUpperCase();
        if (lang.equals("fr") || country.equals("FR") || country.equals("BE")) {
            return AZERTY;
        }
        return QWERTY;
    }
}
