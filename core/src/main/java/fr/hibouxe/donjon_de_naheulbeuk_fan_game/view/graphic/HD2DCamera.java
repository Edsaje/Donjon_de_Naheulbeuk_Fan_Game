package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Team;

public class HD2DCamera {
    private float cameraX;
    private float cameraY = 10.0f; //Hauteur de la caméra
    private float cameraZ;
    private float pitch = -45.0f; //Angle d'inclinaison HD-2D

    private float targetX;
    private float targetZ;

    public void updateTarget(Team team) {
        float tilseSize = 2.0f; //2m par case du donjon
        this.targetX = team.getX() * tilseSize;
        this.targetZ = team.getY() * tilseSize;

        this.cameraX = targetX;
        this.cameraZ = targetZ + 8.0f; //recul vers le bas pour l'angle 45°
    }

    public float getCameraX() { return cameraX; }
    public float getCameraY() { return cameraY; }
    public float getCameraZ() { return cameraZ; }
    public float getPitch() { return pitch; }
    public float getTargetX() { return targetX; }
    public float getTargetZ() { return targetZ; }
}
