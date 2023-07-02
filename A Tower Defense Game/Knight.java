import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.concurrent.*;

/**
 * Knight: melee tower, intended to be the first tower the player uses
 */
public class Knight extends Tower
{
    public Knight() {
        super(1, 100, 1);
        towerExecService = Executors.newScheduledThreadPool(1);
    }
    
    // for knight upgrades
    private KnightUpgradeData ud = new KnightUpgradeData();
    private KnightUpgradeButton damageUpgradeButton;
    private KnightUpgradeButton fireRateUpgradeButton;
    protected ScheduledFuture<?> executor; // used for timing projectile firing
    private ScheduledExecutorService towerExecService = null;
    
    @Override
    protected void registerFiringBehavior() {
        executor = towerExecService.scheduleAtFixedRate(() -> {
            Enemy e = getClosestValidEnemy();
            if (e != null) {
                getWorld().addObject(new KnightAttack(damage, getRotation(), 1000/fireRate), e.getX(), e.getY());
            }
        }, 0, (long)(1000/fireRate), TimeUnit.MILLISECONDS);
    }
    
    @Override 
    protected void loadTowerImage() {
        setImage(".\\images\\towers\\knight.png");
        GreenfootImage i = getImage();
        i.scale((int)(i.getWidth() * (1.75)), (int)(i.getHeight() * 1.75));
    }
    
    @Override
    public void act() {
        super.act();
        
        if (Greenfoot.mouseClicked(this)) {
            setupUpgradeUI(getWorldOfType(TowerDefenseWorld.class));
        }
    }
    
    private void setupUpgradeUI(TowerDefenseWorld w) {
        w.clearUI();
        
        BackButton back = new BackButton();
        w.addObject(back, 1000, 30);
        w.uiElements.add(back);
        
        Text title = new Text("Upgrading Knight", 32);
        w.addObject(title, 1120, 50);
        w.uiElements.add(title);
        
        Text u1 = new Text("Upgrade Damage: " + ud.damageUpgrade.tier + "/" + ud.damageUpgrade.maxTiers, 24);
        w.addObject(u1, 1075, 300);
        w.uiElements.add(u1);
        
        Text u1p = null; 
        if (ud.damageUpgrade.tier != ud.damageUpgrade.maxTiers)
            u1p = new Text("Cost: "+ (int)(ud.damageUpgrade.initialCost + (ud.damageUpgrade.tier * (ud.damageUpgrade.initialCost/2))), 20);
        else
            u1p = new Text("Upgrade maxed!", 20);
        w.addObject(u1p, 1075, 325);
        w.uiElements.add(u1p);
        
        Text u2 = new Text("Upgrade Swing Rate: " + ud.fireRateUpgrade.tier + "/" + ud.fireRateUpgrade.maxTiers, 24);
        w.addObject(u2, 1085, 400);
        w.uiElements.add(u2);
        
        Text u2p = null;
        if (ud.fireRateUpgrade.tier != ud.fireRateUpgrade.maxTiers)
            u2p = new Text("Cost: "+ (int)(ud.fireRateUpgrade.initialCost + (ud.fireRateUpgrade.tier * (ud.fireRateUpgrade.initialCost/2))), 20);
        else
            u2p = new Text("Upgrade maxed!", 20);
        w.addObject(u2p, 1075, 425);
        w.uiElements.add(u2p);
        
        damageUpgradeButton = new KnightUpgradeButton((Knight a) -> {
            int cost = (int)(ud.damageUpgrade.initialCost + (ud.damageUpgrade.tier * (ud.damageUpgrade.initialCost/2)));
            if (w.getMoney() >= cost) {
                w.modifyMoney(-cost);
                ud.damageUpgrade.tier++;
                a.damage += ud.damageUpgrade.incrementValue;
                a.range += 5;
                a.setupUpgradeUI(w);
                executor.cancel(false);
                executor = null;
                registerFiringBehavior();
                return;
            } else {
                w.message("You don't have enough money!", 24);
            }
        }, this);
        
        fireRateUpgradeButton = new KnightUpgradeButton((Knight a) -> {
            int cost = (int)(ud.fireRateUpgrade.initialCost + (ud.fireRateUpgrade.tier * (ud.fireRateUpgrade.initialCost/2)));
            if (w.getMoney() >= cost) {
                w.modifyMoney(-cost);
                ud.fireRateUpgrade.tier++;
                a.fireRate += ud.fireRateUpgrade.incrementValue;
                a.range += 5;
                a.setupUpgradeUI(w);
                executor.cancel(false);
                executor = null;
                registerFiringBehavior();
                return;
            } else {
                w.message("You don't have enough money!", 24);
            }
        }, this);
        
        if (ud.damageUpgrade.tier != ud.damageUpgrade.maxTiers) {
            w.addObject(damageUpgradeButton, 1250, 300);
            w.uiElements.add(damageUpgradeButton);
        }
        
        if (ud.fireRateUpgrade.tier != ud.fireRateUpgrade.maxTiers) {
            w.addObject(fireRateUpgradeButton, 1250, 400);
            w.uiElements.add(fireRateUpgradeButton);
        }
        
        //Text rangeDesp = new Text("Upgrading either damage or fire rate ");
    }
}
