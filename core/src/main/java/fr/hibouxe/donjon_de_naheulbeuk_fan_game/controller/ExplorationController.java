package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Cell;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.Dungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.save.ISaveManager;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IMenuView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.IExplorationView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract.ICombatView;
import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item;
import java.util.List;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState;

public class ExplorationController implements GameState {
    private Dungeon maze;
    private Team team;
    private boolean running;
    private int currentFloor = 1;
    private IExplorationView view;
    private IMenuView menu;
    private ICombatView combatView;
    private boolean isTutorial;
    private int activeSlot = 1;
    private boolean elfJoined = false;
    private boolean elfHealed = false;
    private InventoryController inventoryController;
    
    private enum SubState { EXPLORING, PAUSE_MENU, STATUS_MENU, INVENTORY_SELECT_CATEGORY, INVENTORY_SELECT_ITEM, INVENTORY_SELECT_TARGET }
    private SubState subState = SubState.EXPLORING;
    private int selectedItemIndex = -1;
    private int selectedCategory = -1;

    private ISaveManager saveManager;
    private fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager locManager;
    private long lastMoveTime = 0;

    private GameContext gameContext;

    public ExplorationController(Dungeon maze, Team team, IExplorationView view, IMenuView menu, ICombatView combatView, boolean isTutorial, GameContext gameContext, ISaveManager saveManager, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager locManager) {
        this(maze, team, view, menu, combatView, isTutorial, 1, gameContext, saveManager, locManager);
    }

    public ExplorationController(Dungeon maze, Team team, IExplorationView view, IMenuView menu, ICombatView combatView, boolean isTutorial, int activeSlot, GameContext gameContext, ISaveManager saveManager, fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.lang.LocalizationManager locManager) {
        this.maze = maze;
        this.team = team;
        this.view = view;
        this.menu = menu;
        this.combatView = combatView;
        this.isTutorial = isTutorial;
        this.activeSlot = activeSlot;
        this.gameContext = gameContext;
        this.saveManager = saveManager;
        this.locManager = locManager;
        this.inventoryController = new InventoryController(menu, locManager);
    }

    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    private boolean floorIntroPlayed = false;

    @Override
    public void enter() {
        this.running = true;
        
        maze.updateFogOfWar(team.getX(), team.getY(), 3);
        view.display(maze, team, currentFloor);
        
        if (!floorIntroPlayed) {
            playFloorIntro(currentFloor);
            floorIntroPlayed = true;
        }
    }

    private void playFloorIntro(int floorNumber) {
        java.util.List<String> dialogues = new java.util.ArrayList<>();
        if (maze instanceof TutorialDungeon) {
            switch (floorNumber) {
                case 1:
                    dialogues.add("\n=== CHAPITRE 0 : Fuite de la taverne ===");
                    dialogues.add("Ranger : Aïe... J'ai un mal de crâne effroyable...");
                    dialogues.add("Ranger : Où est-ce que je suis ? J'étais tellement occupé à courir que je me suis perdu ! Je dois retrouver les autres !");
                    dialogues.add("Utilisez Z, Q, S, D (ou les flèches) pour vous déplacer dans le couloir.");
                    dialogues.add("Atteignez l'escalier au bout du chemin pour avancer.");
                    break;
                case 2:
                    dialogues.add("Ranger : Hey mais c'est l'Elfe ! Elle est par terre...");
                    dialogues.add("Ranger : Je ferai mieux d'aller voir !");
                    break;
                case 3:
                    dialogues.add("Voleur : Chuuut ! Restez dans l'ombre ! On ne voit rien avec ce brouillard...");
                    dialogues.add("Voleur : Il y a une patrouille d'Orques Géants juste devant. Ils sont trop nombreux !");
                    dialogues.add("Ranger : Comment on passe alors ?");
                    dialogues.add("Voleur : On va utiliser la Ruse !");
                    dialogues.add("[UI Tuto] : Avancez pour dissiper le brouillard de guerre.");
                    dialogues.add("[UI Tuto] : La Minimap en haut à droite affiche les ennemis en rouge. Évitez-les !");
                    dialogues.add("[UI Tuto] : Si un combat inévitable se déclenche, utilisez la commande [Fuir] !");
                    break;
                case 4:
                    dialogues.add("Ogre : Chprouk ! Grrrumph !");
                    dialogues.add("Magicienne : Non, tu ne peux pas le manger ! Écoutez-moi, bande de rustres...");
                    dialogues.add("Magicienne : Mes réserves d'énergie astrale sont complètement épuisées et ma robe est pleine de poussière. Il nous faut faire une pause !");
                    dialogues.add("Ranger : Il faut qu'on fasse le point sur notre situation stratégique, on ne sait pas ce qui nous attend au bout de ce couloir.");
                    dialogues.add("Elfe : C'est quoi la stratélique ?");
                    dialogues.add("Ranger : Misère...");
                    dialogues.add("[UI Tuto] : Regardez le panneau sur la droite de l'écran pour suivre l'état de la compagnie.");
                    dialogues.add("[UI Tuto] : Vous pouvez y voir les Points de Vie (PV) et le Mana (PM) de chaque héros en temps réel.");
                    break;
                case 5:
                    dialogues.add("[Bruits métalliques et cris de guerre depuis la salle suivante...]");
                    dialogues.add("Nain : Prends ça dans les rotules, face de pet !");
                    dialogues.add("Barbare : CROM ! Taper la porte ! Taper les gardes !");
                    dialogues.add("Ranger : Ils ont trouvé la sortie ! Mais ils sont encerclés, il faut qu'on les aide !");
                    dialogues.add("[UI Tuto] : Utilisez les compétences spécifiques de chaque héros pour prendre l'avantage en combat.");
                    break;
            }
        } else if (maze instanceof NaheulbeukDungeon) {
            if (floorNumber == 4) {
                dialogues.add("Ranger : On est presque devant le bureau de Zangdar ! Préparez vos armes !");
            } else if (floorNumber == 5) {
                dialogues.add("=== ÉTAGE 5 : L'ANTICHAMBRE DU BUREAU DE ZANGDAR ===");
                dialogues.add("Magicienne : Attention ! C'est un Golem de Fer ! C'est une machine à baffes insensible aux armes simples !");
                dialogues.add("Nain : YAAAAAAAAAH ! (Il charge la hache en avant, frappe l'acier et se tord les poignets !)");
                dialogues.add("Zangdar (depuis son balcon) : Insolents ! Misérables cloportes ! Vous n'emporterez jamais la statuette de Gladeulfeurh ! Golem de fer, réduis-les en bouillie !");
            } else {
                dialogues.add("=== DESCENTE à L'ÉTAGE " + floorNumber + " ===");
                dialogues.add("Narrateur : La compagnie avance prudemment dans les ténèbres...");
            }
        }
        
        for (String d : dialogues) {
            menu.displayDialogue(d);
        }
    }

    private List<Item> getFilteredInventory(int categoryIndex) {
        List<Item> filtered = new java.util.ArrayList<>();
        for (Item item : team.getInventory()) {
            boolean isEquip = item instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.equipment.Equipment;
            boolean isUsable = item instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.usable.potion.Potion;
            if (categoryIndex == 0 && isUsable) filtered.add(item);
            else if (categoryIndex == 1 && isEquip) filtered.add(item);
            else if (categoryIndex == 2 && !isEquip && !isUsable) filtered.add(item);
        }
        return filtered;
    }

    @Override
    public void onInput(String action) {
        if (!running) return;

        if (subState == SubState.INVENTORY_SELECT_CATEGORY) {
            if ("ENTER".equals(action)) {
                int categoryIndex = menu.getMenuSelection();
                if (categoryIndex == 3) {
                    subState = SubState.EXPLORING;
                    if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                        ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest(null, null);
                    }
                } else {
                    selectedCategory = categoryIndex;
                    subState = SubState.INVENTORY_SELECT_ITEM;
                    
                    List<Item> filtered = getFilteredInventory(selectedCategory);
                    String[] options = new String[filtered.size() + 1];
                    for (int i = 0; i < filtered.size(); i++) {
                        options[i] = filtered.get(i).getName();
                    }
                    options[options.length - 1] = "Retour";
                    if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                        ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest("OBJETS", options);
                    }
                }
            } else if ("X".equals(action) || "ECHAP".equals(action)) {
                subState = SubState.EXPLORING;
                if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                    ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest(null, null);
                }
            }
            return;
        } else if (subState == SubState.PAUSE_MENU) {
            if ("ENTER".equals(action)) {
                int selection = menu.getMenuSelection();
                if (selection == 0) { // Sac
                    handleInventoryAction();
                } else if (selection == 1) { // Magie
                    menu.displayDialogue("Pas de magie disponible.");
                } else if (selection == 2) { // Compétences
                    menu.displayDialogue("Géré par l'interface dédié en combat.");
                } else if (selection == 3) { // Status
                    subState = SubState.STATUS_MENU;
                    if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                        ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).displayStatusScreen(team);
                    }
                } else if (selection == 4) { // Options
                    menu.displayDialogue("Les options sont accessibles via la touche M.");
                } else if (selection == 5) { // Sauvegarder
                    if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                        saveManager.saveQuickSave(1, team, maze, currentFloor);
                        ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).displaySaveSuccess(1);
                    }
                } else if (selection == 6) { // Quitter
                    com.badlogic.gdx.Gdx.app.exit();
                }
            } else if ("X".equals(action) || "ECHAP".equals(action)) {
                subState = SubState.EXPLORING;
                if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                    ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest(null, null);
                }
            }
            return;
        } else if (subState == SubState.INVENTORY_SELECT_ITEM) {
            if ("ENTER".equals(action)) {
                int selection = menu.getMenuSelection();
                List<Item> filtered = getFilteredInventory(selectedCategory);
                if (selection == filtered.size()) {
                    subState = SubState.INVENTORY_SELECT_CATEGORY;
                    if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                        ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest("CATEGORIES", new String[]{"Consommables", "Equipements", "Camelote", "Retour"});
                    }
                } else if (selection >= 0 && selection < filtered.size()) {
                    Item selected = filtered.get(selection);
                    selectedItemIndex = team.getInventory().indexOf(selected);
                    
                    if (selectedCategory == 2) {
                        menu.displayDialogue("\nÇa ne sert à rien, c'est juste de la camelote !");
                    } else {
                        subState = SubState.INVENTORY_SELECT_TARGET;
                        String[] options = new String[team.getMembers().size() + 1];
                        for (int i = 0; i < team.getMembers().size(); i++) {
                            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character member = team.getMembers().get(i);
                            options[i] = member.getName() + " (PV: " + member.getHealthPoint() + "/" + member.getMaxHealthPoint() + ")";
                        }
                        options[options.length - 1] = "Retour";
                        if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                            ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest("CIBLE_OBJET", options);
                        }
                    }
                }
            } else if ("X".equals(action) || "ECHAP".equals(action)) {
                subState = SubState.INVENTORY_SELECT_CATEGORY;
                if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                    ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest("CATEGORIES", new String[]{"Consommables", "Equipements", "Camelote", "Retour"});
                }
            }
            return;
        } else if (subState == SubState.INVENTORY_SELECT_TARGET) {
            if ("ENTER".equals(action)) {
                int targetIndex = menu.getMenuSelection();
                if (targetIndex >= 0 && targetIndex < team.getMembers().size()) {
                    Character target = team.getMembers().get(targetIndex);
                    Item selectedItem = team.getInventory().get(selectedItemIndex);
                    boolean used = selectedItem.use(target);
                    if (used) {
                        team.removeItem(selectedItem);
                        menu.displayMessage("\n" + target.getName() + " utilise " + selectedItem.getName() + " !");
                        if (isTutorial && currentFloor == 2 && !elfHealed && target.getClass().getSimpleName().equals("Elf")) {
                            this.elfHealed = true;
                            menu.displayDialogue(locManager.getString("TUTO_FLOOR_2_ELF_HEALED_1"));
                            if (maze.getGrid()[1][2].getEvent() instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.WoundedElfEvent) {
                                maze.getGrid()[1][2].getEvent().onItemUsed(selectedItem, target, maze);
                            }
                        }
                    } else {
                        menu.displayDialogue("\n" + target.getName() + " ne peut pas utiliser ça !");
                    }
                }
                subState = SubState.EXPLORING;
                if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                    ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest(null, null);
                }
            } else if ("X".equals(action) || "ECHAP".equals(action)) {
                subState = SubState.INVENTORY_SELECT_ITEM;
                List<Item> filtered = getFilteredInventory(selectedCategory);
                String[] options = new String[filtered.size() + 1];
                for (int i = 0; i < filtered.size(); i++) {
                    options[i] = filtered.get(i).getName();
                }
                options[options.length - 1] = "Retour";
                if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
                    ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest("OBJETS", options);
                }
            }
            return;
        }

        if (checkTutorialScripts(action)) {
            return;
        }

        if (System.currentTimeMillis() - lastMoveTime < 250) {
            return;
        }

        boolean moved = handleMovementAction(action);

        if (moved) {
            lastMoveTime = System.currentTimeMillis();
            handlePostMovement();
            maze.updateFogOfWar(team.getX(), team.getY(), 3);
            view.display(maze, team, currentFloor);
        }
    }

    private boolean handleMovementAction(String choice) {
        switch (choice) {
            case "ENTER":
                handleInteraction();
                return false;
            case "Z":
                team.setFacingDirection(1); // Nord
                return tryMove(0, -1);
            case "S":
                team.setFacingDirection(0); // Sud
                return tryMove(0, 1);
            case "Q":
                team.setFacingDirection(2); // Ouest
                return tryMove(-1, 0);
            case "D":
                team.setFacingDirection(3); // Est
                return tryMove(1, 0);
            case "1": team.setActiveLeaderIndex(0); return false;
            case "2": team.setActiveLeaderIndex(1); return false;
            case "3": team.setActiveLeaderIndex(2); return false;
            case "4": team.setActiveLeaderIndex(3); return false;
            case "5": team.setActiveLeaderIndex(4); return false;
            case "6": team.setActiveLeaderIndex(5); return false;
            case "7": team.setActiveLeaderIndex(6); return false;
            case "MENU_STATUS":
                menu.displayStatusScreen(team);
                return false;
            case "MENU_INVENTORY":
                handleInventoryAction();
                return false;
            case "MENU_EQUIPMENT":
                menu.displayMessage("Menu d'Equipement non implemente.");
                return false;
            case "MENU_MAGIC":
                menu.displayMessage("Menu de Magie/Competences non implemente.");
                return false;
            case "MENU_SAVE":
                handleSaveAction();
                return false;
            default:
                return false;
        }
    }

    private void handleSaveAction() {
        if (isTutorial) {
            menu.displayDialogue("\nSauvegarde Rapide impossible pendant le tutoriel ! Zangdar vous surveille...");
            return;
        }
        boolean quickSaved = saveManager.saveQuickSave(activeSlot, team, maze, currentFloor);
        if (quickSaved) {
            view.displaySaveSuccess(activeSlot);
        } else {
            view.displaySaveError();
        }
    }

    private boolean checkTutorialScripts(String choice) {
        if (isTutorial && currentFloor == 2 && elfJoined && !elfHealed) {
            if ("ZSQD".contains(choice) || choice.equals("ENTER")) {
                if (choice.equals("ENTER")) {
                    handleInteraction();
                } else {
                    menu.displayDialogue("\nL'Elfe est trop blessee pour avancer. Appuyez sur ECHAP pour ouvrir le menu, allez dans SAC, et utilisez la Potion de Soin sur l'Elfe.");
                    menu.clearMessages();
                }
                return true;
            }
        }
        return false;
    }

    private void handlePostMovement() {
        Cell currentCell = maze.getGrid()[team.getX()][team.getY()];
        boolean tookStairs = handleCellEvents(currentCell);

        if (running && !currentCell.hasMonster() && !tookStairs) {
            maze.moveMonsters(team);
            Cell newCell = maze.getGrid()[team.getX()][team.getY()];
            handleCellEvents(newCell);
        }
    }

    private void handleInventoryAction() {
        if (team.getInventory().isEmpty()) {
            menu.displayDialogue("Le sac à dos est vide !");
            return;
        }
        subState = SubState.INVENTORY_SELECT_CATEGORY;
        if (menu instanceof fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp) {
            ((fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic.HD2DGameApp)menu).setMenuRequest("CATEGORIES", new String[]{"Consommables", "Equipements", "Camelote", "Retour"});
        }
    }

    private boolean handleCellEvents(Cell currentCell) {
        if (currentCell.getEvent() != null) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult result = currentCell.getEvent().trigger(team);
            if (result != null && result.getDialogsToDisplay() != null) {
                for (String d : result.getDialogsToDisplay()) {
                    menu.displayDialogue(d);
                }
            }
        }
        
        if (currentCell.hasMonster()) {
            java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters = currentCell.getMonsters();

            gameContext.triggerBattle(monsters, () -> {
                view.displayDungeon(maze, team, currentFloor);
                currentCell.getMonsters().clear(); 
                
                if (isTutorial && currentFloor == 5) {
                    menu.displayDialogue(locManager.getString("TUTO_FLOOR_5_POST_NAIN_1"));
                    menu.displayDialogue(locManager.getString("TUTO_FLOOR_5_POST_RANGER_1"));
                }

                if (maze.isExpeditionComplete(currentFloor)) {
                    running = false; 
                }
            }, () -> {
                view.displayDungeon(maze, team, currentFloor);
                running = false; 
            }, () -> {
                currentCell.getMonsters().clear();
                view.displayDungeon(maze, team, currentFloor);
            });
        }

        if (currentCell.hasItem()) {
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.item.Item item = currentCell.getItem();
            boolean take = view.askPickupItem(item);

            if (take) {
                boolean added = team.addItem(item);
                if (added) {
                    menu.displayMessage("\n" + item.getName() + " ramasse(e), esperons que le Nain ne vole rien !");
                    currentCell.setItem(null); 
                } else {
                    menu.displayMessage("\nJe crois que le Nain essaye encore de porter trop d'objets !");
                }
            } else {
                menu.displayMessage("\nVous laissez le coffre intact.");
            }
        }

        if (currentCell.hasStairs()) {
            if (isTutorial) {
                if (currentFloor < 5) {
                    menu.displayMessage("\nVous prenez l'escalier pour fuir plus loin dans le cellier...");
                    currentFloor++;
                    view.displayTransitionScreen(currentFloor);
                    this.maze = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.TutorialDungeon();
                    this.maze.prepareFloor(currentFloor, team);
                    this.floorIntroPlayed = true;
                    playFloorIntro(currentFloor);
                } else {
                    running = false; 
                }
            } else {
                menu.displayMessage("\nVous trouvez un escalier lugubre qui descend dans les profondeurs...");
                currentFloor++;
                view.displayTransitionScreen(currentFloor);
                this.maze = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.NaheulbeukDungeon();
                this.maze.prepareFloor(currentFloor, team);
                this.floorIntroPlayed = true;
                playFloorIntro(currentFloor);
            }
            return true;
        }
        return false;
    }

    public boolean tryMove(int deltaX, int deltaY) {
        int targetX = team.getX() + deltaX;
        int targetY = team.getY() + deltaY;
        
        if (targetX >= 0 && targetX < maze.getWidth() && targetY >= 0 && targetY < maze.getHeight()) {
            Cell targetCell = maze.getGrid()[targetX][targetY];
            
            if (targetCell.hasBlockingEvent()) {
                fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.event.EventResult result = targetCell.getEvent().trigger(team);
                if (result != null && result.getDialogsToDisplay() != null) {
                    for (String d : result.getDialogsToDisplay()) {
                        menu.displayDialogue(d);
                    }
                }
                return false;
            }
            
            if (targetCell.isWalkable()) {
                team.move(deltaX, deltaY);
                return true;
            }
        }
        return false;
    }

    private void handleInteraction() {
        if (isTutorial && currentFloor == 2 && !elfHealed) {
            int targetX = team.getX();
            int targetY = team.getY();
            
            if (team.getFacingDirection() == 1) targetY -= 1; // Nord
            else if (team.getFacingDirection() == 0) targetY += 1; // Sud
            else if (team.getFacingDirection() == 2) targetX -= 1; // Ouest
            else if (team.getFacingDirection() == 3) targetX += 1; // Est
            
            if (targetX == 1 && targetY == 2) {
                if (!elfJoined) {
                    interactWithElf();
                }
            }
        }
    }
    
    private void interactWithElf() {
        if (!elfJoined) {
            menu.displayDialogue("Ranger : He l'Elfe, leve-toi, on doit sortir d'ici.");
            menu.displayDialogue("Elfe : *gemissement* J'ai trop mal a la tete... je peux a peine marcher.");
            menu.displayDialogue("Ranger : Bon, rejoins le groupe, mais il va falloir te rafistoler avant qu'on bouge d'ici.");
            
            fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf elfe = new fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.playerClasses.Elf();
            elfe.setHealthPoint(1);
            team.getMembers().add(elfe);
            elfJoined = true;
            menu.displayDialogue("\n[L'Elfe a rejoint le groupe, mais elle est gravement blessee !]");
            menu.clearMessages();
        }
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void exit() {
        this.running = false;
    }
}