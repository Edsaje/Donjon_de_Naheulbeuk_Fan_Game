package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller;

public interface GameContext {
    void triggerBattle(java.util.List<fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character> monsters, Runnable onVictory, Runnable onDefeat, Runnable onFlee);
    void resumeExploration();
    void goToMainMenu();
    void goToVillage();
    void exitGame();
    void pushState(fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state.GameState state);
    void popState();
}
