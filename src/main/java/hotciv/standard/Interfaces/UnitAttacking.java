package hotciv.standard.Interfaces;
import hotciv.framework.*;
import hotciv.standard.GameImpl;
import hotciv.standard.UnitImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface UnitAttacking {
    public boolean canAttackerBeatDefender(UnitImpl attacker, UnitImpl defender, Position from, Position to, MutableGame game);
    public int getAttackingUnitStrength(UnitImpl attacker, Position from, MutableGame game);
    public int getDefendingUnitStrength(UnitImpl defender, Position to, MutableGame game);
    public int getTerrainMultiplier(UnitImpl unit);
    public int getNumFriendlyTiles(Position from, MutableGame game);
    public static Iterator<Position> get8neighborhoodIterator(Position center){
        List<Position> list = new ArrayList<>();
        // Define the 'delta' to add to the row for the 8 positions
        int[] rowDelta = new int[] {-1, -1, 0, +1, +1, +1, 0, -1};
        // Define the 'delta' to add to the colum for the 8 positions
        int[] columnDelta = new int[] {0, +1, +1, +1, 0, -1, -1, -1};

        for (int index = 0; index < rowDelta.length; index++) {
            int row = center.getRow() + rowDelta[index];
            int col = center.getColumn() + columnDelta[index];
            if (row >= 0 && col >= 0
                    && row < GameConstants.WORLDSIZE
                    && col < GameConstants.WORLDSIZE)
                list.add(new Position(row, col));
        }
        return list.iterator();
    }

    public static Iterable<Position> get8neighborhoodOf(Position center) {
        final Iterator<Position> iterator = get8neighborhoodIterator(center);
        Iterable<Position> iterable = new Iterable<Position>() {
            @Override
            public Iterator<Position> iterator() {
                return iterator;
            }
        };
        return iterable;
    }
}

