package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

public class HD2DCamera {
    private float cameraX;
    private float cameraY = 5.0f; //Hauteur de la camra
    private float cameraZ;
    private float pitch = -45.0f; //Angle d'inclinaison HD-2D

    private float targetX;
    private float targetZ;

    public void updateTarget(Team team) {
        float tilseSize = 1.0f; //1m par case du donjon
        this.targetX = team.getX() * tilseSize + 0.5f;
        this.targetZ = team.getY() * tilseSize + 0.5f;

        this.cameraX = targetX;
        this.cameraZ = targetZ + 4.0f; //recul vers le bas pour l'angle 45
    }

    public float getCameraX() { return cameraX; }
    public float getCameraY() { return cameraY; }
    public float getCameraZ() { return cameraZ; }
    public float getPitch() { return pitch; }
    public float getTargetX() { return targetX; }
    public float getTargetZ() { return targetZ; }
}
