package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;

/**
 * Composant de rendu 2D spécialisé pour l'Interface Utilisateur (HUD) et le ConsoleMenu Interactif Dragon Quest (SRP).
 * Ouvre une fenêtre bleue à bordure dorée (Sac, Sorts, Équipement, Carte, Sauvegarde) à la pression de 'M' ou 'ECHAP'.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class HUDRenderer implements Disposable {
    private SpriteBatch uiBatch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private boolean isMenuOpen = false;
    private int selectedOption = 0;
    private String[] menuOptions = {
            "Status", "Sac", "Équipement", "Magie", "Sauvegarder", "Fermer"
    };

    public HUDRenderer() {
        uiBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        shapeRenderer = new ShapeRenderer();
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    /**
     * Effectue le rendu 2D du HUD et du ConsoleMenu Dragon Quest en superposition.
     *
     * @param dungeon Donjon actuel
     * @param playerX Position X du joueur
     * @param playerY Position Y du joueur
     * @param currentFloor Étage actuel
     * */
    public void renderHUD(Dungeon dungeon, int playerX, int playerY, int currentFloor, HD2DGameApp.GameState state, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<String> messages, String menuTitle, String[] menuOptions, HD2DGameApp gameApp) {
        handleMenuInput(state);
        if (menuTitle != null && menuOptions != null) {
            handleContextualMenuInput(menuOptions, gameApp);
        }

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);

        if (state == HD2DGameApp.GameState.BATTLE || state == HD2DGameApp.GameState.HUB) {
            renderMinimalistWindow(team, messages);
            if (menuTitle != null && menuOptions != null) {
                renderContextualMenu(menuTitle, menuOptions);
            }
        } else if (isMenuOpen) {
            renderDragonQuestWindow(dungeon, playerX, playerY, team);
        } else {
            renderExplorationHUD(dungeon, playerX, playerY, currentFloor, state);
        }
    }

    private int contextMenuSelection = 0;

    private void handleContextualMenuInput(String[] options, HD2DGameApp gameApp) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.Z) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            contextMenuSelection = (contextMenuSelection - 1 + options.length) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            contextMenuSelection = (contextMenuSelection + 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (gameApp.parentView != null) {
                gameApp.parentView.pushInput(String.valueOf(contextMenuSelection + 1));
            }
            contextMenuSelection = 0; // Reset for next time
        } else {
            // Check number keys
            for (int i = 1; i <= options.length; i++) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0 + i) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0 + i)) {
                    if (gameApp.parentView != null) {
                        gameApp.parentView.pushInput(String.valueOf(i));
                    }
                    contextMenuSelection = 0;
                    break;
                }
            }
        }
    }

    private void renderContextualMenu(String title, String[] options) {
        int menuWidth = 400;
        int menuHeight = 60 + options.length * 40;
        int x = 800; // Bottom right area
        int y = 50;
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Fenêtre Noire
        shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.1f, 0.95f));
        shapeRenderer.rect(x, y, menuWidth, menuHeight);
        
        // Bordure blanche fine
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rectLine(x, y, x + menuWidth, y, 2);
        shapeRenderer.rectLine(x, y + menuHeight, x + menuWidth, y + menuHeight, 2);
        shapeRenderer.rectLine(x, y, x, y + menuHeight, 2);
        shapeRenderer.rectLine(x + menuWidth, y, x + menuWidth, y + menuHeight, 2);

        // Curseur
        if (contextMenuSelection < options.length) {
            int cursorY = y + menuHeight - 80 - contextMenuSelection * 40;
            shapeRenderer.setColor(new Color(0.3f, 0.3f, 0.3f, 0.8f));
            shapeRenderer.rect(x + 10, cursorY - 5, menuWidth - 20, 30);
        }
        
        shapeRenderer.end();

        uiBatch.begin();
        font.setColor(Color.GOLD);
        font.draw(uiBatch, title, x + 25, y + menuHeight - 20);

        font.setColor(Color.WHITE);
        for (int i = 0; i < options.length; i++) {
            String prefix = (i == contextMenuSelection) ? "> " : "  ";
            font.draw(uiBatch, prefix + options[i], x + 35, y + menuHeight - 60 - i * 40);
        }
        uiBatch.end();
    }

    private void handleMenuInput(HD2DGameApp.GameState state) {
        if (state == HD2DGameApp.GameState.BATTLE || state == HD2DGameApp.GameState.HUB) return; // Désactivé en combat et HubController

        // Touche 'M' ou 'ECHAP' pour ouvrir/fermer le ConsoleMenu
        if (Gdx.input.isKeyJustPressed(Input.Keys.M) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isMenuOpen = !isMenuOpen;
            return;
        }

        if (isMenuOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.Z) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                selectedOption = (selectedOption + 1) % menuOptions.length;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (selectedOption == 5) {
                    isMenuOpen = false; // Option 6 : Fermer
                }
            }
        }
    }

    private void renderExplorationHUD(Dungeon dungeon, int playerX, int playerY, int currentFloor, HD2DGameApp.GameState state) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (state == HD2DGameApp.GameState.EXPLORATION && dungeon != null) {
            renderMinimap(dungeon, playerX, playerY);
        }

        shapeRenderer.end();

        uiBatch.begin();
        font.setColor(Color.WHITE);
        font.draw(uiBatch, "ÉTAGE " + currentFloor, 20, 700);
        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[M/ECHAP: ConsoleMenu]", 1100, 40);
        uiBatch.end();
    }

    private void renderMinimalistWindow(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<String> messages) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.1f, 0.85f));
        shapeRenderer.rect(100, 50, 1080, 250);
        shapeRenderer.end();

        uiBatch.begin();
        font.setColor(Color.WHITE);
        int startY = 270;
        int maxLines = 8;
        int startIdx = Math.max(0, messages.size() - maxLines);

        for (int i = startIdx; i < messages.size(); i++) {
            font.draw(uiBatch, messages.get(i), 120, startY);
            startY -= 25;
        }

        // --- STATISTIQUES DE L'ÉQUIPE (Droite de la boîte) ---
        if (team != null) {
            font.setColor(Color.LIGHT_GRAY);
            int teamY = 270;
            for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character hero : team.getMembers()) {
                String line = String.format("%-15s : PV: %d/%d | %s: %d/%d",
                        hero.getName(),
                        hero.getHealthPoint(), hero.getMaxHealthPoint(), 
                        hero.getResourceName(), hero.getCurrentResource(), hero.getMaxResource());
                font.draw(uiBatch, line, 650, teamY);
                teamY -= 25;
            }
        }

        uiBatch.end();
    }

    private void renderDragonQuestWindow(Dungeon dungeon, int playerX, int playerY, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fenêtre Noire épurée (Remplacement du bleu/doré)
        shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.1f, 0.9f));
        shapeRenderer.rect(180, 90, 920, 540);
        
        // Bordure
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rectLine(180, 90, 1100, 90, 3);
        shapeRenderer.rectLine(180, 630, 1100, 630, 3);
        shapeRenderer.rectLine(180, 90, 180, 630, 3);
        shapeRenderer.rectLine(1100, 90, 1100, 630, 3);

        // Séparateur vertical
        shapeRenderer.rectLine(500, 100, 500, 620, 2);

        // Curseur de sélection (Gris sombre)
        int cursorY = 520 - selectedOption * 55;
        shapeRenderer.setColor(new Color(0.25f, 0.25f, 0.25f, 0.8f));
        shapeRenderer.rect(210, cursorY - 5, 275, 40);

        shapeRenderer.end();

        // 4. Textes du ConsoleMenu et Statistiques de la Compagnie
        uiBatch.begin();
        font.draw(uiBatch, "ConsoleMenu", 400, 680);

        // Rendu des choix du ConsoleMenu à gauche
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                font.setColor(Color.WHITE);
                font.draw(uiBatch, "> " + menuOptions[i], 220, 520 - i * 55);
            } else {
                font.setColor(Color.LIGHT_GRAY);
                font.draw(uiBatch, "  " + menuOptions[i], 220, 520 - i * 55);
            }
        }

        // Rendu du statut de l'équipe à droite
        font.setColor(Color.WHITE);
        if (team != null && !team.getMembers().isEmpty()) {
            font.draw(uiBatch, "COMPAGNIE", 540, 580);
            font.setColor(Color.LIGHT_GRAY);
            int startY = 520;
            for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character hero : team.getMembers()) {
                String line = String.format("%-15s : Nv %d | PV: %d/%d  | %s: %d/%d",
                        hero.getName(), hero.getLevel(),
                        hero.getHealthPoint(), hero.getMaxHealthPoint(),
                        hero.getResourceName(), hero.getCurrentResource(), hero.getMaxResource());
                font.draw(uiBatch, line, 540, startY);
                startY -= 50;
            }
        } else {
            font.draw(uiBatch, "--- COMPAGNIE (VIDE) ---", 540, 580);
        }

        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[Utilise Z/W/S ou Fleches pour naviguer, ENTREE pour selectionner]", 540, 150);

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
