import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * For Knight tower upgrades
 */
public class KnightUpgradeButton extends UIElement
{
    private KnightUpgradeButtonRunnable onClick = null;
    private Knight r = null;
    public KnightUpgradeButton(KnightUpgradeButtonRunnable onClick, Knight ref) {
        this.onClick = onClick;
        r = ref;
        setImage(".\\images\\ui\\Plus.png");
        GreenfootImage i = getImage();
        i.scale((int)(i.getWidth() * 0.75), (int)(i.getWidth() * 0.75));
    }
    
    @Override
    public void act() {
        if (Greenfoot.mouseClicked(this))
            onClick.run(r);
    }
}
