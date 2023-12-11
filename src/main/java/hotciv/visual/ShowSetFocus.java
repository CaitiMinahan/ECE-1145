package hotciv.visual;

import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.stub.StubGame2;
import hotciv.view.GfxConstants;
import minidraw.framework.*;
import minidraw.standard.*;

import java.awt.event.MouseEvent;

/** Template code for exercise FRS 36.40.

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
public class ShowSetFocus {
  
  public static void main(String[] args) {
    Game game = new StubGame2();

    DrawingEditor editor = 
      new MiniDrawApplication( "Click any tile to set focus",  
                               new HotCivFactory4(game) );
    editor.open();
    editor.showStatus("Click a tile to see Game's setFocus method being called.");

    SetFocusTool setFocusTool = new SetFocusTool(editor, game);
    editor.setTool(setFocusTool);
  }
}

class SetFocusTool extends NullTool{
  private Game game;
  private DrawingEditor editor;

  public SetFocusTool(DrawingEditor editor, Game game){
    this.editor = editor;
    this.game = game;
  }

  // override the implementation and see what needs to happen for a move (click)
  public void mouseUp(MouseEvent e, int x, int y) {
    // set the focus to a tile that was clicked on
    Position newPosition = GfxConstants.getPositionFromXY(x,y);
    game.setTileFocus(newPosition);
  }
}
