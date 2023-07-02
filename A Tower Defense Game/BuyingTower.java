import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class to help with purchasing towers
 */
public abstract class BuyingTower extends Actor
{
    private TowerRangeHighlighter highlighter;
    
    public BuyingTower() {
        setClassImage();
        highlighter = new TowerRangeHighlighter(getRange());
        highlighter.hide();
    }
    
    @Override
    public void act()
    {
        MouseInfo m = Greenfoot.getMouseInfo();
        TowerDefenseWorld w = getWorldOfType(TowerDefenseWorld.class);
        
        
        if (m != null) {
            setLocation(m.getX(), m.getY());
            highlighter.setLocation(m.getX(), m.getY());
            
            Canvas c = (Canvas) getOneIntersectingObject(Canvas.class);
            if (getOneIntersectingObject(UICanvas.class) != null) {
                highlighter.hide();
                
                if (Greenfoot.mouseClicked(null)) {
                    w.isBusy = false;
                    w.removeObject(highlighter);
                    w.removeObject(this);
                    return;
                }
            } else if (c != null) {
                Color a = c.getImage().getColorAt(getX(), getY());
                
                if (getOneIntersectingObject(Tower.class) == null && a.getGreen() >= 200) {
                    highlighter.toValidState();
                    if (Greenfoot.mouseClicked(null)) {
                        if (w.getMoney() < getPrice()) {
                            w.message("You don't have enough money!", 24);
                        } else {
                            w.modifyMoney(-getPrice());
                            w.addObject(getTower(), getX(), getY());
                            w.isBusy = false;
                            w.message("", 24);
                            w.removeObject(highlighter);
                            w.removeObject(this);
                            return;
                        }
                    }
                } else {
                    highlighter.toInvalidState();
                    if (Greenfoot.mouseClicked(null)) {
                        w.message("You can't place the tower here!", 24);
                    }
                }
            }
        }
    }
    
    protected abstract void setClassImage();
    
    // set price here
    protected abstract int getPrice();
    
    // make sure this value matches the range of the actual tower (? extends Tower)
    protected abstract int getRange();
    
    // tower to place when buying
    protected abstract Tower getTower();
    
    @Override
    public void addedToWorld(World w) {
        w.addObject(highlighter, getX(), getY());
    }
}
