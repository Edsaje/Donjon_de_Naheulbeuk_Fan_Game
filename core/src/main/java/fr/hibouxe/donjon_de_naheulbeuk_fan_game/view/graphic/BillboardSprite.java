package fr.hibouxe.donjon_de_naheulbeuk_fan_game.view.graphic;

import fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.entity.Character;

public class BillboardSprite {
    private Character character;
    private float worldX;
    private float worldY = 1.0f; //ancrage debout au sol
    private float worldZ;
    private float rotationY; //rotation face caméra sur l'axe Y

    public BillboardSprite(Character character, float gridX, float gridY) {
        this.character = character;
        float tileSize = 1.0f;
        this.worldX = gridX * tileSize + 0.5f;
        this.worldZ = gridY * tileSize + 0.5f;
    }

    public void faceCamera(float cameraX, float cameraZ) {
        double angleRad = Math.atan2(cameraX - worldX, cameraZ - worldZ);
        this.rotationY = (float) Math.toDegrees(angleRad);
    }

    public Character getCharacter() { return character; }
    public float getWorldX() { return worldX; }
    public float getWorldY() { return worldY; }
    public float getWorldZ() { return worldZ; }
    public float getRotationY() { return rotationY; }

}
