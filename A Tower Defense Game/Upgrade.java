/**
 * Represents metadata for a single tower upgrade (name, tiers, costs, etc.)
 * Upgrade cost formula: initcost + (tier * initcost/2);
 */
public class Upgrade  
{
    public final String name;
    public int tier = 0;
    public final int maxTiers;
    public final double incrementValue;
    public final int initialCost;
    public Upgrade(String name, int maxTiers, double incrementValue, int initialCost) {
        this.name = name;
        tier = 0;
        this.maxTiers = maxTiers;
        this.incrementValue = incrementValue;
        this.initialCost = initialCost;
    }
}
