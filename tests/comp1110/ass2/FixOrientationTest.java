package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class FixOrientationTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(5000);

    @Test
    public void testEmpty(){
        String output = Tile.fixOrientationStr("");
        assertTrue("Expected: " + "\"\", but got \"" + output + "\"", output.equals(""));
    }


    private final String[] standardInput  = {"S0A10", "A1D41", "B2C40", "B1C64"};
    private final String[] standardOutput = {"S0A10", "A1D41", "B2C40", "B1C64"};

    @Test
    public void standardOrientation(){
        orientationTest(standardInput, standardOutput);
    }


    private final String[] twoOrientationInput  = {"S5A17", "A1B52", "A4C66", "B0D55", "B2E44"};
    private String[] twoOrientationOutput = {"S5A11", "A1B50", "A4C60", "B0D51", "B2E40"};


    @Test
    public void twoOrientation(){
        orientationTest(twoOrientationInput, twoOrientationOutput);

    }


    private final String[] fourOrientationInput  = {"S1E16", "S0A14", "S4B66", "A0C77", "A2E56", "A3A44", "A5F55", "S2E55", "S3F66"};
    private final String[] fourOrientationOutput = {"S1E12", "S0A10", "S4B63", "A0C70", "A2E50", "A3A42", "A5F52", "S2E50", "S3F60"};

    @Test
    public void fourOrientation(){
        orientationTest(fourOrientationInput, fourOrientationOutput);
    }


    public void orientationTest(String[] input, String[] output){
        for(int i = 0; i < input.length; i ++){
            Tile t = new Tile(input[i]);
            t.fixOrientation();
            String result = t.toString();
            String expected = output[i];
            assertTrue("Expected: " + expected + " but got " + result, result.equals(expected));
        }

    }
}
