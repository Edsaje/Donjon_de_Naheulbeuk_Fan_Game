package fr.hibouxe.donjon_de_naheulbeuk_fan_game.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.dungeon.Dungeon;

/**
 * Composant de rendu 2D spécialisé pour l'Interface Utilisateur (HUD) et le Menu Interactif Dragon Quest (SRP).
 * Ouvre une fenêtre bleue à bordure dorée (Sac, Sorts, Équipement, Carte, Sauvegarde) à la pression de 'M' ou 'ECHAP'.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class HUDRenderer implements Disposable {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch uiBatch;
    private BitmapFont font;

    private boolean isMenuOpen = false;
    private int selectedOption = 0;
    private String[] menuOptions = {
            "1. Sac & Inventaire",
            "2. Sorts & Magie",
            "3. Equipement",
            "4. Carte du Donjon",
            "5. Sauvegarder",
            "6. Fermer (ECHAP)"
    };

    public HUDRenderer() {
        shapeRenderer = new ShapeRenderer();
        uiBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.2f);
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    /**
     * Effectue le rendu 2D du HUD et du Menu Dragon Quest en superposition.
     *
     * @param dungeon Donjon actuel
     * @param playerX Position X du joueur
     * @param playerY Position Y du joueur
     * @param currentFloor Étage actuel
     * @param state État du jeu (EXPLORATION / BATTLE)
     */
    public void renderHUD(Dungeon dungeon, int playerX, int playerY, int currentFloor, HD2DGameApp.GameState state) {
        handleMenuInput();

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);

        if (isMenuOpen) {
            renderDragonQuestWindow(dungeon, playerX, playerY);
        } else {
            renderExplorationHUD(dungeon, playerX, playerY, currentFloor, state);
        }
    }

    private void handleMenuInput() {
        // Touche 'M' ou 'ECHAP' pour ouvrir/fermer le menu Dragon Quest
        if (Gdx.input.isKeyJustPressed(Input.Keys.M) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isMenuOpen = !isMenuOpen;
            System.out.println("=== MENU DRAGON QUEST : " + (isMenuOpen ? "OUVERT" : "FERMÉ") + " ===");
            return;
        }

        if (isMenuOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                selectedOption = (selectedOption + 1) % menuOptions.length;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (selectedOption == 5) {
                    isMenuOpen = false; // Option 6 : Fermer
                } else {
                    System.out.println("=== ACTION MENU SÉLECTIONNÉE : " + menuOptions[selectedOption] + " ===");
                }
            }
        }
    }

    private void renderExplorationHUD(Dungeon dungeon, int playerX, int playerY, int currentFloor, HD2DGameApp.GameState state) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Rendu de la Minimap 2D translucide en haut à droite pendant l'exploration
        if (state == HD2DGameApp.GameState.EXPLORATION && dungeon != null) {
            renderMinimap(dungeon, playerX, playerY);
        }

        shapeRenderer.end();

        uiBatch.begin();

        font.setColor(Color.GOLD);
        font.draw(uiBatch, "DONJON DE NAHEULBEUK - ÉTAGE " + currentFloor, 20, 700);

        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[Appuie sur 'M' ou 'ECHAP' pour ouvrir le Menu Dragon Quest]", 650, 40);

        if (state == HD2DGameApp.GameState.BATTLE) {
            font.setColor(Color.RED);
            font.draw(uiBatch, "=== BATAILLE EN COURS (MODE DRAGON QUEST) ===", 400, 700);
        }

        uiBatch.end();
    }

    private void renderDragonQuestWindow(Dungeon dungeon, int playerX, int playerY) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // 1. Fond sombre global translucide
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.7f));
        shapeRenderer.rect(0, 0, 1280, 720);

        // 2. Bordure Dorée extérieure de la Fenêtre Dragon Quest (Centre)
        shapeRenderer.setColor(new Color(0.85f, 0.7f, 0.2f, 0.95f));
        shapeRenderer.rect(180, 90, 920, 540);

        // 3. Fond Bleu Nuit intérieur (Bleu Royal Dragon Quest)
        shapeRenderer.setColor(new Color(0.05f, 0.08f, 0.28f, 0.95f));
        shapeRenderer.rect(186, 96, 908, 528);

        // Cadre séparateur pour le menu d'actions à gauche
        shapeRenderer.setColor(new Color(0.85f, 0.7f, 0.2f, 0.8f));
        shapeRenderer.rect(500, 110, 4, 500);

        // Curseur de sélection doré
        int cursorY = 490 - selectedOption * 55;
        shapeRenderer.setColor(new Color(0.9f, 0.8f, 0.2f, 0.5f));
        shapeRenderer.rect(210, cursorY - 5, 275, 40);

        shapeRenderer.end();

        // 4. Textes du Menu et Statistiques de la Compagnie
        uiBatch.begin();

        font.setColor(Color.GOLD);
        font.draw(uiBatch, "=== MENU DE LA COMPAGNIE DE NAHEULBEUK ===", 400, 680);

        // Rendu des choix du menu à gauche
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                font.setColor(Color.YELLOW);
                font.draw(uiBatch, "> " + menuOptions[i], 220, 520 - i * 55);
            } else {
                font.setColor(Color.WHITE);
                font.draw(uiBatch, "  " + menuOptions[i], 220, 520 - i * 55);
            }
        }

        // Rendu du statut de l'équipe à droite
        font.setColor(Color.GOLD);
        font.draw(uiBatch, "--- COMPAGNIE (7 HÉROS) ---", 540, 580);

        font.setColor(Color.WHITE);
        font.draw(uiBatch, "Le Ranger     : Nv 1 | PV: 50/50  | MP: 20/20", 540, 520);
        font.draw(uiBatch, "Le Nain       : Nv 1 | PV: 70/70  | MP: 0/0", 540, 470);
        font.draw(uiBatch, "La Magicienne : Nv 1 | PV: 35/35  | MP: 60/60", 540, 420);
        font.draw(uiBatch, "L'Elfe        : Nv 1 | PV: 40/40  | MP: 40/40", 540, 370);
        font.draw(uiBatch, "Le Barbare    : Nv 1 | PV: 65/65  | MP: 0/0", 540, 320);
        font.draw(uiBatch, "L'Ogre        : Nv 1 | PV: 90/90  | MP: 0/0", 540, 270);
        font.draw(uiBatch, "La Voleuse    : Nv 1 | PV: 45/45  | MP: 15/15", 540, 220);

        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[Utilise Z/S ou Fleches pour naviguer, ENTREE pour selectionner]", 540, 150);

        uiBatch.end();
    }

    private void renderMinimap(Dungeon dungeon, int playerX, int playerY) {
        int miniCellSize = 6;
        int originX = 1100;
        int originY = 550;

        Cell[][] grid = dungeon.getGrid();

        // Fond sombre translucide de la minimap
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.6f));
        shapeRenderer.rect(originX - 10, originY - 10, dungeon.getWidth() * miniCellSize + 20, dungeon.getHeight() * miniCellSize + 20);

        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                Cell cell = grid[x][y];
                int drawX = originX + x * miniCellSize;
                int drawY = originY + (dungeon.getHeight() - 1 - y) * miniCellSize;

                if (cell.isWalkable()) {
                    shapeRenderer.setColor(new Color(0.3f, 0.3f, 0.35f, 0.8f));
                    shapeRenderer.rect(drawX, drawY, miniCellSize - 1, miniCellSize - 1);

                    if (cell.hasMonster()) {
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.rect(drawX, drawY, miniCellSize - 1, miniCellSize - 1);
                    } else if (cell.hasItem()) {
                        shapeRenderer.setColor(Color.CYAN);
                        shapeRenderer.rect(drawX, drawY, miniCellSize - 1, miniCellSize - 1);
                    } else if (cell.hasStairs()) {
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.rect(drawX, drawY, miniCellSize - 1, miniCellSize - 1);
                    }
                }
            }
        }

        // Marqueur Joueur en Doré
        int playerDrawX = originX + playerX * miniCellSize;
        int playerDrawY = originY + (dungeon.getHeight() - 1 - playerY) * miniCellSize;
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(playerDrawX - 1, playerDrawY - 1, miniCellSize + 1, miniCellSize + 1);
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (uiBatch != null) uiBatch.dispose();
        if (font != null) font.dispose();
    }
}
