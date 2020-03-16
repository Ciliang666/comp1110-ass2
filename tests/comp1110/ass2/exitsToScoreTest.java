package comp1110.ass2;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;


public class exitsToScoreTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(5000);

    @Test
    public void testSmallerThanTwelve() {
        try {
            this.test(10,36 );
        } catch (InputMismatchException var2) {
            Assert.assertTrue("Unexpected output", false);
        } catch (NoSuchElementException var3) {
            Assert.assertTrue("No output", false);
        }

    }

    @Test
    public void testEqualToTwelve() {
        try {
            this.test(12, 45);
        } catch (InputMismatchException var2) {
            Assert.assertTrue("Unexpected output", false);
        } catch (NoSuchElementException var3) {
            Assert.assertTrue("No output", false);
        }

    }

    @Test
    public void testGreaterThanTwelve() {
        try {
            this.test(13, 45);
        } catch (InputMismatchException var2) {
            Assert.assertTrue("Unexpected output", false);
        } catch (NoSuchElementException var3) {
            Assert.assertTrue("No output", false);
        }

    }


    private static void test(int exits, int ref) {

        int output = Board.exitsToScore(exits);
        Assert.assertTrue("Incorrect output for input " + output + ", expected: " + ref + ", got: " + output, output == ref);
    }
}