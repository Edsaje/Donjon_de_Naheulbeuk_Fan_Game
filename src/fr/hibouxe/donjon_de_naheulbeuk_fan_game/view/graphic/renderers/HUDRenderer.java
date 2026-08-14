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
 * Ouvre une fenêtre bleue à bordure dorée (Sac, Sorts, équipement, Carte, Sauvegarde) à la pression de 'M' ou 'ECHAP'.
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
            "Status", "Sac", "équipement", "Magie", "Sauvegarder", "Fermer"
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

    public void renderTransitionScreen(int floor) {
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        uiBatch.begin();
        font.getData().setScale(2.5f);
        font.setColor(Color.WHITE);
        String text = "ÉTAGE " + floor;
        float x = (Gdx.graphics.getWidth() / 2f) - (text.length() * 15f);
        float y = (Gdx.graphics.getHeight() / 2f);
        font.draw(uiBatch, text, x, y);
        font.getData().setScale(1.2f);
        uiBatch.end();
    }

    /**
     * Effectue le rendu 2D du HUD et du ConsoleMenu Dragon Quest en superposition.
     *
     * @param dungeon Donjon actuel
     * @param playerX Position X du joueur
     * @param playerY Position Y du joueur
     * @param currentFloor étage actuel
     * */
    public void renderHUD(Dungeon dungeon, int playerX, int playerY, int currentFloor, HD2DGameApp.GameState state, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<String> messages, String menuTitle, String[] menuOptions, HD2DGameApp gameApp) {
        handleMenuInput(state, gameApp, messages, menuTitle);
        if (menuTitle != null && menuOptions != null) {
            handleContextualMenuInput(menuOptions, gameApp);
        }

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);

        if (state == HD2DGameApp.GameState.BATTLE || state == HD2DGameApp.GameState.HUB) {
            renderMinimalistWindow(team, messages);
            if (state == HD2DGameApp.GameState.BATTLE && team != null) {
                renderBattleStatus(team);
            }
        } else if (isMenuOpen) {
            renderDragonQuestWindow(dungeon, playerX, playerY, team);
        } else {
            renderExplorationHUD(dungeon, playerX, playerY, currentFloor, state);
            if (messages != null && !messages.isEmpty()) {
                renderMinimalistWindow(team, messages);
            }
        }

        // Toujours dessiner le menu contextuel s'il existe et n'est pas un menu de pause (dialogue)
        if (menuTitle != null && menuOptions != null && !"[Continuer]".equals(menuOptions[0])) {
            renderContextualMenu(menuTitle, menuOptions);
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

    private void handleMenuInput(HD2DGameApp.GameState state, HD2DGameApp gameApp, java.util.List<String> messages, String menuTitle) {
        if (state == HD2DGameApp.GameState.BATTLE || state == HD2DGameApp.GameState.HUB) return; // Désactivé en combat et HubController
        if (messages != null && !messages.isEmpty()) return; // Désactivé pendant un dialogue
        if (menuTitle != null) return; // Désactivé pendant un sous-menu contextuel

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
                if (selectedOption == 0 && gameApp.parentView != null) {
                    gameApp.parentView.pushInput("C");
                    isMenuOpen = false;
                } else if (selectedOption == 1 && gameApp.parentView != null) {
                    gameApp.parentView.pushInput("I");
                    isMenuOpen = false;
                } else if (selectedOption == 4 && gameApp.parentView != null) {
                    gameApp.parentView.pushInput("K");
                    isMenuOpen = false;
                } else if (selectedOption == 5) {
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

    private void renderBattleStatus(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team) {
        int windowWidth = 220;
        int windowHeight = 40 + (team.getMembers().size() * 65);
        int padding = 20;
        int startX = 20;
        int startY = Gdx.graphics.getHeight() - windowHeight - 20;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.3f, 0.85f));
        shapeRenderer.rect(startX, startY, windowWidth, windowHeight);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(startX, startY, startX + windowWidth, startY, 2);
        shapeRenderer.rectLine(startX, startY + windowHeight, startX + windowWidth, startY + windowHeight, 2);
        shapeRenderer.rectLine(startX, startY, startX, startY + windowHeight, 2);
        shapeRenderer.rectLine(startX + windowWidth, startY, startX + windowWidth, startY + windowHeight, 2);
        shapeRenderer.end();

        uiBatch.begin();
        font.setColor(Color.GOLD);
        font.draw(uiBatch, "COMPAGNIE", startX + 20, startY + windowHeight - 15);
        font.setColor(Color.WHITE);

        int currentY = startY + windowHeight - 50;
        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character member : team.getMembers()) {
            if (member.getHealthPoint() <= 0) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            
            font.draw(uiBatch, member.getName(), startX + 10, currentY);
            currentY -= 20;
            font.draw(uiBatch, "PV: " + member.getHealthPoint() + "/" + member.getMaxHealthPoint(), startX + 20, currentY);
            currentY -= 20;
            font.draw(uiBatch, member.getResourceName().substring(0, 1) + "P: " + member.getCurrentResource() + "/" + member.getMaxResource(), startX + 20, currentY);
            currentY -= 25; // Espace entre persos
        }
        uiBatch.end();
    }

    private void renderMinimalistWindow(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<String> messages) {
        if (messages == null || messages.isEmpty()) return;

        // Extraire le dernier message
        String currentMessage = messages.get(messages.size() - 1);
        String speakerName = null;
        String dialogueText = currentMessage;

        if (currentMessage.contains(" : ")) {
            String[] parts = currentMessage.split(" : ", 2);
            speakerName = parts[0];
            dialogueText = parts[1];
        } else if (currentMessage.startsWith("[") && currentMessage.contains("] ")) {
            int closeBracket = currentMessage.indexOf("] ");
            speakerName = currentMessage.substring(1, closeBracket);
            dialogueText = currentMessage.substring(closeBracket + 2);
        }

        int boxX = 100;
        int boxY = 20;
        int boxWidth = 1080;
        int boxHeight = 180;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.05f, 0.05f, 0.05f, 0.95f));
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);

        if (speakerName != null) {
            shapeRenderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
            shapeRenderer.rect(boxX + 20, boxY + boxHeight - 10, 200, 40);
        }
        shapeRenderer.end();

        uiBatch.begin();
        font.setColor(Color.WHITE);
        if (speakerName != null) {
            font.draw(uiBatch, speakerName, boxX + 30, boxY + boxHeight + 15);
        }
        font.draw(uiBatch, dialogueText, boxX + 30, boxY + boxHeight - 30);
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

                if (cell.isDiscovered()) {
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
