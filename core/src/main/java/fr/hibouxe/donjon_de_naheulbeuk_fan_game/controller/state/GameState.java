package fr.hibouxe.donjon_de_naheulbeuk_fan_game.controller.state;

public interface GameState {
    void enter();
    void update(float deltaTime);
    void onInput(String action);
    void exit();
}
