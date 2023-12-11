package hotciv.standard;

import hotciv.framework.GameObserver;
import hotciv.framework.*;

public class GameObserverSpy implements GameObserver {
    public boolean worldChangedAtCalled = false;
    public boolean turnEndsCalled = false;
    public boolean tileFocusChangedAtCalled = false;

    @Override
    public void worldChangedAt(Position pos) {
        // Set the flag to true to indicate that the method was called
        worldChangedAtCalled = true;
    }

    @Override
    public void turnEnds(Player nextPlayer, int age) {
        // Set the flag to true to indicate that the method was called
        turnEndsCalled = true;
    }

    @Override
    public void tileFocusChangedAt(Position position) {
        // Set the flag to true to indicate that the method was called
        tileFocusChangedAtCalled = true;
    }
}

// the GameObserverSpy is d a type of Test Spy. In the context of testing, the term "spy"
// refers to an object that records information about method calls made on it, allowing you to later verify those calls.
// A Test Spy helps you observe and verify interactions between objects during testing

// We will use this to implement the GameObserver interface and create automated testing of the Subject behavior