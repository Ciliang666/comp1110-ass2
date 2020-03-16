package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class RouteTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(5000);


    @Test
    //Every tile in a route should be connected to at least one of other tiles
    public void routeConnected(){
        for(String game : games){
            //For each game
            Board b = new Board(game);
            ArrayList<ArrayList<Tile>> routes = b.routes();
            for(ArrayList<Tile> route : routes){
                //For each route
                for(int i = 0; i < route.size() - 1; i ++){
                    //Check each tile
                    Tile t1 = route.get(i);
                    boolean connected = false;
                    for(int j = 0; j < route.size(); j ++){
                        Tile t2 = route.get(j);
                        if(Tile.twoTileConnected(t1, t2)){
                            connected = true;
                            break;
                        }
                    }
                    assertTrue("Tile is not connected: " + t1, connected);
                }
            }
        }
    }



    @Test
    //One valid route should have at least one exit
    public void routeHasExit(){
        for(String game : games){
            //For each game
            Board b = new Board(game);
            ArrayList<ArrayList<Tile>> routes = b.routes();
            for(ArrayList<Tile> route : routes){
                //For each route
                String thisRoute = "";
                boolean hasExit = false;
                for(Tile t : route){
                    //Check each tile
                    thisRoute += t;
                    if(t.isExit()){
                        hasExit = true;
                        break;
                    }
                }
                assertTrue("No exit in this route: " + thisRoute, hasExit);
            }
        }
    }


    private final String[] games = {
            "A4A12B2B16A1B01A1B23S1B32A1A32B1B44B2A44A4C16A3D15A4D01A5D23A4E20B1F24A2F17A1F01B0G16A5C34A4C43A5C53A3D50A4D61S4E50A0F51A1F67S2E46B1E31A1F30A2G36A1G41B1G52",
            "A3D61A3D53B0C52A0B52A2B63A4D41B0E60A0F61A3D31A3D23A2G30B0F34A3E32A1B01B2B10A1B21A0A63A4D01A1G41B0G12S2D10A4C10B2A10A2B33A1A30S4E11A4E21A3C21A3C31S5F11",
            "A3A10A3A52A3G10B2F10S1B50A2B61A0C60A1B41B1A35A4A41A2B31A1C30B0D32A2C50A4E10A3D12B2B10A2F01A0G00A4D01B1A27S3B20A4C10A1D50A0F23B2G25A3E30A4E41",
            "A2A30A0A43A3A50B2B50A4C50A3D50A2B43B0G12B0A14A2B33A0B11A4E50A3D61A2B21A3G52B1G45A3F52B2F41A3F33A1E40A1D40A3E32A3E27B0F10S0E12B1D17A4D01A1B61A0C43",
            "A4A50A1F61A0B61S5F50B1F46A1F01S1F12A2F23A1E20B2D21A3D03A1C20A0B22B1A61A4D11A4G10B1G44A2G30A3C01A3C12B0B31A1B01A4B50B0C50A2F32A0E32A0E40A4D31B1D47A1B11",
            "A4A50A1A30B2B31A0C34A3B41B2C40A3B52A2B60A2C62S5C50B1D65A4B21A2A60A3B10A4A10A4C10B2G10B2F10A4E10A3D12A1F01S2D00A4C00B1B02A0F23A0G20A2F61B2F50A3G52A0G02",
            "A4G10B2F10A4E10A0F20A3D17A0E22A2E31B1E44S0D42A3D23A4D31A2F30B1F42A1G30A0C42A0C57B0C22A2F03A1E02S5D01A0B22B0A50A4D51A3D61B2B53A0B30B2A31A4E60A3A41A0B03",
            "A0F00A0B00A0A31B1A14A0B61A0F61A0G32B1D61A0G43A0A62A0E61B1G56S1G60A0E03A0A03B1G12A0G02S0A50A0B50A5D03B1B40",
            "A5D00A0B03A0A30B1A10S3A20A0F03A0G32A5G53B1F60A5D61A0B61A0G43B1A54S1A40A0B40A0B31S4A60A5C62B1G12A0G01A0C00A5E62B1B23",
            "A0F00A0B00A0A30B1A10A0B61A0F61A0G32B1D61A0G43A0A62A0E61B1G56S1G60S5A20A0E03A0A03B1G12A0G02S0A50A0B50A0B41B1A41",
            "A4A10A1A30A4A50S1B32A1B01A1B61B2B10A1B21S5B50A1B41A4D01A4D61A3D12B0C30A3D50A4C10A4C50A1F01A1F61A4G10B1F12A4G50B1E10A1E21A3E52B1F56S4E31",
            "A4A50A0B61A3D60B1F60S0E61A0E52A5G52A4G61B2B50A0A30A1A21A5A10B2G31A3G41B0G12A0F13A2F01S1D03A2B02A1C00B0A00A3G21S3G00A1E00A4C50B1D50A0F51",
            "A3A53S0A44B1A35A0B03A0F07A0B61A4A61A1C00B0D02S4E00A4G10A4F10B1E12A2G31S3G20A4A10A4B10A4C10B0D10A5D62A0F61B1G56A2E67B0G41A2F22",
            "A1A30A1G30B0F30A4E30S2D30A1B30B0C32A4D41A1B61S1B50A1B01S5B10A1B21A4A10B0C10A1B41A1F61A4D61B0F53A4D51B0D21A1D11A1F01A4G10A1F11B0D03A1F21",
            "A3A10B2A31A1B30A0F61A4A21B1B14A4A41A4D61S2A50A5A63A2B01A1C02B0G52S0B63A0E63A2E51A4D51B0C32A5D31A5C61A0E41S5D41B1D03A5B51A4G10A0C42B0G30"
    };
}
