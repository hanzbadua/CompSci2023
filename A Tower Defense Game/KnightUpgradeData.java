/**
 * Represents, well, a Knight tower's upgrade data
 * Technically "upgrade data" classes should be extending a parent "upgrade data" class
 * But compared to everything else that would be so much work
 * Considering upgrades are different based on the tower
 * And making it work with UI as well would be lots of fun
 */
public class KnightUpgradeData  
{
    public Upgrade damageUpgrade;
    public Upgrade fireRateUpgrade;
    
    public KnightUpgradeData() {
        damageUpgrade = new Upgrade("Damage", 5, 1, 8);
        fireRateUpgrade = new Upgrade("Swing Rate", 5, 0.4, 12);
    }
}
