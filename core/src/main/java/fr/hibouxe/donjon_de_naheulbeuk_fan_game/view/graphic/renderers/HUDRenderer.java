package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.renderers;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.infrastructure.audio.AudioManager;
import com.badlogic.gdx.utils.Disposable;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager;

/**
 * Composant de rendu 2D spÃƒÂ©cialisÃƒÂ© pour l'Interface Utilisateur (HUD) et le ConsoleMenu Interactif Dragon Quest (SRP).
 * Ouvre une fenÃƒÂªtre bleue ÃƒÂ  bordure dorÃƒÂ©e (Sac, Sorts, Ãƒâ€°quipement, Carte, Sauvegarde) ÃƒÂ  la pression de 'M' ou 'ECHAP'.
 *
 * @author Hibouxe
 * @version 2.0
 */
public class HUDRenderer implements Disposable {
    private static final com.badlogic.gdx.graphics.Color COLOR_FLOAT_BG = new com.badlogic.gdx.graphics.Color(0.1f, 0.1f, 0.1f, 0.95f);
    private static final com.badlogic.gdx.graphics.Color COLOR_GOLD_BORDER = new com.badlogic.gdx.graphics.Color(0.8f, 0.7f, 0.4f, 1f);
    private static final com.badlogic.gdx.graphics.Color COLOR_CURSOR_FLOAT = new com.badlogic.gdx.graphics.Color(0.3f, 0.3f, 0.3f, 0.8f);
    private static final com.badlogic.gdx.graphics.Color COLOR_MENU_BG = new com.badlogic.gdx.graphics.Color(0.05f, 0.05f, 0.05f, 0.95f);
    private static final com.badlogic.gdx.graphics.Color COLOR_MENU_CURSOR = new com.badlogic.gdx.graphics.Color(0.4f, 0.4f, 0.4f, 0.6f);
    private static final com.badlogic.gdx.graphics.Color COLOR_TEXT_BLUE = new com.badlogic.gdx.graphics.Color(0.7f, 0.9f, 1f, 1f);
    private static final com.badlogic.gdx.graphics.Color COLOR_HP = new com.badlogic.gdx.graphics.Color(0.2f, 0.8f, 0.2f, 1f);
    private static final com.badlogic.gdx.graphics.Color COLOR_MP = new com.badlogic.gdx.graphics.Color(0.2f, 0.6f, 0.9f, 1f);
    private static final com.badlogic.gdx.graphics.Color COLOR_PORTRAIT_BG = new com.badlogic.gdx.graphics.Color(0.2f, 0.2f, 0.2f, 1f);
    private static final com.badlogic.gdx.graphics.Color COLOR_CURSOR_HL = new com.badlogic.gdx.graphics.Color(0.3f, 0.3f, 0.4f, 0.8f);
    private static final com.badlogic.gdx.graphics.Color COLOR_MAP_BG = new com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 0.7f);
    private static final com.badlogic.gdx.graphics.Color COLOR_MINI_CELL = new com.badlogic.gdx.graphics.Color(0.4f, 0.4f, 0.45f, 0.9f);
    
    private final com.badlogic.gdx.math.Rectangle cachedScissors = new com.badlogic.gdx.math.Rectangle();
    private final com.badlogic.gdx.math.Rectangle cachedClipBounds = new com.badlogic.gdx.math.Rectangle();

    public SpriteBatch uiBatch;
    public BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private String floatingMessage = null;
    private float floatingMessageTimer = 0f;
    
    public void showFloatingMessage(String msg, float duration) {
        this.floatingMessage = msg;
        this.floatingMessageTimer = duration;
    }

    private boolean isMenuOpen = false;
    private int selectedOption = 0;
    private String[] menuOptions = {
            "Status", "Sac", "Ãƒâ€°quipement", "Magie", "Sauvegarder", "ParamÃƒÂ¨tres", "Fermer"
    };
    
    // --- Settings Menu State ---
    private boolean isSettingsMenuOpen = false;
    private int selectedSettingsOption = 0;
    private String[] settingsOptions = {
            "Plein Ecran : ", "V-Sync : ", "Volume General : ", "Volume Musique : ", "Volume SFX : ", "Vitesse Marche : ", "Vitesse Texte : ", "Retour"
    };

    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager settingsManager;

    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.infrastructure.audio.AudioManager audioManager;
    
    public HUDRenderer(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager settingsManager, fr.hibouxe.donjon_de_naheulbeuk_fan_game.infrastructure.audio.AudioManager audioManager) {
        this.settingsManager = settingsManager;
        this.audioManager = audioManager;
        uiBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        shapeRenderer = new ShapeRenderer();
        uiViewport = new com.badlogic.gdx.utils.viewport.FitViewport(1280, 720);
    }
    
    private com.badlogic.gdx.utils.viewport.Viewport uiViewport;

    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
        uiBatch.setProjectionMatrix(uiViewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(uiViewport.getCamera().combined);
    }

    public boolean isSettingsMenuOpen() { return isSettingsMenuOpen; }
    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    public void renderTransitionScreen(int floor, long startTime) {
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        long elapsed = System.currentTimeMillis() - startTime;
        float progress;
        if (elapsed <= 1000) {
            progress = elapsed / 1000.0f;
        } else {
            progress = 1.0f - ((elapsed - 1000) / 500.0f);
        }
        progress = Math.max(0f, Math.min(1.0f, progress));

        uiViewport.apply();
        shapeRenderer.setProjectionMatrix(uiViewport.getCamera().combined);
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        float squareSize = 64f;
        int cols = (int)(1280 / squareSize) + 2;
        int rows = (int)(720 / squareSize) + 2;
        float currentSize = squareSize * progress;

        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                float cx = i * squareSize + squareSize / 2f;
                float cy = j * squareSize + squareSize / 2f;
                shapeRenderer.rect(cx - currentSize/2f, cy - currentSize/2f, currentSize, currentSize);
            }
        }
        shapeRenderer.end();

        if (floor >= 0 && font != null) {
            uiBatch.begin();
            font.getData().setScale(2.5f);
            font.setColor(Color.WHITE);
            String text = "ETAGE " + floor;
            float x = (1280 / 2f) - (text.length() * 15f);
            float y = (720 / 2f);
            font.draw(uiBatch, text, x, y);
            uiBatch.end();
            font.getData().setScale(1.0f);
        } else if (progress >= 0.8f) {
            uiBatch.begin();
            font.getData().setScale(2.0f);
            font.setColor(Color.WHITE);
            String text = "Chargement...";
            float x = (1280 / 2f) - (text.length() * 12f);
            float y = (720 / 2f);
            font.draw(uiBatch, text, x, y);
            font.getData().setScale(1.2f);
            uiBatch.end();
        }
    }

    /**
     * Effectue le rendu 2D du HUD et du ConsoleMenu Dragon Quest en superposition.
     *
     * @param dungeon Donjon actuel
     * @param playerX Position X du joueur
     * @param playerY Position Y du joueur
     * @param currentFloor Ãƒâ€°tage actuel
     * */
    public void renderHUD(Dungeon dungeon, float playerX, float playerY, int currentFloor, HD2DGameApp.GameState state, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, java.util.List<String> messages, String menuTitle, String[] menuOptions, HD2DGameApp gameApp) {



        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);

        if (state == HD2DGameApp.GameState.BATTLE || state == HD2DGameApp.GameState.HUB) {
            if (state == HD2DGameApp.GameState.BATTLE && team != null) {
                renderBattleStatus(team);
            }
        } else if (isSettingsMenuOpen) {
            renderSettingsWindow();
        } else if (isMenuOpen) {
            renderDragonQuestWindow(dungeon, playerX, playerY, team);
        } else {
            renderExplorationHUD(dungeon, playerX, playerY, currentFloor, state);
        }

        // Toujours dessiner le menu contextuel s'il existe et n'est pas un menu de pause (dialogue)
        if (!isSettingsMenuOpen && menuTitle != null && menuOptions != null && !"[Continuer]".equals(menuOptions[0]) && state != HD2DGameApp.GameState.TRANSITION) {
            if ("STATISTIQUES".equals(menuTitle) && team != null) {
                renderContextualMenu(menuTitle, menuOptions, state);
                renderStatusScreen(team, contextMenuSelection);
            } else {
                renderContextualMenu(menuTitle, menuOptions, state);
                if (team != null && state != HD2DGameApp.GameState.BATTLE) {
                    int contextX = isMenuOpen ? 50 + 350 + 20 : 50;
                    renderDragonQuestTeamStatus(team, contextX, 720 - (60 + menuOptions.length * 40) - 50);
                }
            }
        } else if (!isSettingsMenuOpen && isMenuOpen && team != null) {
            // Dessiner le statut de l'équipe sous le menu principal s'il n'y a pas de menu contextuel
            int menuHeight = 60 + 7 * 40; // 7 options dans le menu principal
            renderDragonQuestTeamStatus(team, 50, 720 - menuHeight - 50);
        }
        
        if (floatingMessage != null && floatingMessageTimer > 0) {
            floatingMessageTimer -= Gdx.graphics.getDeltaTime();
            uiBatch.begin();
            font.getData().setScale(2.5f);
            com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, floatingMessage);
            float cx = (1280 - layout.width) / 2;
            float cy = (720 + layout.height) / 2 + 150;
            font.setColor(0, 0, 0, 1);
            font.draw(uiBatch, floatingMessage, cx + 2, cy - 2);
            font.setColor(1, 1, 0, 1);
            font.draw(uiBatch, floatingMessage, cx, cy);
            font.getData().setScale(1.5f);
            uiBatch.end();
        } else {
            floatingMessage = null;
        }

        if (messages != null && !messages.isEmpty()) {
            renderMinimalistWindow(team, messages, state);
        }
    }

    private int contextMenuSelection = 0;



    private void renderContextualMenu(String title, String[] options, HD2DGameApp.GameState state) {
        if (state == HD2DGameApp.GameState.BATTLE) {
            // === MENU DE COMBAT (Style DQ3 HD-2D) ===
            int menuWidth = 350;
            int menuHeight = 60 + options.length * 40;
            int x = 30; // AlignÃƒÂ© ÃƒÂ  gauche, plus bas
            int y = 50;
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // FenÃ¢â€Å“Ã‚Â¬tre Noire semi-transparente
            shapeRenderer.setColor(COLOR_FLOAT_BG);
            shapeRenderer.rect(x, y, menuWidth, menuHeight);
            
            // Bordure grise sobre
            shapeRenderer.setColor(COLOR_GOLD_BORDER);
            shapeRenderer.rectLine(x, y, x + menuWidth, y, 3);
            shapeRenderer.rectLine(x, y + menuHeight, x + menuWidth, y + menuHeight, 3);
            shapeRenderer.rectLine(x, y, x, y + menuHeight, 3);
            shapeRenderer.rectLine(x + menuWidth, y, x + menuWidth, y + menuHeight, 3);

            // Curseur gris foncÃƒÂ©
            if (contextMenuSelection < options.length) {
                int cursorY = y + menuHeight - 80 - contextMenuSelection * 40;
                shapeRenderer.setColor(COLOR_CURSOR_FLOAT);
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
            
            int itemWidth = 350;
            int menuWidth = Math.max(300, columns * itemWidth + 60);
            int menuHeight = 60 + rows * 40;
            
            // PlacÃƒÂ© en haut ÃƒÂ  gauche, ou dÃƒÂ©calÃƒÂ© (poupÃƒÂ©e russe) si le menu principal est ouvert
            int x = isMenuOpen ? 50 + 350 + 20 : 50; 
            int y = 720 - menuHeight - 50; 
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            
            // FenÃƒÂªtre noire (95% opacitÃƒÂ©)
            shapeRenderer.setColor(COLOR_MENU_BG);
            shapeRenderer.rect(x, y, menuWidth, menuHeight);
            
            // Bordure blanche ÃƒÂ©paisse (3px)
            shapeRenderer.setColor(COLOR_GOLD_BORDER);
            shapeRenderer.rectLine(x, y, x + menuWidth, y, 3);
            shapeRenderer.rectLine(x, y + menuHeight, x + menuWidth, y + menuHeight, 3);
            shapeRenderer.rectLine(x, y, x, y + menuHeight, 3);
            shapeRenderer.rectLine(x + menuWidth, y, x + menuWidth, y + menuHeight, 3);
            
            // Curseur de sÃƒÂ©lection (fond de ligne gris transparent)
            if (contextMenuSelection < options.length) {
                int col = contextMenuSelection % columns;
                int row = contextMenuSelection / columns;
                
                int highlightWidth = (columns == 1) ? menuWidth - 20 : itemWidth;
                int cursorX = x + 10 + col * itemWidth;
                int cursorY = y + menuHeight - 75 - row * 40;
                
                shapeRenderer.setColor(COLOR_MENU_CURSOR);
                shapeRenderer.rect(cursorX, cursorY, highlightWidth, 35);
            }
            shapeRenderer.end();

            uiBatch.begin();
            // Titre alignÃƒÂ© ÃƒÂ  gauche
            font.setColor(COLOR_TEXT_BLUE);
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





    private void renderStatusScreen(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, int selection) {
        if (selection < 0 || selection >= team.getMembers().size()) return; // Si on est sur "Retour", on n'affiche rien
        
        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character hero = team.getMembers().get(selection);
        
        int statusWidth = 600;
        int statusHeight = 450;
        // PlacÃƒÂ© ÃƒÂ  cÃƒÂ´tÃƒÂ© du menu de gauche (x = 50 + menuWidth (environ 300) + 20)
        int x = 370;
        int y = 720 - statusHeight - 50;
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // FenÃƒÂªtre Noire (95% opacitÃƒÂ©)
        shapeRenderer.setColor(COLOR_MENU_BG);
        shapeRenderer.rect(x, y, statusWidth, statusHeight);
        
        // Bordure blanche ÃƒÂ©paisse (3px)
        shapeRenderer.setColor(COLOR_GOLD_BORDER);
        shapeRenderer.rectLine(x, y, x + statusWidth, y, 3);
        shapeRenderer.rectLine(x, y + statusHeight, x + statusWidth, y + statusHeight, 3);
        shapeRenderer.rectLine(x, y, x, y + statusHeight, 3);
        shapeRenderer.rectLine(x + statusWidth, y, x + statusWidth, y + statusHeight, 3);
        
        shapeRenderer.end();
        
        uiBatch.begin();
        
        // Titre
        font.setColor(COLOR_TEXT_BLUE);
        font.draw(uiBatch, "Fiche de " + hero.getName() + " (" + hero.getType() + ") - Niveau " + hero.getLevel(), x + 25, y + statusHeight - 15);
        
        font.setColor(Color.WHITE);
        int currentY = y + statusHeight - 60;
        
        // Section SantÃƒÂ© & Prog
        font.draw(uiBatch, "SantÃ¢â€Å“Ã‚Â® : " + hero.getHealthPoint() + " / " + hero.getMaxHealthPoint(), x + 30, currentY);
        font.draw(uiBatch, hero.getResourceName() + " : " + hero.getCurrentResource() + " / " + hero.getMaxResource(), x + 300, currentY);
        currentY -= 35;
        font.draw(uiBatch, "ExpÃ¢â€Å“Ã‚Â®rience : " + hero.getXp() + " / " + hero.getXpToNextLevel(), x + 30, currentY);
        currentY -= 50;
        
        // Attributs
        font.setColor(COLOR_TEXT_BLUE);
        font.draw(uiBatch, "Attributs de combat", x + 25, currentY);
        font.setColor(Color.WHITE);
        currentY -= 35;
        font.draw(uiBatch, "Attaque : " + hero.getAttack(), x + 30, currentY);
        font.draw(uiBatch, "DÃ¢â€Å“Ã‚Â®fense : " + hero.getDefense(), x + 300, currentY);
        currentY -= 35;
        font.draw(uiBatch, "Attaque Mag. : " + hero.getMagicAttack(), x + 30, currentY);
        font.draw(uiBatch, "DÃ¢â€Å“Ã‚Â®fense Mag. : " + hero.getMagicDefense(), x + 300, currentY);
        currentY -= 35;
        font.draw(uiBatch, "Vitesse : " + hero.getSpeed(), x + 30, currentY);
        currentY -= 50;
        
        //ÃƒÂ©quipements
        font.setColor(COLOR_TEXT_BLUE);
        font.draw(uiBatch, "Ã¢â€Å“ÃƒÂ«quipement actuel", x + 25, currentY);
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

    private void renderExplorationHUD(Dungeon dungeon, float playerX, float playerY, int currentFloor, HD2DGameApp.GameState state) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (state == HD2DGameApp.GameState.EXPLORATION && dungeon != null) {
            renderMinimap(dungeon, playerX, playerY);
        }

        shapeRenderer.end();

        uiBatch.begin();
        font.setColor(Color.WHITE);
        if (state == HD2DGameApp.GameState.EXPLORATION) {
            font.draw(uiBatch, "Ãƒâ€°TAGE " + currentFloor, 20, 720 - 20);
        } else if (state == HD2DGameApp.GameState.VILLAGE) {
            font.draw(uiBatch, "CAMPEMENT", 20, 720 - 20);
        }
        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[M/ECHAP: Menu]", 1280 - 180, 40);
        uiBatch.end();
    }

    private void renderBattleStatus(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team) {
        int padding = 15;
        int barWidth = 120;
        int barHeight = 8;
        int startX = 1280 - 250; // AlignÃ¢â€Å“Ã‚Â® Ã¢â€Å“ÃƒÂ¡ droite
        
        int memberCount = team.getMembers().size();
        int startY = (720 + (memberCount * 80)) / 2; // CentrÃƒÂ© verticalement
        
        // Optionnel : un fond trÃƒÂ¨s lÃƒÂ©ger semi-transparent derriÃƒÂ¨re tous les statuts pour la lisibilitÃƒÂ©
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
            shapeRenderer.setColor(COLOR_HP);
            shapeRenderer.rect(startX + 30, currentY - 25, barWidth * hpPercent, barHeight);
            
            // Fond sombre pour la barre PM/Ressource
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(startX + 30, currentY - 45, barWidth, barHeight);
            
            // Barre PM/Ressource (Bleu cyan)
            float mpPercent = Math.max(0, (float) member.getCurrentResource() / member.getMaxResource());
            shapeRenderer.setColor(COLOR_MP);
            shapeRenderer.rect(startX + 30, currentY - 45, barWidth * mpPercent, barHeight);
            
            shapeRenderer.end();

            // 2. Rendu des Textes avec le SpriteBatch
            uiBatch.begin();
            
            // Nom du personnage (Blanc ou rouge si mort)
            if (member.getHealthPoint() <= 0) font.setColor(Color.RED);
            else font.setColor(Color.WHITE);
            font.draw(uiBatch, member.getName(), startX + 110 - (member.getName().length() * 4), currentY); // CentrÃ¢â€Å“Ã‚Â® approximativement
            
            // Textes "PV" et "PM" (Vert et Bleu)
            font.setColor(COLOR_HP);
            font.draw(uiBatch, "PV", startX, currentY - 18);
            
            font.setColor(COLOR_MP);
            font.draw(uiBatch, member.getResourceName().substring(0, 1) + "M", startX, currentY - 38);
            
            // Valeurs numÃƒÂ©riques sur le cÃƒÂ´tÃƒÂ© droit des barres
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
        String currentMessage = messages.get(0);
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
            // Affichage ultra ÃƒÂ©purÃƒÂ© type "Texte flottant" (sans fond) pour les combats
            int screenWidth = 1280;
            int screenHeight = 720;
            
            // PlacÃƒÂ© trÃƒÂ¨s bas sur l'ÃƒÂ©cran
            int textY = screenHeight / 12; 
            
            // Approximation simple du centrage pour le texte
            int textX = (screenWidth / 2) - (dialogueText.length() * 4); 

            uiBatch.begin();
            // Effet d'ombre/contour noir pour la lisibilitÃƒÂ©
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

        // Affichage classique pour l'exploration
        int boxWidth = 1280 - 200; // Marge de 100px de chaque cÃƒÂ´tÃƒÂ©
        int boxX = 100;
        int boxY = 20;
        int boxHeight = 180;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(COLOR_MENU_BG);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);

        if (speakerName != null) {
            shapeRenderer.setColor(COLOR_PORTRAIT_BG);
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

        private void renderSettingsWindow() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int menuWidth = 550;
        int menuHeight = 80 + settingsOptions.length * 40;
        int menuX = (1280 - menuWidth) / 2;
        int menuY = (720 - menuHeight) / 2;

        shapeRenderer.setColor(COLOR_MENU_BG);
        shapeRenderer.rect(menuX, menuY, menuWidth, menuHeight);
        
        shapeRenderer.setColor(COLOR_GOLD_BORDER);
        shapeRenderer.rectLine(menuX, menuY, menuX + menuWidth, menuY, 3);
        shapeRenderer.rectLine(menuX, menuY + menuHeight, menuX + menuWidth, menuY + menuHeight, 3);
        shapeRenderer.rectLine(menuX, menuY, menuX, menuY + menuHeight, 3);
        shapeRenderer.rectLine(menuX + menuWidth, menuY, menuX + menuWidth, menuY + menuHeight, 3);

        int cursorY = menuY + menuHeight - 75 - selectedSettingsOption * 40;
        shapeRenderer.setColor(COLOR_CURSOR_HL);
        shapeRenderer.rect(menuX + 10, cursorY, menuWidth - 20, 35);

        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.settings.GameSettingsManager config = this.settingsManager;
        for (int i = 0; i < settingsOptions.length; i++) {
            if (i >= 2 && i <= 4) {
                int barX = menuX + 250;
                int barY = menuY + menuHeight - 50 - (i * 40) - 15;
                int barW = 200;
                int barH = 10;
                
                shapeRenderer.setColor(Color.DARK_GRAY);
                shapeRenderer.rect(barX, barY, barW, barH);
                
                float val = 0;
                if (i == 2) val = config.getMasterVolume();
                if (i == 3) val = config.getBgmVolume();
                if (i == 4) val = config.getSfxVolume();
                
                shapeRenderer.setColor(COLOR_HP);
                shapeRenderer.rect(barX, barY, barW * val, barH);
                
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(barX + (barW * val) - 2, barY - 2, 4, barH + 4);
            }
        }

        shapeRenderer.end();
        uiBatch.begin();

        font.setColor(COLOR_GOLD_BORDER);
        font.draw(uiBatch, "PARAMETRES", menuX + menuWidth / 2f - 70, menuY + menuHeight - 20);
        font.setColor(Color.WHITE);

        for (int i = 0; i < settingsOptions.length; i++) {
            String text = settingsOptions[i];
            String valueText = "";
            
            if (i == 0) valueText = config.isFullscreen() ? "Oui" : "Non";
            else if (i == 1) valueText = config.isVsync() ? "Oui" : "Non";
            else if (i == 2) valueText = Math.round(config.getMasterVolume() * 100) + "%";
            else if (i == 3) valueText = Math.round(config.getBgmVolume() * 100) + "%";
            else if (i == 4) valueText = Math.round(config.getSfxVolume() * 100) + "%";
            else if (i == 5) valueText = config.getMovementSpeed() > 1f ? "Rapide" : "Normal";
            else if (i == 6) valueText = config.getTextSpeed() == 3 ? "Instant" : (config.getTextSpeed() == 2 ? "Rapide" : "Normal");
            
            font.draw(uiBatch, text, menuX + 30, menuY + menuHeight - 50 - (i * 40));
            
            if (valueText.length() > 0 && (i < 2 || i > 4)) {
                font.draw(uiBatch, "< " + valueText + " >", menuX + 250, menuY + menuHeight - 50 - (i * 40));
            } else if (i >= 2 && i <= 4) {
                font.draw(uiBatch, valueText, menuX + 250 + 210, menuY + menuHeight - 50 - (i * 40));
            }
        }

        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[Q/D]: Modifier   [ENTRÉE]: Valider", menuX + 60, menuY + 25);

        uiBatch.end();
    }

    private void renderDragonQuestWindow(Dungeon dungeon, float playerX, float playerY, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // 1. FenÃƒÂªtre du Menu Principal (Top-Left)
        int menuWidth = 350;
        int menuHeight = 60 + menuOptions.length * 40;
        int menuX = 50;
        int menuY = 720 - menuHeight - 50;

        // Fond Noir (95% opacitÃƒÂ©)
        shapeRenderer.setColor(COLOR_MENU_BG);
        shapeRenderer.rect(menuX, menuY, menuWidth, menuHeight);
        
        // Bordure blanche ÃƒÂ©paisse (3px)
        shapeRenderer.setColor(COLOR_GOLD_BORDER);
        shapeRenderer.rectLine(menuX, menuY, menuX + menuWidth, menuY, 3);
        shapeRenderer.rectLine(menuX, menuY + menuHeight, menuX + menuWidth, menuY + menuHeight, 3);
        shapeRenderer.rectLine(menuX, menuY, menuX, menuY + menuHeight, 3);
        shapeRenderer.rectLine(menuX + menuWidth, menuY, menuX + menuWidth, menuY + menuHeight, 3);

        // Curseur de sÃƒÂ©lection (fond de ligne gris transparent)
        int cursorY = menuY + menuHeight - 75 - selectedOption * 40;
        shapeRenderer.setColor(COLOR_MENU_CURSOR);
        shapeRenderer.rect(menuX + 10, cursorY, menuWidth - 20, 35);

        shapeRenderer.end();

        // --- Textes ---
        uiBatch.begin();
        
        // Options du menu principal
        font.setColor(COLOR_TEXT_BLUE);
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
        
        // Aide ÃƒÂ  la navigation en bas ÃƒÂ  droite (trÃƒÂ¨s discret)
        font.setColor(Color.LIGHT_GRAY);
        font.draw(uiBatch, "[Utilise Z/W/S, ENTREE pour selectionner]", 1280 - 400, 50);

        uiBatch.end();
    }

        private void renderMinimap(Dungeon dungeon, float playerX, float playerY) {
        int miniCellSize = 8;
        int mapWidth = 200;
        int mapHeight = 200;
        float centerX = 1280 - mapWidth / 2f - 30; // 30px padding from right
        float centerY = 720 - mapHeight / 2f - 30; // 30px padding from top

        shapeRenderer.flush();

        com.badlogic.gdx.math.Rectangle scissors = cachedScissors;
        com.badlogic.gdx.math.Rectangle clipBounds = cachedClipBounds.set(centerX - mapWidth/2f, centerY - mapHeight/2f, mapWidth, mapHeight);
        com.badlogic.gdx.scenes.scene2d.utils.ScissorStack.calculateScissors(uiViewport.getCamera(), shapeRenderer.getTransformMatrix(), clipBounds, scissors);
        com.badlogic.gdx.scenes.scene2d.utils.ScissorStack.pushScissors(scissors);

        // Fond sombre
        shapeRenderer.setColor(COLOR_MAP_BG);
        shapeRenderer.rect(centerX - mapWidth/2f, centerY - mapHeight/2f, mapWidth, mapHeight);

        fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell[][] grid = dungeon.getGrid();
        int range = (mapWidth / miniCellSize) / 2 + 2;
        int startX = Math.max(0, (int)playerX - range);
        int endX = Math.min(dungeon.getWidth() - 1, (int)playerX + range);
        int startY_grid = Math.max(0, (int)playerY - range);
        int endY_grid = Math.min(dungeon.getHeight() - 1, (int)playerY + range);

        for (int x = startX; x <= endX; x++) {
            for (int y = startY_grid; y <= endY_grid; y++) {
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell cell = grid[x][y];
                float drawX = centerX + (x + 0.5f - playerX) * miniCellSize;
                float drawY = centerY - (y + 0.5f - playerY) * miniCellSize; // Z is inverted visually for map

                if (cell.isDiscovered() && cell.isWalkable()) {
                    shapeRenderer.setColor(COLOR_MINI_CELL);
                    shapeRenderer.rect(drawX - miniCellSize/2f, drawY - miniCellSize/2f, miniCellSize, miniCellSize);

                    if (cell.hasItem()) {
                        shapeRenderer.setColor(Color.CYAN);
                        shapeRenderer.rect(drawX - miniCellSize/2f + 1, drawY - miniCellSize/2f + 1, miniCellSize - 2, miniCellSize - 2);
                    } else if (cell.hasStairs()) {
                        shapeRenderer.setColor(Color.WHITE);
                        shapeRenderer.rect(drawX - miniCellSize/2f, drawY - miniCellSize/2f, miniCellSize, miniCellSize);
                    }
                }
            }
        }

        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.RoamingMonsterGroup mg : dungeon.getRoamingMonsters()) {
            if (dungeon.getGrid()[(int)mg.getX()][(int)mg.getZ()].isDiscovered()) {
                float mDrawX = centerX + (mg.getX() + 0.5f - playerX) * miniCellSize;
                float mDrawY = centerY - (mg.getZ() + 0.5f - playerY) * miniCellSize;
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(mDrawX - miniCellSize/2f + 1, mDrawY - miniCellSize/2f + 1, miniCellSize - 2, miniCellSize - 2);
            }
        }

        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(centerX - miniCellSize/2f, centerY - miniCellSize/2f, miniCellSize, miniCellSize);

        shapeRenderer.flush();
        com.badlogic.gdx.scenes.scene2d.utils.ScissorStack.popScissors();

        shapeRenderer.end();
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(COLOR_GOLD_BORDER);
        com.badlogic.gdx.Gdx.gl.glLineWidth(3f);
        shapeRenderer.rect(centerX - mapWidth/2f, centerY - mapHeight/2f, mapWidth, mapHeight);
        shapeRenderer.end();
        com.badlogic.gdx.Gdx.gl.glLineWidth(1f);
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
    }
    
    @Override
    public void dispose() {
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (uiBatch != null) uiBatch.dispose();
        if (font != null) font.dispose();
    }
    public int getContextMenuSelection() { return contextMenuSelection; }
    public void resetContextMenuSelection() { this.contextMenuSelection = 0; }

    public boolean onInput(String action, HD2DGameApp gameApp) {
        if (action != null && action.startsWith("MENU_")) {
            return false;
        }
        if (gameApp != null && gameApp.currentMenuTitle != null && gameApp.currentMenuOptions != null) {
            String[] options = gameApp.currentMenuOptions;
            if ("Z".equals(action) || "Q".equals(action) || "UP".equals(action)) {
                contextMenuSelection = (contextMenuSelection - 1 + options.length) % options.length; audioManager.playUIHover();
                return true;
            } else if ("S".equals(action) || "D".equals(action) || "DOWN".equals(action)) {
                contextMenuSelection = (contextMenuSelection + 1) % options.length; audioManager.playUIHover();
                return true;
            }
            if ("INVENTAIRE".equals(gameApp.currentMenuTitle) || "CIBLE_OBJET".equals(gameApp.currentMenuTitle) || "CATEGORIES".equals(gameApp.currentMenuTitle) || "OBJETS".equals(gameApp.currentMenuTitle) || "PAUSE".equals(gameApp.currentMenuTitle) || "CHOISIR UN OBJET".equals(gameApp.currentMenuTitle) || "CIBLE".equals(gameApp.currentMenuTitle) || "EMPLACEMENT".equals(gameApp.currentMenuTitle)) {
                if ("ENTER".equals(action) || "SPACE".equals(action) || "X".equals(action)) {
                    return false;
                }
                return true;
            }
            if ("STATISTIQUES".equals(gameApp.currentMenuTitle)) {
                if ("X".equals(action) || ("ENTER".equals(action) && "Retour".equals(options[contextMenuSelection]))) {
                    audioManager.playUIClose();
                    gameApp.setMenuRequest(null, null);
                    isMenuOpen = true; 
                    return true;
                }
            } else if ("X".equals(action) || ("ENTER".equals(action) && "Retour".equals(options[contextMenuSelection]))) {
                audioManager.playUIClose();
                return false;
            } else if ("ENTER".equals(action) || "SPACE".equals(action)) {
                audioManager.playUIAccept();
                return false;
            } else if (action.matches("[1-9]")) {
                int index = Integer.parseInt(action);
                if (index > 0 && index <= options.length) {
                    contextMenuSelection = index - 1;
                }
                return false;
            }
            return true;
        }

        if ("M".equals(action) || "X".equals(action)) {
            if (isSettingsMenuOpen) {
                isSettingsMenuOpen = false;
                settingsManager.saveSettings();
                audioManager.playUIClose();
            } else {
                isMenuOpen = !isMenuOpen;
                if (isMenuOpen) audioManager.playUIOpen();
                else audioManager.playUIClose();
            }
            return true;
        }

        if (isSettingsMenuOpen) {
            if ("Z".equals(action) || "UP".equals(action)) {
                selectedSettingsOption = (selectedSettingsOption - 1 + settingsOptions.length) % settingsOptions.length;
                audioManager.playUIHover();
                return true;
            } else if ("S".equals(action) || "DOWN".equals(action)) {
                selectedSettingsOption = (selectedSettingsOption + 1) % settingsOptions.length;
                audioManager.playUIHover();
                return true;
            } else if ("Q".equals(action) || "LEFT".equals(action)) {
                if (selectedSettingsOption == 2) {
                    settingsManager.setMasterVolume(Math.max(0f, settingsManager.getMasterVolume() - 0.1f));
                    audioManager.updateMusicVolume();
                    audioManager.playUIHover();
                } else if (selectedSettingsOption == 3) {
                    settingsManager.setBgmVolume(Math.max(0f, settingsManager.getBgmVolume() - 0.1f));
                    audioManager.updateMusicVolume();
                    audioManager.playUIHover();
                } else if (selectedSettingsOption == 4) {
                    settingsManager.setSfxVolume(Math.max(0f, settingsManager.getSfxVolume() - 0.1f));
                    audioManager.playUIHover();
                }
                return true;
            } else if ("D".equals(action) || "RIGHT".equals(action)) {
                if (selectedSettingsOption == 2) {
                    settingsManager.setMasterVolume(Math.min(1.0f, settingsManager.getMasterVolume() + 0.1f));
                    audioManager.updateMusicVolume();
                    audioManager.playUIHover();
                } else if (selectedSettingsOption == 3) {
                    settingsManager.setBgmVolume(Math.min(1.0f, settingsManager.getBgmVolume() + 0.1f));
                    audioManager.updateMusicVolume();
                    audioManager.playUIHover();
                } else if (selectedSettingsOption == 4) {
                    settingsManager.setSfxVolume(Math.min(1.0f, settingsManager.getSfxVolume() + 0.1f));
                    audioManager.playUIHover();
                }
                return true;
            } else if ("ENTER".equals(action) || "SPACE".equals(action)) {
                audioManager.playUIAccept();
                if (selectedSettingsOption == 0) {
                    settingsManager.setFullscreen(!settingsManager.isFullscreen());
                } else if (selectedSettingsOption == 1) {
                    settingsManager.setVsync(!settingsManager.isVsync());
                } else if (selectedSettingsOption == 5) {
                    settingsManager.setMovementSpeed(settingsManager.getMovementSpeed() > 1.0f ? 1.0f : 1.5f);
                } else if (selectedSettingsOption == 6) {
                    int nextSpeed = settingsManager.getTextSpeed() + 1;
                    if (nextSpeed > 3) nextSpeed = 1;
                    settingsManager.setTextSpeed(nextSpeed);
                } else if (selectedSettingsOption == 7) {
                    isSettingsMenuOpen = false;
                    settingsManager.saveSettings();
                    audioManager.playUIClose();
                    isMenuOpen = true;
                }
                return true;
            }
            return true;
        }

        if (isMenuOpen) {
            if ("Z".equals(action) || "Q".equals(action) || "UP".equals(action)) {
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length; audioManager.playUIHover();
                return true;
            } else if ("S".equals(action) || "D".equals(action) || "DOWN".equals(action)) {
                selectedOption = (selectedOption + 1) % menuOptions.length; audioManager.playUIHover();
                return true;
            } else if ("ENTER".equals(action) || "SPACE".equals(action)) {
                audioManager.playUIAccept();
                if (selectedOption == 0) {
                    gameApp.pushInput("MENU_STATUS");
                } else if (selectedOption == 1) {
                    gameApp.pushInput("MENU_INVENTORY");
                } else if (selectedOption == 2) {
                    isMenuOpen = false;
                    gameApp.pushInput("MENU_EQUIPMENT");
                } else if (selectedOption == 3) {
                    isMenuOpen = false;
                    gameApp.pushInput("MENU_MAGIC");
                } else if (selectedOption == 4) {
                    isMenuOpen = false;
                    gameApp.pushInput("MENU_SAVE");
                } else if (selectedOption == 5) {
                    isSettingsMenuOpen = true;
                } else if (selectedOption == 6) {
                    isMenuOpen = false;
                }
                return true;
            }
            return true;
        }

        return false;
    }

    private void renderDragonQuestTeamStatus(fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team team, int statusX, int menuY) {
        if (team == null) return;
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        int statusWidth = 450;
        int statusHeight = 60 + team.getMembers().size() * 50;
        int statusY = menuY - statusHeight - 30;

        shapeRenderer.setColor(COLOR_MENU_BG);
        shapeRenderer.rect(statusX, statusY, statusWidth, statusHeight);
        
        shapeRenderer.setColor(COLOR_GOLD_BORDER);
        shapeRenderer.rectLine(statusX, statusY, statusX + statusWidth, statusY, 3);
        shapeRenderer.rectLine(statusX, statusY + statusHeight, statusX + statusWidth, statusY + statusHeight, 3);
        shapeRenderer.rectLine(statusX, statusY, statusX, statusY + statusHeight, 3);
        shapeRenderer.rectLine(statusX + statusWidth, statusY, statusX + statusWidth, statusY + statusHeight, 3);
        shapeRenderer.end();

        uiBatch.begin();
        int startY = statusY + statusHeight - 30;
        for (fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character hero : team.getMembers()) {
            String line = String.format("%-15s : Nv %d | PV: %d/%d | PM: %d/%d",
                    hero.getName(), hero.getLevel(),
                    hero.getHealthPoint(), hero.getMaxHealthPoint(),
                    hero.getCurrentResource(), hero.getMaxResource());
            font.draw(uiBatch, line, statusX + 25, startY);
            startY -= 50;
        }
        uiBatch.end();
    }
}
