import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Base class for all Enemy actors
 */
public abstract class Enemy extends Actor
{
    protected double hp = 0;
    protected int speed = 0;
    protected int money = 0;
    
    // keep track of the current navnode the enemy is on
    private int navnodeCounter = 0;
    
    protected Enemy(double h, int s, int m) {
        hp = h;
        speed = s;
        money = m;
        loadEnemyImage();
    }
    
    @Override 
    public void act() {
        movement();
        deathLogic();
    }
    
    // handles movement between navnodes and such
    private void movement() {
        // movement
        move(speed);
        
        // go to next navnode
        Navnode[] navnodes = getWorldOfType(TowerDefenseWorld.class).getCurrentNavnodeCollection();
        
        // if last navnode, remove
        if (navnodeCounter == navnodes.length) {
            TowerDefenseWorld w = getWorldOfType(TowerDefenseWorld.class);
            // damage done is hp remaining
            w.modifyLives((int)-hp);
            w.removeObject(this);
            return;
        }
        
        // go to next navnode
        Navnode next = navnodes[navnodeCounter];
        turnTowards(next.getX(), next.getY());
        
        // and increment
        if (intersects(next)) {
            navnodeCounter += 1;
        }
    }
    
    // stuff that happens when enemy hp <= 0
    private void deathLogic() {
        if (hp <= 0) {
            TowerDefenseWorld w = getWorldOfType(TowerDefenseWorld.class);
            w.modifyMoney(money);
            w.removeObject(this);
            return;
        }
    }
    
    // be sure to specify negative argument if required
    public void modifyHealth(double h) {
        hp += h;
    }
    
    protected abstract void loadEnemyImage();
}
