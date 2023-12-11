package hotciv.standard;

import hotciv.standard.Interfaces.ChangeProduction;
import hotciv.framework.*;
import hotciv.standard.GameImpl;
import hotciv.standard.Interfaces.MutableGame;
import hotciv.standard.Interfaces.SetFocus;

import java.util.Objects;

public class GenericSetFocus implements SetFocus {
    @Override
    public void setFocus(Position p, String balance, MutableGame g){
        CityImpl newCity = (CityImpl)g.getCityAt(p);
        if(newCity!= null){
            newCity.setWorkForceFocus(balance);
        }
    }
}