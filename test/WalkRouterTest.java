import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class WalkRouterTest {

    private static final String URL_STRING = "https://s3-eu-west-1.amazonaws.com/citymapper-assets/citymapper-coding-test-graph.dat";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void mainTest_directNeighbours() throws Exception {
        String[] inputArr = {URL_STRING, "1522658960", "439984987"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(1 + "\r\n", outContent.toString());
    }

    @Test
    public void mainTest_indirectNeighbours() throws Exception {
        String[] inputArr = {URL_STRING, "1522658960", "1522658933"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(210 + "\r\n", outContent.toString());
    }

    @Test (expected = IllegalArgumentException.class)
    public void mainTest_noNeighbours() throws Exception {
        String[] inputArr = {URL_STRING, "0", "1"};
        WalkRouter.main(inputArr);
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}