// enums are special objects that contain constants grouped in a specific category
// good examples of enum candidates include but not limited to: months, basic colors, specific states, days of the week, etc.
// this enum represents difficulties

/*                             Easy, Normal, Hard
    Initial Speed              Same for all difficulties (2)
    Powerups req. for upgrade  10    15      20
    Lives                      3     2       1
    Powerups exist             6     5       4
    Enemies                    2     3       4
    Enemy speed                1-2   1-2     1-3    <-- speed ranges = fluctuate constantly between the ranges
    Enemy rotation update      220   260     220
    (see Enemy.act() method comments for info about enemy rotation update)
    
    And maybe difficulty affects more things that I forgot about but I don't think so, who knows
*/
public enum Difficulty {
    EASY,
    NORMAL,
    HARD
}