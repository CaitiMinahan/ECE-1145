package hotciv.standard;
import hotciv.framework.City;
import hotciv.framework.Player;
import hotciv.standard.*;

public class CityImpl implements City {
    private int size;
    private Player owner;

    /* productionUnit = type of unit being
      produced at a certain city
     */
    private String productionUnit;
    private String focus;
    /*  treasury = number of money/production in the city's treasury
     that can be used to produce a
     unit in the city
     */
    private int treasury;

    // TODO: modified CityImpl function so we pass in the owner of the city
    public CityImpl(Player owner) {
        size = 1;
        treasury = 0;
        this.owner = owner;
    }
    public void setTreasury(int t){
        this.treasury = t;
    }
    @Override
    public Player getOwner() { return owner; }
    @Override
    public int getSize() { return size; }
    @Override
    public int getTreasury() { return treasury; }
    @Override
    public String getProduction() { return productionUnit; }
    @Override
    public String getWorkforceFocus() { return focus; }
}
