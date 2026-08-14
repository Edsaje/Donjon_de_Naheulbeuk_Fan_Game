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
            renderMinimalistWindow(team, messages, state);
            if (state == HD2DGameApp.GameState.BATTLE && team != null) {
                renderBattleStatus(team);
            }
        } else if (isMenuOpen) {
            renderDragonQuestWindow(dungeon, playerX, playerY, team);
        } else {
            renderExplorationHUD(dungeon, playerX, playerY, currentFloor, state);
            if (messages != null && !messages.isEmpty()) {
                renderMinimalistWindow(team, messages, state);
            }
        }

        // Toujours dessiner le menu contextuel s'il existe et n'est pas un menu de pause (dialogue)
        if (menuTitle != null && menuOptions != null && !"[Continuer]".equals(menuOptions[0])) {
            if ("STATISTIQUES".equals(menuTitle) && team != null) {
                renderContextualMenu(menuTitle, menuOptions, state);
                renderStatusScreen(team, contextMenuSelection);
            } else {
                renderContextualMenu(menuTitle, menuOptions, state);
            }
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

    private void renderContextualMenu(String title, String[] options, HD2DGameApp.GameState state) {
        if (state == HD2DGameApp.GameState.BATTLE) {
            // === MENU DE COMBAT (Style DQ3 HD-2D) ===
            int menuWidth = 350;
            int menuHeight = 60 + options.length * 40;
            int x = 30; // Aligné à gauche, plus bas
            int y = 50;
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // Fenêtre Noire semi-transparente
            shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.1f, 0.95f));
            shapeRenderer.rect(x, y, menuWidth, menuHeight);
            
            // Bordure grise sobre
            shapeRenderer.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
            shapeRenderer.rectLine(x, y, x + menuWidth, y, 2);
            shapeRenderer.rectLine(x, y + menuHeight, x + menuWidth, y + menuHeight, 2);
            shapeRenderer.rectLine(x, y, x, y + menuHeight, 2);
            shapeRenderer.rectLine(x + menuWidth, y, x + menuWidth, y + menuHeight, 2);

            // Curseur gris foncé
            if (contextMenuSelection < options.length) {
                int cursorY = y + menuHeight - 80 - contextMenuSelection * 40;
                shapeRenderer.setColor(new Color(0.3f, 0.3f, 0.3f, 0.8f));
                shapeRenderer.rect(x + 10, cursorY - 5, menuWidth - 20, 30);
            }
            shapeRenderer.end();

            uiBatch.begin();
            font.setColor(Color.WHITE); // Titre blanc
            font.draw(uiBatch, title, x + 25, y + menuHeight - 20);

            font.setColor(Color.WHITE);
            for (int i = 0; i < options.length; i++) {
                String prefix = (i == contextMenuSelection) ? "> " : "  ";
                font.draw(uiBatch, prefix + options[i], x + 35, y + menuHeight - 60 - i * 40);
            }
            uiBatch.end();

        } else {
            // === MENU HORS COMBAT (Style DQ1 Remake Items) ===
            int columns = (options.length > 5 && options.length <= 8) ? 2 : 1;
            int rows = (int) Math.ceil((double) options.length / columns);
            
            int itemWidth = 250;
            int menuWidth = Math.max(300, columns * itemWidth + 60);
            int menuHeight = 60 + rows * 40;
            
            // Placé en haut à gauche
            int x = 50; 
            int y = Gdx.graphics.getHeight() - menuHeight - 50; 
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            
            // Fenêtre Noire (95% opacité)
            shapeRenderer.setColor(new Color(0.05f, 0.05f, 0.05f, 0.95f));
            shapeRenderer.rect(x, y, menuWidth, menuHeight);
            
            // Bordure blanche épaisse (3px)
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rectLine(x, y, x + menuWidth, y, 3);
            shapeRenderer.rectLine(x, y + menuHeight, x + menuWidth, y + menuHeight, 3);
            shapeRenderer.rectLine(x, y, x, y + menuHeight, 3);
            shapeRenderer.rectLine(x + menuWidth, y, x + menuWidth, y + menuHeight, 3);
            
            // Curseur de sélection (fond de ligne gris transparent)
            if (contextMenuSelection < options.length) {
                int col = contextMenuSelection % columns;
                int row = contextMenuSelection / columns;
                
                int highlightWidth = (columns == 1) ? menuWidth - 20 : itemWidth;
                int cursorX = x + 10 + col * itemWidth;
                int cursorY = y + menuHeight - 75 - row * 40;
                
                shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
                shapeRenderer.rect(cursorX, cursorY, highlightWidth, 35);
            }
            shapeRenderer.end();

            uiBatch.begin();
            // Titre aligné à gauche
            font.setColor(new Color(0.7f, 0.9f, 1f, 1f));
            font.draw(uiBatch, title, x + 25, y + menuHeight - 15);

            for (int i = 0; i < options.length; i++) {
                int col = i % columns;
                int row = i / columns;
                
                int textX = x + 40 + col * itemWidth;
                int textY = y + menuHeight - 50 - row * 40;
                
                if (i == contextMenuSelection) {
                    font.setColor(Color.GOLD);
                    font.draw(uiBatch, ">>", textX - 25, textY);
                }
                
                font.setColor(Color.WHITE);
                font.draw(uiBatch, options[i], textX, textY);
            }
            uiBatch.end();
        }
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

    private void renderStatusScreen(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, int selection) {
        if (selection < 0 || selection >= team.getMembers().size()) return; // Si on est sur "Retour", on n'affiche rien
        
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character hero = team.getMembers().get(selection);
        
        int statusWidth = 600;
        int statusHeight = 450;
        // Placé à côté du menu de gauche (x = 50 + menuWidth (environ 300) + 20)
        int x = 370;
        int y = Gdx.graphics.getHeight() - statusHeight - 50;
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Fenêtre Noire (95% opacité)
        shapeRenderer.setColor(new Color(0.05f, 0.05f, 0.05f, 0.95f));
        shapeRenderer.rect(x, y, statusWidth, statusHeight);
        
        // Bordure blanche épaisse (3px)
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(x, y, x + statusWidth, y, 3);
        shapeRenderer.rectLine(x, y + statusHeight, x + statusWidth, y + statusHeight, 3);
        shapeRenderer.rectLine(x, y, x, y + statusHeight, 3);
        shapeRenderer.rectLine(x + statusWidth, y, x + statusWidth, y + statusHeight, 3);
        
        shapeRenderer.end();
        
        uiBatch.begin();
        
        // Titre
        font.setColor(new Color(0.7f, 0.9f, 1f, 1f));
        font.draw(uiBatch, "Fiche de " + hero.getName() + " (" + hero.getType() + ") - Niveau " + hero.getLevel(), x + 25, y + statusHeight - 15);
        
        font.setColor(Color.WHITE);
        int currentY = y + statusHeight - 60;
        
        // Section Santé & Prog
        font.draw(uiBatch, "Santé : " + hero.getHealthPoint() + " / " + hero.getMaxHealthPoint(), x + 30, currentY);
        font.draw(uiBatch, hero.getResourceName() + " : " + hero.getCurrentResource() + " / " + hero.getMaxResource(), x + 300, currentY);
        currentY -= 35;
        font.draw(uiBatch, "Expérience : " + hero.getXp() + " / " + hero.getXpToNextLevel(), x + 30, currentY);
        currentY -= 50;
        
        // Attributs
        font.setColor(new Color(0.7f, 0.9f, 1f, 1f));
        font.draw(uiBatch, "Attributs de combat", x + 25, currentY);
        font.setColor(Color.WHITE);
        currentY -= 35;
        font.draw(uiBatch, "Attaque : " + hero.getAttack(), x + 30, currentY);
        font.draw(uiBatch, "Défense : " + hero.getDefense(), x + 300, currentY);
        currentY -= 35;
        font.draw(uiBatch, "Attaque Mag. : " + hero.getMagicAttack(), x + 30, currentY);
        font.draw(uiBatch, "Défense Mag. : " + hero.getMagicDefense(), x + 300, currentY);
        currentY -= 35;
        font.draw(uiBatch, "Vitesse : " + hero.getSpeed(), x + 30, currentY);
        currentY -= 50;
        
        // Équipements
        font.setColor(new Color(0.7f, 0.9f, 1f, 1f));
        font.draw(uiBatch, "Équipement actuel", x + 25, currentY);
        font.setColor(Color.WHITE);
        currentY -= 35;
        
        java.util.Map<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment> equips = hero.getEquipments();
        int col = 0;
        int rowCount = 0;
        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot slot : fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.EquipmentSlot.values()) {
            String equipName = equips.containsKey(slot) ? equips.get(slot).getName() : "Aucun";
            int drawX = x + 30 + (col * 270);
            font.draw(uiBatch, slot.name() + " : " + equipName, drawX, currentY);
            
            col++;
            if (col > 1) {
                col = 0;
                currentY -= 30;
                rowCount++;
            }
        }
        
        uiBatch.end();
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
        int padding = 15;
        int barWidth = 120;
        int barHeight = 8;
        int startX = Gdx.graphics.getWidth() - 250; // Aligné à droite
        int startY = 300; // Position de départ Y

        int memberCount = team.getMembers().size();
        
        // Optionnel : un fond très léger semi-transparent derrière tous les statuts pour la lisibilité
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.4f));
        // shapeRenderer.rect(startX - 20, startY - (memberCount * 80) + 40, 260, memberCount * 80);
        // shapeRenderer.end();

        int currentY = startY;

        for (int i = 0; i < memberCount; i++) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character member = team.getMembers().get(i);
            
            // 1. Rendu des barres de vie et magie avec le ShapeRenderer
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            
            // Fond sombre pour la barre PV
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(startX + 30, currentY - 25, barWidth, barHeight);
            
            // Barre PV (Vert clair)
            float hpPercent = Math.max(0, (float) member.getHealthPoint() / member.getMaxHealthPoint());
            shapeRenderer.setColor(new Color(0.2f, 0.8f, 0.2f, 1f));
            shapeRenderer.rect(startX + 30, currentY - 25, barWidth * hpPercent, barHeight);
            
            // Fond sombre pour la barre PM/Ressource
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(startX + 30, currentY - 45, barWidth, barHeight);
            
            // Barre PM/Ressource (Bleu cyan)
            float mpPercent = Math.max(0, (float) member.getCurrentResource() / member.getMaxResource());
            shapeRenderer.setColor(new Color(0.2f, 0.6f, 0.9f, 1f));
            shapeRenderer.rect(startX + 30, currentY - 45, barWidth * mpPercent, barHeight);
            
            shapeRenderer.end();

            // 2. Rendu des Textes avec le SpriteBatch
            uiBatch.begin();
            
            // Nom du personnage (Blanc ou rouge si mort)
            if (member.getHealthPoint() <= 0) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(uiBatch, member.getName(), startX + 110 - (member.getName().length() * 4), currentY); // Centré approximativement
            
            // Textes "PV" et "PM" (Vert et Bleu)
            font.setColor(new Color(0.2f, 0.8f, 0.2f, 1f));
            font.draw(uiBatch, "PV", startX, currentY - 18);
            
            font.setColor(new Color(0.2f, 0.6f, 0.9f, 1f));
            font.draw(uiBatch, member.getResourceName().substring(0, 1) + "M", startX, currentY - 38);
            
            // Valeurs numériques sur le côté droit des barres
            font.setColor(Color.WHITE);
            font.draw(uiBatch, String.valueOf(member.getHealthPoint()), startX + 30 + barWidth + 10, currentY - 18);
            font.draw(uiBatch, String.valueOf(member.getCurrentResource()), startX + 30 + barWidth + 10, currentY - 38);
            
            uiBatch.end();
            
            currentY -= 80; // Espace entre chaque personnage
        }
    }

    private void renderMinimalistWindow(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<String> messages, HD2DGameApp.GameState state) {
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

        if (state == HD2DGameApp.GameState.BATTLE) {
            // Affichage ultra épuré type "Texte flottant" (sans fond) pour les combats
            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();
            
            // Placé très bas sur l'écran
            int textY = screenHeight / 12; 
            
            // Approximation simple du centrage pour le texte
            int textX = (screenWidth / 2) - (dialogueText.length() * 4); 

            uiBatch.begin();
            // Effet d'ombre/contour noir pour la lisibilité
            font.setColor(Color.BLACK);
            font.draw(uiBatch, dialogueText, textX + 2, textY - 2);
            font.draw(uiBatch, dialogueText, textX - 2, textY + 2);
            font.draw(uiBatch, dialogueText, textX + 2, textY + 2);
            font.draw(uiBatch, dialogueText, textX - 2, textY - 2);
            
            // Texte principal blanc
            font.setColor(Color.WHITE);
            font.draw(uiBatch, dialogueText, textX, textY);
            uiBatch.end();
            return;
        }

        // Affichage classique (Boîte de dialogue) pour l'exploration
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

        // 1. Fenêtre du Menu Principal (Top-Left)
        int menuWidth = 350;
        int menuHeight = 60 + menuOptions.length * 40;
        int menuX = 50;
        int menuY = Gdx.graphics.getHeight() - menuHeight - 50;

        // Fond Noir (95% opacité)
        shapeRenderer.setColor(new Color(0.05f, 0.05f, 0.05f, 0.95f));
        shapeRenderer.rect(menuX, menuY, menuWidth, menuHeight);
        
        // Bordure blanche épaisse (3px)
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(menuX, menuY, menuX + menuWidth, menuY, 3);
        shapeRenderer.rectLine(menuX, menuY + menuHeight, menuX + menuWidth, menuY + menuHeight, 3);
        shapeRenderer.rectLine(menuX, menuY, menuX, menuY + menuHeight, 3);
        shapeRenderer.rectLine(menuX + menuWidth, menuY, menuX + menuWidth, menuY + menuHeight, 3);

        // Curseur de sélection (fond de ligne gris transparent)
        int cursorY = menuY + menuHeight - 75 - selectedOption * 40;
        shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
        shapeRenderer.rect(menuX + 10, cursorY, menuWidth - 20, 35);

        // 2. Fenêtre du Statut de l'équipe (Bottom-Left)
        int statusWidth = 450;
        int statusHeight = 60 + ((team != null) ? team.getMembers().size() * 50 : 0);
        int statusX = 50;
        int statusY = menuY - statusHeight - 30; // Juste en dessous du menu

        shapeRenderer.setColor(new Color(0.05f, 0.05f, 0.05f, 0.95f));
        shapeRenderer.rect(statusX, statusY, statusWidth, statusHeight);
        
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(statusX, statusY, statusX + statusWidth, statusY, 3);
        shapeRenderer.rectLine(statusX, statusY + statusHeight, statusX + statusWidth, statusY + statusHeight, 3);
        shapeRenderer.rectLine(statusX, statusY, statusX, statusY + statusHeight, 3);
        shapeRenderer.rectLine(statusX + statusWidth, statusY, statusX + statusWidth, statusY + statusHeight, 3);

        shapeRenderer.end();

        // --- Textes ---
        uiBatch.begin();
        
        // Options du menu principal
        font.setColor(new Color(0.7f, 0.9f, 1f, 1f));
        font.draw(uiBatch, "Menu", menuX + 25, menuY + menuHeight - 15);

        for (int i = 0; i < menuOptions.length; i++) {
            int textX = menuX + 50;
            int textY = menuY + menuHeight - 50 - i * 40;
            
            if (i == selectedOption) {
                font.setColor(Color.GOLD);
                font.draw(uiBatch, ">>", textX - 30, textY);
            }
            font.setColor(Color.WHITE);
            font.draw(uiBatch, menuOptions[i], textX, textY);
        }

        // Statut de la Compagnie
        font.setColor(new Color(0.7f, 0.9f, 1f, 1f));
        font.draw(uiBatch, "Compagnie", statusX + 25, statusY + statusHeight - 15);
        
        font.setColor(Color.WHITE);
        if (team != null && !team.getMembers().isEmpty()) {
            int startY = statusY + statusHeight - 50;
            for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character hero : team.getMembers()) {
                String line = String.format("%-15s : Nv %d | PV: %d/%d | PM: %d/%d",
                        hero.getName(), hero.getLevel(),
                        hero.getHealthPoint(), hero.getMaxHealthPoint(),
                        hero.getCurrentResource(), hero.getMaxResource());
                font.draw(uiBatch, line, statusX + 25, startY);
                startY -= 50;
            }
        } else {
            font.draw(uiBatch, "(Vide)", statusX + 25, statusY + statusHeight - 50);
        }
        
        // Aide à la navigation en bas à droite (très discret)
        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[Utilise Z/W/S, ENTREE pour selectionner]", Gdx.graphics.getWidth() - 400, 50);

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
