package fr.hibouxe.donjon_de_naheulbeuk_fan_game.model.dungeon.theme;

public class DungeonTheme {
    private String name;
    private float tileSize = 1.0f;
    private String texturePath;
    private String wallTexturePath;
    private String floorModel;
    private String wallModel;
    private String doorModel;
    private String doorFrameModel;
    private float[] wallOffset = {0f, 0f, 0f};

    public DungeonTheme() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public float getTileSize() { return tileSize; }
    public void setTileSize(float tileSize) { this.tileSize = tileSize; }
    public String getTexturePath() { return texturePath; }
    public void setTexturePath(String texturePath) { this.texturePath = texturePath; }
    public String getWallTexturePath() { return wallTexturePath; }
    public void setWallTexturePath(String wallTexturePath) { this.wallTexturePath = wallTexturePath; }
    public String getFloorModel() { return floorModel; }
    public void setFloorModel(String floorModel) { this.floorModel = floorModel; }
    public String getWallModel() { return wallModel; }
    public void setWallModel(String wallModel) { this.wallModel = wallModel; }
    public String getDoorModel() { return doorModel; }
    public void setDoorModel(String doorModel) { this.doorModel = doorModel; }
    public String getDoorFrameModel() { return doorFrameModel; }
    public void setDoorFrameModel(String doorFrameModel) { this.doorFrameModel = doorFrameModel; }
    public float[] getWallOffset() { return wallOffset; }
    public void setWallOffset(float[] wallOffset) { this.wallOffset = wallOffset; }
}

