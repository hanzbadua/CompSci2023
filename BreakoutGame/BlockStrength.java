import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * BlockStrength is an enum that represents different block strengths (identified by color)
 * Hitting a block with a ball decreases the block strength by one enumeration value (THREE->TWO->ONE)
 * If the block is already at min strength (one/green) then the block is destroyed (ONE->null)
 * 
 * Also has multiple helper methods
 * 
 * Strength/color block correspondance:
 * One/green
 * Two/orange
 * Three/red
 */
public enum BlockStrength 
{
    // possible enum values
    ONE, 
    TWO,
    THREE;
    
    // returns the corresponding color depending on strength (see class comment)
    public Color getColor() {
        // we don't need to break out of a case if we're returning (redundant)
        switch(this) {
            case ONE:
                // the Color class has a bunch of static final vars such as GREEN and other basic colors
                // which are already premade Color objects representing basic colors
                return Color.GREEN;
            case TWO:
                return Color.ORANGE;
            case THREE:
                return Color.RED;
            // the default case occurs if none of the ones above do
            // however tho, this default case will never occur as every existing case in this switch statement covers every possible value
            // but this is required as methods with a return value need a guaranteed value to return
            // ex: if we don't have this the compiler will think of a scenario where none of the cases above occurs (which we know won't happen)
            // and that this method won't return anything (which can't happen)
            // the default case satisfies the required return value
            default:
                return null;
        }
    }
    
    // Returns the next lower strength relative to the current one
    // Returns null if strength is already lowest possible (ONE)
    // note: null block strength = remove block
    public BlockStrength downgrade() {
        switch (this) {
            case ONE: 
                return null;
            case TWO:
                return ONE;
            case THREE:
                return TWO;
            default: // required for compilation
                return null; 
        }
    }
    
    // Get a random BlockStrength 
    public static BlockStrength random() {
        switch (Greenfoot.getRandomNumber(4)) { // 0-3
            // 50% chance for strength ONE block, 25% chance each for str TWO/THREE
            case 0:
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            default: // req for compilation
                return null;
        }
    }
}
