import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Tiles are the blocks that make up terrain rows
 * A tile can represent a single obstacle, or a single safe space for the frog to jump on to
 */
public class Tile extends GameActor
{
    // required information for stuff to work
    // terrain row number
    public final int row;
    // tile id/number
    public final int id;
    
    @Override
    public boolean isDangerous() {
        return _isDangerous;
    }
    
    // "closed" tiles aren't traversable
    public boolean isClosed() {
        return _isClosed;
    }
    
    private boolean _isDangerous;
    private boolean _isClosed;
    
    // change tile image/behavior based on tiletype
    // tile is also dangerous based on what type of tile it is
    public Tile(TileType t, int row, int id) {
        this.row = row;
        this.id = id;
        _isDangerous = false;
        _isClosed = false;
        switch (t) {
            case FIELD_TILE:
                setImage(".\\images\\FieldTile.png");
                break;
            case ROAD_TILE:
                setImage(".\\images\\RoadTile.png");
                break;
            case WATER_TILE:
                setImage(".\\images\\WaterTile.png");
                _isDangerous = true;
                break;
            case FOREST_TILE:
                setImage(".\\images\\ForestTile.png");
                _isClosed = true;
                break;
        }
    }
    
    public static TileType getTileTypeByNumber(int num) {
        switch (num) {
            case 0:
                return TileType.FIELD_TILE;
            case 1:
                return TileType.ROAD_TILE;
            case 2:
                return TileType.WATER_TILE;
            case 3:
                return TileType.FOREST_TILE;
            default:
                return null;
        }
    }
}
