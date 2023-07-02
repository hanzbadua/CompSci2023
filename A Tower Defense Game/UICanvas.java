import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents the base for the non-game area containing the tower menu (buying new towers, upgrading current towers, etc.)
 * Should be 320 * 720 resolution placed at 1120,360
 */
public class UICanvas extends Actor
{
    public UICanvas() {
        setImage(".\\images\\ui\\TowerUIBase.png");
        getImage().scale(320, 720);
    }
    
    @Override
    public void addedToWorld(World w) {
        setLocation(1120, 360);
    }
}
