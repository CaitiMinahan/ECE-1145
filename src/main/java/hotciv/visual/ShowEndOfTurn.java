package hotciv.visual;

import hotciv.framework.Game;
import hotciv.stub.StubGame2;
import hotciv.view.GfxConstants;
import minidraw.framework.*;
import minidraw.standard.*;

import java.awt.event.MouseEvent;

/** Template code for exercise FRS 36.42.

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
public class ShowEndOfTurn {
  
  public static void main(String[] args) {
    Game game = new StubGame2();

    DrawingEditor editor = 
      new MiniDrawApplication( "Click top shield to end the turn",  
                               new HotCivFactory4(game) );
    editor.open();
    editor.showStatus("Click to shield to see Game's endOfTurn method being called.");

    EndOfTurnTool endOfTurnTool = new EndOfTurnTool(editor,game);
    editor.setTool(endOfTurnTool);
  }
}

// create a new class that extends the tool
class EndOfTurnTool extends NullTool{
  private Game game;
  private DrawingEditor editor;
  public EndOfTurnTool(DrawingEditor editor, Game game){
    this.editor = editor;
    this.game = game;
  }

  // override the implementation and make sure it works for end of turn (click)
  public void mouseDown(MouseEvent e, int x, int y){
    // get the location of x-y and if on the shield location then do the end of turn
    if ((x >= GfxConstants.TURN_SHIELD_X && x <= GfxConstants.TURN_SHIELD_X + 27) &&
            (y >= GfxConstants.TURN_SHIELD_Y && y <= GfxConstants.TURN_SHIELD_X + 39))
    {
      // we have a click inside the turn shield
      System.out.println("We have a click inside the turn shield. Run EOT");
      // run end of turn
      game.endOfTurn();

    }
  }
}
