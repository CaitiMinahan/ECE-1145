package hotciv.visual;

import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.standard.Factories.SemiCivFactory;
import hotciv.stub.StubGame2;
import hotciv.view.GfxConstants;
import minidraw.framework.*;
import minidraw.standard.*;

import java.awt.event.MouseEvent;

/** Template code for exercise FRS 36.43.

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
public class ShowAction {
  
  public static void main(String[] args) {
    Game game = new StubGame2();

    DrawingEditor editor = 
      new MiniDrawApplication( "Shift-Click unit to invoke its action",  
                               new HotCivFactory4(game) );
    editor.open();
    editor.showStatus("Shift-Click on unit to see Game's performAction method being called.");

    // Add the ActionTool
    ActionTool actionTool = new ActionTool(editor, game);
    editor.setTool(actionTool);
  }
}

class ActionTool extends NullTool {
  private Game game;
  private DrawingEditor editor;

  public ActionTool(DrawingEditor editor, Game game) {
    this.editor = editor;
    this.game = game;
  }
  public void mouseDown(MouseEvent e, int x, int y) {
    // Check if the shift key is down
    if (e.isShiftDown()) {
      // Convert the x and y coordinates to a game position
      Position pos = GfxConstants.getPositionFromXY(x, y);

      // Invoke the action of the unit at the position, if any
      if (game.getUnitAt(pos) != null) {
        game.performUnitActionAt(pos);
      }
    }
  }
}
