package hotciv.standard;
import hotciv.framework.City;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.standard.*;

public class CityImpl implements City {
    private int size;
    private Player owner;
    private int populationSize;

    //type of unit being produced at a certain city
    public String productionUnit;
    public String focus;
    /*  treasury = number of money/production in the city's treasury
     that can be used to produce a
     unit in the city
     */
    private int treasury;

    // TODO: modified CityImpl function so we pass in the owner of the city
    public CityImpl(Player owner) {
        size = 1;
        treasury = 0;
        focus = GameConstants.foodFocus;
        productionUnit = GameConstants.ARCHER;
        this.owner = owner;
    }
    public void setTreasury(int t){
        this.treasury = t;
    }

    public void setSize(int size) { this.size = size;}
    public void setPopulationSize(int popSize) {this.populationSize = popSize;}
    public int getPopulationSize() { return this.populationSize;}
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
    public void setWorkForceFocus(String focus1) { focus = focus1; }

    // new function to set the owner of a city in case of battle
    public void setOwner(Player player) { this.owner = player;}
}
