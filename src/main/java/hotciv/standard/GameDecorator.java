package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.Interfaces.*;

public class GameDecorator implements MutableGame {
    protected MutableGame decoratedGame;
    public GameDecorator(MutableGame mutableGame){

        this.decoratedGame = mutableGame;
    }
    public Tile getTileAt( Position p ){
        return decoratedGame.getTileAt(p);
    }
    public Unit getUnitAt( Position p ){
        return decoratedGame.getUnitAt(p);
    }
    public City getCityAt( Position p ){
        return decoratedGame.getCityAt(p);
    }
    public Player getPlayerInTurn(){
        return decoratedGame.getPlayerInTurn();
    }
    public Player getWinner() {
        return decoratedGame.getWinner();
    }
    public int getAge(){
        return decoratedGame.getAge();
    }
    public void setupWorldLayout(WorldLayout worldLayoutStrategy){ decoratedGame.setupWorldLayout(worldLayoutStrategy);}
    public Unit getCurrentUnit() { return decoratedGame.getCurrentUnit(); }
    public void setCurrentUnit(Unit u) { decoratedGame.setCurrentUnit(u);}
    public Player getCurrentPlayer(){ return decoratedGame.getCurrentPlayer(); }
    public Position getPositionFromUnit(UnitImpl u) { return decoratedGame.getPositionFromUnit(u); }
    public void killUnit(Position positionToClear) { decoratedGame.killUnit(positionToClear); }
    public boolean canUnitAttack(Unit unitToCheck) { return decoratedGame.canUnitAttack(unitToCheck); }
    public String getUnitActionStringType() { return decoratedGame.getUnitActionStringType(); }
    public void placeCity(Position position, Player player) { decoratedGame.placeCity(position, player);}
    public boolean cityExistsAt(Position position) { return decoratedGame.cityExistsAt(position); }
    public City createCity(Player player) { return decoratedGame.createCity(player); }
    public void setCurrentCity(City city) { decoratedGame.setCurrentCity(city); }
    public void setTurnCount(int turnCount) { decoratedGame.setTurnCount(turnCount); }
    public int getTurnCount() { return decoratedGame.getTurnCount(); }
    public void setAge(int age) { decoratedGame.setAge(age); }
    @Override
    public boolean moveUnit(Position from, Position to) {
        boolean success = decoratedGame.moveUnit(from, to);
        if (success) {
            Unit unit = decoratedGame.getUnitAt(to);
            logAction(String.format("%s moves %s from %s to %s",
                    decoratedGame.getCurrentPlayer(), unit.getTypeString(), from, to));
        }
        return success;
    }
    @Override
    public void endOfTurn() {
        decoratedGame.endOfTurn();
        logAction(String.format("%s ends turn", decoratedGame.getCurrentPlayer()));
    }
    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {
        decoratedGame.changeWorkForceFocusInCityAt(p, balance);
        logAction(String.format("%s changes work force focus in city at %s to %s",
                decoratedGame.getCurrentPlayer(), p, balance));
    }
    @Override
    public void changeProductionInCityAt(Position p, String unitType) {
        decoratedGame.changeProductionInCityAt(p, unitType);
        logAction(String.format("%s changes production in city at %s to %s",
                decoratedGame.getCurrentPlayer(), p, unitType));
    }
    public void performUnitActionAt( Position p ){
        decoratedGame.performUnitActionAt(p);
        logAction(String.format("%s performs an action at %s", decoratedGame.getCurrentPlayer(), p));
    }

    // New logging function
    private void logAction(String message) {
        System.out.println(message);
    }
}
