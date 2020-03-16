package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import org.junit.rules.Timeout;

public class TestRelativeDirection {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);
    @Test
    public void testRowNeighbours(){
        Tile tileA = new Tile("A1A10");
        Tile tileB = new Tile("A1A20");
        assertEquals(Tile.relativeDirection(tileA,tileB),'d','a');
        Tile tileC = new Tile("A1B20");
        assertEquals(Tile.relativeDirection(tileA,tileC),'n');
    }
    @Test
    public void testColNeighbours(){
        Tile tileA = new Tile("B1A10");
        Tile tileB = new Tile("A1B10");
        assertEquals(Tile.relativeDirection(tileA,tileB),'w','d');
        Tile tileC = new Tile("A1C20");
        assertEquals(Tile.relativeDirection(tileA,tileC),'n');
    }
}
