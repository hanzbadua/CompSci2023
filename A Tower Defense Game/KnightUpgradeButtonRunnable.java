/**
 * Functional interface to pass methods as arguments
 * See https://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html for more info
 * This functional interface is used for upgrade buttons
 */
@FunctionalInterface
public interface KnightUpgradeButtonRunnable  
{
    public abstract void run(Knight k);
}
