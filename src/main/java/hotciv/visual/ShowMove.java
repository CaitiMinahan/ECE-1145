package hotciv.visual;

import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.stub.StubGame2;
import hotciv.view.GfxConstants;
import hotciv.view.UnitFigure;
import minidraw.framework.*;
import minidraw.standard.handlers.DragTracker;
import minidraw.standard.*;

import java.awt.event.MouseEvent;

/** Template code for exercise FRS 36.39.

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
public class ShowMove {

  public static void main(String[] args) {
    Game game = new StubGame2();

    DrawingEditor editor =
            new MiniDrawApplication("Move any unit using the mouse",
                    new HotCivFactory4(game));
    editor.open();
    editor.showStatus("Move units to see Game's moveUnit method being called.");

    MoveTool moveTool = new MoveTool(editor, game);
    editor.setTool(moveTool);
  }
}

class MoveTool extends NullTool{
  // private game
  private Game game;
  // drawing editor
  private DrawingEditor editor;

  private int fromX, toX, fromY, toY;
  private Tool cachedTool, child;
  private Figure draggedFig;

  private Tool createDragTracker(Figure draggedFig){
    return new DragTracker(editor, draggedFig);
  }

  public MoveTool(DrawingEditor editor, Game game){
    this.editor = editor;
    this.game = game;
    child = cachedTool = new NullTool();
  }

  // override the implementation and see what needs to happen for a move (drag)
  @Override
  public void mouseDown(MouseEvent e, int x, int y) {
    // lock the model
    Drawing model = editor.drawing();
    model.lock();
    // stash the x and y from coordinates
    fromX = x;
    fromY = y;

    draggedFig = model.findFigure(x, y);
    if (draggedFig != null && draggedFig instanceof UnitFigure) {
      child = createDragTracker(draggedFig);
      child.mouseDown(e, x, y);
    }
  }

  @Override
  public void mouseDrag(MouseEvent e, int x, int y){
    child.mouseDrag(e, x, y);
  }

  @Override
  public void mouseMove(MouseEvent e, int x, int y){
    child.mouseMove(e, x, y);
  }

  public void mouseUp(MouseEvent e, int x, int y){
    editor.drawing().unlock();
    toX = x;
    toY = y;

    // try and convert these to positions
    if(draggedFig instanceof UnitFigure) {
      Position from = GfxConstants.getPositionFromXY(fromX, fromY);
      Position to = GfxConstants.getPositionFromXY(toX, toY);

      // try and run the move unit function if
      // compare the to and from and make sure they are different
      if (!from.equals(to)) {
        boolean canMove = game.moveUnit(from, to);

        if (canMove) {
          System.out.println("unit was moved from " + from + " to " + to);
        }
        if(!canMove){
          draggedFig.moveBy(fromX - x, fromY - y);
        }
      }
    }
    child.mouseUp(e, x, y);
    child = cachedTool;
    draggedFig = null;
  }
}
