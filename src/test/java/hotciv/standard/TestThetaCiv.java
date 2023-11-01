package hotciv.standard;
import hotciv.framework.*;
import hotciv.standard.Factories.ThetaCivFactory;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
public class TestThetaCiv {
    private GameImpl game;
    private UnitAction thetaUnitAction;
    private GameFactory gameFactory;

    @Before
    public void setUp() {
        gameFactory = new ThetaCivFactory();
        game = new GameImpl(gameFactory);
    }

    @Test
    public void createNewUFOUnitShouldReturnTypeCorrectly() {
        UnitImpl ufoUnit = new UnitImpl(GameConstants.UFO, Player.RED);
        assertThat(ufoUnit.getTypeString(), is("ufo"));
    }
}
