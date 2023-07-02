import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Go to the main tower UI
 * used on tower upgrade UIs    
 */
public class BackButton extends UIElement
{
    public BackButton() {
        setImage(".\\images\\ui\\Back.png");
    }
    
    @Override
    public void act()
    {
        if (Greenfoot.mouseClicked(this)) {
            TowerDefenseWorld t = getWorldOfType(TowerDefenseWorld.class);
            t.goToMainMenu();
        }
    }
}
