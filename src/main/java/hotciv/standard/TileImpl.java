package hotciv.standard;
import hotciv.framework.Tile;
import hotciv.standard.*;
public class TileImpl implements Tile {
    private String terrain;
    public TileImpl(String terrain){
        this.terrain = terrain;
    }
    @Override
    public String getTypeString() {
        return terrain;
    }
}
