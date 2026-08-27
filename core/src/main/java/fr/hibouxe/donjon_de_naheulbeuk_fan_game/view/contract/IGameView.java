package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.contract;

public interface IGameView {
    void switchToHubView();
    void switchToExplorationView();
    void displayTransitionScreen(int targetFloor);
    boolean isAnyMenuOpen();
    
    IExplorationView getExplorationView();
    IMenuView getMenuView();
    ICombatView getCombatView();

    boolean onInput(String action);
    void setMenuRequest(String title, String[] options);
    int getMenuSelection();
    void resetMenuSelection();
}
