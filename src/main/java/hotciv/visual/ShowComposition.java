package hotciv.visual;

import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.stub.StubGame2;
import minidraw.framework.*;
import minidraw.standard.*;
import java.awt.event.MouseEvent;
import hotciv.view.GfxConstants;
import java.awt.Rectangle;

/** Template code for exercise FRS 36.44.

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Computer Science Department
     Aarhus University
   
   This source code is provided WITHOUT ANY WARRANTY either 
   expressed or implied. You may study, use, modify, and 
   distribute it for non-commercial purposes. For any 
   commercial use, see http://www.baerbak.com/
 */
public class ShowComposition {
  
  public static void main(String[] args) {
    Game game = new StubGame2();

    DrawingEditor editor = 
      new MiniDrawApplication( "Click and/or drag any item to see all game actions",  
                               new HotCivFactory4(game) );
    editor.open();
    editor.showStatus("Click and drag any item to see Game's proper response.");

    CompositionTool compositionTool = new CompositionTool(editor, game);
    editor.setTool(compositionTool);
  }
}
class CompositionTool extends NullTool {
  private Game game;
  private DrawingEditor editor;

  private boolean drag = false;

  // Tools
   private MoveTool moveTool;
   private EndOfTurnTool endOfTurnTool;
  private ActionTool actionTool;
   private SetFocusTool setFocusTool;

  public CompositionTool(DrawingEditor editor, Game game){
    this.editor = editor;
    this.game = game;

    // Initialize the tools
    this.moveTool = new MoveTool(this.editor, this.game);
    this.endOfTurnTool = new EndOfTurnTool(this.editor, this.game);
    this.actionTool = new ActionTool(this.editor, this.game);
    this.setFocusTool = new SetFocusTool(this.editor, this.game);
  }

  // Mouse Events
  public void mouseUp(MouseEvent e, int x, int y){
    // Convert the x and y coordinates to a game position
    Position pos = GfxConstants.getPositionFromXY(x, y);

    // If the player is dragging the mouse
    if(drag) {
      moveTool.mouseUp(e, x, y);
      drag = false;
    }
    // If mouse ends up on a unit
    else if(game.getUnitAt(pos) != null){
      // Select the box of where that unit is
      System.out.println("Using SetFocusTool to set focus on the unit");
      setFocusTool.mouseUp(e, x, y);
    }
    // If mouse ends up on a city
    else if(game.getCityAt(pos) != null){
      // Select the box of where that city is
      System.out.println("Using SetFocusTool to set focus on the city");
      setFocusTool.mouseUp(e, x, y);
    }
  }
  public void mouseDown(MouseEvent e, int x, int y){
    // Convert the x and y coordinates to a game position
    Position pos = GfxConstants.getPositionFromXY(x, y);

    // Set x and y range to determine what tool to use
    boolean y_ShieldRange = (y <= 104) && (y >= 65);
    boolean x_ShieldRange = (x <= 588) && (x >= 560);

    // If both coordinates are in the range
    // That means you're on the EndOfTurnTool
    if(y_ShieldRange && x_ShieldRange){
      System.out.println("Calling EndOfTurnTool");
       endOfTurnTool.mouseDown(e, x, y);
    }
    // If the player clicks on a unit
    else if(game.getUnitAt(pos) != null){
      // If the player is pressing shift while clicking
      // ActionTool should be called
      if(e.isShiftDown()){
        System.out.println("Calling ActionTool");
        actionTool.mouseDown(e, x, y);
      }
      // If the player isn't pressing shift while clicking
      // MoveTool should be called
      else{
        System.out.println("Calling MoveTool");
        moveTool.mouseDown(e, x, y);
      }
    }
  }
  public void mouseDrag(MouseEvent e, int x, int y){
    // If this function is called, the mouse is being dragged
    // Update boolean
    drag = true;
    moveTool.mouseDrag(e, x, y);
  }
}