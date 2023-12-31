package hotciv.view;

import hotciv.framework.*;
import minidraw.framework.*;
import minidraw.standard.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** CivDrawing is a specialized Drawing (model component) from
 * MiniDraw that dynamically builds the list of Figures for MiniDraw
 * to render the Unit and other information objects that are visible
 * in the Game instance.
 *
 * TODO: This is a TEMPLATE for the SWEA Exercise! This means
 * that it is INCOMPLETE and that there are several options
 * for CLEANING UP THE CODE when you add features to it!

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Department of Computer Science
     Aarhus University
   
   Please visit http://www.baerbak.com/ for further information.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class CivDrawing 
  implements Drawing, GameObserver {
  
  protected Drawing delegate;
  /** store all moveable figures visible in this drawing = units */
  protected Map<Unit,UnitFigure> unitFigureMap;
  protected Map<City, CityFigure> cityFigureMap;

  /** the Game instance that this CivDrawing is going to render units
   * from */
  protected Game game;
  
  public CivDrawing(DrawingEditor editor, Game game ) {
    super();
    this.delegate = new StandardDrawing();
    this.game = game;
    this.unitFigureMap = new HashMap<>();
    this.cityFigureMap = new HashMap<>();

    // Initialize ageText to see if this resolves it from being null when turnEnds is called
    ageText = new TextFigure("", new Point(GfxConstants.AGE_TEXT_X, GfxConstants.AGE_TEXT_Y));
    delegate.add(ageText);  // Add ageText to the delegate for rendering

    // register this unit drawing as listener to any game state
    // changes...
    game.addObserver(this);
    // ... and build up the set of figures associated with
    // units in the game.
    defineUnitMap();
    // and the set of 'icons' in the status panel
    defineIcons();
  }
  
  /** The CivDrawing should not allow client side
   * units to add and manipulate figures; only figures
   * that renders game objects are relevant, and these
   * should be handled by observer events from the game
   * instance. Thus this method is 'killed'.
   */
  public Figure add(Figure arg0) {
    throw new RuntimeException("Should not be used...");
  }


  /** erase the old list of units, and build a completely new
   * one from scratch by iterating over the game world and add
   * Figure instances for each unit in the world.
   */
  protected void defineUnitMap() {
    // ensure no units of the old list are accidental in
    // the selection!
    clearSelection();

    // remove all unit figures in this drawing
    removeAllUnitFigures();

    // iterate world, and create a unit figure for
    // each unit in the game world, as well as
    // create an association between the unit and
    // the unitFigure in 'unitFigureMap'.
    Position p;
    for ( int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
      for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
        p = new Position(r,c);
        Unit unit = game.getUnitAt(p);
        if ( unit != null ) {
          String type = unit.getTypeString();
          // convert the unit's Position to (x,y) coordinates
          Point point = new Point( GfxConstants.getXFromColumn(p.getColumn()),
                                   GfxConstants.getYFromRow(p.getRow()) );
          UnitFigure unitFigure =
            new UnitFigure( type, point, unit );
          unitFigure.addFigureChangeListener(this);
          unitFigureMap.put(unit, unitFigure);

          // get the unit move count and update TODO
          int moveCount = unit.getMoveCount();


          // also insert in delegate list as it is
          // this list that is iterated by the
          // graphics rendering algorithms
          delegate.add(unitFigure);
        }
      }
    }
  }

  // added method to define the city figures
  public void defineCityMap() {
    // ensure no units of the old list are accidental in
    // the selection!
    clearSelection();

    // remove all city figures in this drawing
//    removeAllCityFigures();
    // Iterate over the game world and create city figures
    Position p;
    for (int r = 0; r < GameConstants.WORLDSIZE; r++) {
      for (int c = 0; c < GameConstants.WORLDSIZE; c++) {
        p = new Position(r, c);
        City city = game.getCityAt(p);
        if (city != null) {
          // convert the city's Position to (x,y) coordinates
          Point point = new Point(GfxConstants.getXFromColumn(p.getColumn()),
                  GfxConstants.getYFromRow(p.getRow()));
          CityFigure cityFigure = new CityFigure(city, point);
          cityFigure.addFigureChangeListener(this);
          // add the city to the map
          cityFigureMap.put(city, cityFigure);

          // get the city production and update TODO

          // also insert in delegate list
          delegate.add(cityFigure);
        }
      }
    }
  }

  /** remove all unit figures in this
   * drawing, and reset the map (unit->unitfigure).
   * It is important to actually remove the figures
   * as it forces a graphical redraw of the screen
   * where the unit figure was.
   */
  protected void removeAllUnitFigures() {
    for (Unit u : unitFigureMap.keySet()) {
      UnitFigure uf = unitFigureMap.get(u);
      delegate.remove(uf);
    }
    unitFigureMap.clear();
  }
  protected void removeAllCityFigures() {
    if(cityFigureMap.size() > 0) {
      for (City c : cityFigureMap.keySet()) {
        CityFigure cf = cityFigureMap.get(c);
        delegate.remove(cf);
      }
      cityFigureMap.clear();
    }
  }



  // todo: create all protected image figures
  protected ImageFigure turnShieldIcon, unitShieldIcon, cityShieldIcon, productionIcon, workForceFocusIcon;
  protected TextFigure ageText, moveText;
  protected void defineIcons() {
    turnShieldIcon =
      new ImageFigure( "redshield",
                       new Point( GfxConstants.TURN_SHIELD_X,
                                  GfxConstants.TURN_SHIELD_Y ) );
    unitShieldIcon =
            new ImageFigure( GfxConstants.NOTHING,
                    new Point( GfxConstants.UNIT_SHIELD_X,
                            GfxConstants.UNIT_SHIELD_Y ) );
    cityShieldIcon =
            new ImageFigure( GfxConstants.NOTHING,
                    new Point( GfxConstants.CITY_SHIELD_X,
                            GfxConstants.CITY_SHIELD_Y ) );

    // icon for the current unit being produced in a city
    productionIcon =
            new ImageFigure (GfxConstants.NOTHING,
                    new Point(GfxConstants.CITY_PRODUCTION_X, GfxConstants.CITY_PRODUCTION_Y));
    // icon for the balance of the city
    workForceFocusIcon =
            new ImageFigure (GfxConstants.NOTHING,
                    new Point(GfxConstants.WORKFORCEFOCUS_X, GfxConstants.WORKFORCEFOCUS_Y));
    // text for the current move
    moveText = new TextFigure("",
            new Point(GfxConstants.UNIT_COUNT_X, GfxConstants.UNIT_COUNT_Y));
    // text for the age
    ageText = new TextFigure(Integer.toString(game.getAge()),
            new Point(GfxConstants.AGE_TEXT_X, GfxConstants.AGE_TEXT_Y));



    // insert in delegate figure list to ensure graphical
    // rendering.
    delegate.add(turnShieldIcon);
    //    // insert in delegate figure list to ensure graphical
    // rendering.
    delegate.add(unitShieldIcon);
    delegate.add(cityShieldIcon);
    delegate.add(ageText);

//>>>>>>> feature_caleb

    // TODO: Further development to include rest of figures needed
    defineCityIcons(); // This gets called once at the start
  }

  // added method for create city figures for defineIcons() method
  protected void defineCityIcons() {
    Position p;
    for (int r = 0; r < GameConstants.WORLDSIZE; r++) {
      for (int c = 0; c < GameConstants.WORLDSIZE; c++) {
        p = new Position(r, c);
        City city = game.getCityAt(p);
        if (city != null) {
          // Convert the city's Position to (x, y) coordinates
          Point point = new Point(GfxConstants.getXFromColumn(p.getColumn()),
                  GfxConstants.getYFromRow(p.getRow()));

          // Use a single city image for all cities
          String cityImageName = GfxConstants.CITY_ICON;

          // Create a city icon using City Figure not image figure
          CityFigure cityIcon = new CityFigure(city,point);
          cityIcon.addFigureChangeListener(this);

          // Insert in delegate list
          delegate.add(cityIcon);
        }
      }
    }
  }

  // === Observer Methods ===

  public void worldChangedAt(Position pos) {
    // TODO: Remove system.out debugging output
    System.out.println( "CivDrawing: world changes at "+pos);
    // this is a really brute-force algorithm: destroy
    // all known units and build up the entire set again
    defineUnitMap();

    // TODO: Cities may change on position as well
    defineCityMap();
  }



  // added this for completing turnEnds TODO for age output:
  // Initialize the age text figure
//  public TextFigure ageText;

  public void turnEnds(Player nextPlayer, int age) {
    // TODO: Remove system.out debugging output
    System.out.println( "CivDrawing: turnEnds at "+age+", next is "+nextPlayer );
    String playername = "red";
    if ( nextPlayer == Player.BLUE ) { playername = "blue"; }
    turnShieldIcon.set( playername+"shield",
                        new Point( GfxConstants.TURN_SHIELD_X,
                                   GfxConstants.TURN_SHIELD_Y ) );
    // TODO: Age output pending
    // Update age text in the graphical view
    ageText.setText("Age: " + age);
    ageText.setFont(new Font("SansSerif", Font.PLAIN, 16));  // Example font customization
    ageText.setTextColor(Color.YELLOW);  // Example text color customization
  }

  public void tileFocusChangedAt(Position position) {
    // System.out.println("Fake it: tileFocusChangedAt " + position);
    // Clear the existing selection by deselecting all figures
    delegate.clearSelection();

    // Use an iterator to iterate over the figures and find the one corresponding to the focused tile
    Iterator<Figure> iterator = delegate.iterator();
    while (iterator.hasNext()) {
      Figure figure = iterator.next();

      // Implement the logic to determine if the figure corresponds to the focused tile
      if (figureIsAtPosition(figure, position)) {
        // Add the figure to the selection to highlight or interact with it
        delegate.addToSelection(figure);
        break; // No need to continue iterating
      }
    }

    // update the color of the unit or the city that corresponds to the player
    City city = game.getCityAt(position);
    Unit unit = game.getUnitAt(position);

    if(city != null){
      // update the city shield color
      // get the player that owns that city
      // check if the delegate has not been added and then add it
      delegate.add(cityShieldIcon);
      Player owner = city.getOwner();
      // get the production in the city and display
      productionIcon.set(city.getProduction(),
              new Point( GfxConstants.CITY_PRODUCTION_X, GfxConstants.CITY_PRODUCTION_Y));
      // get the balance in the city and display that
      workForceFocusIcon.set(city.getWorkforceFocus(),
              new Point( GfxConstants.WORKFORCEFOCUS_X, GfxConstants.WORKFORCEFOCUS_Y));
      // delegate them in
      delegate.add(productionIcon);
      delegate.add(workForceFocusIcon);

      // Print out what should be produced
      System.out.println("The production in the city: " + city.getProduction());
      System.out.println("The workForceFocus in the city: " + city.getWorkforceFocus());


      switch(owner){
        case RED:
          // update the shield color
          cityShieldIcon.set(GfxConstants.RED_SHIELD,
                  new Point (GfxConstants.CITY_SHIELD_X,
                            GfxConstants.CITY_SHIELD_Y));
          break;
        case BLUE:
          // update the shield color
          cityShieldIcon.set(GfxConstants.BLUE_SHIELD,
                  new Point (GfxConstants.CITY_SHIELD_X,
                          GfxConstants.CITY_SHIELD_Y));
          break;
        default:
          System.out.println("default");
      }

    }
    if (unit != null){
      // update the unit shield color
      // get the player that owns that unit
      int moveCount = unit.getMoveCount();
      Player owner = unit.getOwner();
      delegate.add(unitShieldIcon);
      // setting the text with the moves left
      moveText.setText(Integer.toString(moveCount));
      delegate.add(moveText);
      switch(owner){
        case RED:
          // update the shield color
          System.out.println("Should change the unit shield to red");
          System.out.println("Move count: " + moveCount);
          unitShieldIcon.set(GfxConstants.RED_SHIELD,
                  new Point (GfxConstants.UNIT_SHIELD_X,
                          GfxConstants.UNIT_SHIELD_Y));
          break;
        case BLUE:
          // update the shield color
          unitShieldIcon.set(GfxConstants.BLUE_SHIELD,
                  new Point (GfxConstants.UNIT_SHIELD_X,
                          GfxConstants.UNIT_SHIELD_Y));
          break;
        default:
          System.out.println("default");
      }
    }
  }

  // added figureIsAtPosition and getPointFromPosition to implement tileFocusChangedAt() TODO
  // Replace this method with the actual logic to determine if a figure is at a specific position
  private boolean figureIsAtPosition(Figure figure, Position position) {
    // Implement the logic to check if the figure is at the specified position
    // This might involve checking the bounding box or other figure attributes.
    // Replace this with your actual logic.
    // Example:
    Rectangle figureBounds = figure.displayBox();
    Point figurePosition = new Point(figureBounds.x, figureBounds.y);
    return figurePosition.equals(getPointFromPosition(position));
  }

  // Replace this method with the logic to convert a Position to a Point
  private Point getPointFromPosition(Position position) {
    // Implement the logic to convert a game Position to a graphical Point
    // Replace this with your actual logic.
    // Example:
    int x = GfxConstants.getXFromColumn(position.getColumn());
    int y = GfxConstants.getYFromRow(position.getRow());
    return new Point(x, y);
  }

  @Override
  public void requestUpdate() {
    // A request has been issued to repaint
    // everything. We simply rebuild the
    // entire Drawing.
    defineUnitMap();
    // TODO: Cities pending
    defineCityMap();
    defineIcons();
  }

  @Override
  public void addToSelection(Figure arg0) {
    delegate.addToSelection(arg0);
  }

  @Override
  public void clearSelection() {
    delegate.clearSelection();
  }

  @Override
  public void removeFromSelection(Figure arg0) {
    delegate.removeFromSelection(arg0);
  }

  @Override
  public List<Figure> selection() {
    return delegate.selection();
  }

  @Override
  public void toggleSelection(Figure arg0) {
    delegate.toggleSelection(arg0);
  }

  @Override
  public void figureChanged(FigureChangeEvent arg0) {
    delegate.figureChanged(arg0);
  }

  @Override
  public void figureInvalidated(FigureChangeEvent arg0) {
    delegate.figureInvalidated(arg0);
  }

  @Override
  public void figureRemoved(FigureChangeEvent arg0) {
    delegate.figureRemoved(arg0);
  }

  @Override
  public void figureRequestRemove(FigureChangeEvent arg0) {
    delegate.figureRequestRemove(arg0);
  }

  @Override
  public void figureRequestUpdate(FigureChangeEvent arg0) {
    delegate.figureRequestUpdate(arg0);
  }

  @Override
  public void addDrawingChangeListener(DrawingChangeListener arg0) {
    delegate.addDrawingChangeListener(arg0);   
  }

  @Override
  public void removeDrawingChangeListener(DrawingChangeListener arg0) {
    delegate.removeDrawingChangeListener(arg0);
  }

  @Override
  public Figure findFigure(int arg0, int arg1) {
    return delegate.findFigure(arg0, arg1);
  }

  @Override
  public Iterator<Figure> iterator() {
    return delegate.iterator();
  }

  @Override
  public void lock() {
    delegate.lock();
  }

  @Override
  public Figure remove(Figure arg0) {
    return delegate.remove(arg0);
  }

  @Override
  public void unlock() {
    delegate.unlock();
  }
}
